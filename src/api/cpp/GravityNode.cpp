/*
 * GravityNode.cpp
 *
 *  Created on: Aug 14, 2012
 *      Author: Chris Brundick
 */
 
#include <zmq.h>
#include <iostream>
#include <pthread.h>
#include <assert.h>
#ifdef WIN32
#include <winsock2.h>
#else
#include <netinet/in.h>
#include <arpa/inet.h>
#endif
#include <sstream>
#include <signal.h>
#include <tr1/memory>

#include "GravitySubscriptionManager.h"
#include "GravitySubscriptionManager.h"
#include "GravityRequestManager.h"
#include "GravityServiceManager.h"
#include "GravityHeartbeatListener.h"
#include "GravityHeartbeat.h"

#include "protobuf/ComponentLookupRequestPB.pb.h"
#include "protobuf/ComponentDataLookupResponsePB.pb.h"
#include "protobuf/ComponentServiceLookupResponsePB.pb.h"
#include "protobuf/ServiceDirectoryResponsePB.pb.h"
#include "protobuf/ServiceDirectoryRegistrationPB.pb.h"
#include "protobuf/ServiceDirectoryUnregistrationPB.pb.h"

#include "GravityNode.h" //Needs to be last on Windows so it is included after nb30.h for the DUPLICATE definition. 

std::string gravity::emptyString("");

static void* startSubscriptionManager(void* context)
{
	// Create and start the GravitySubscriptionManager
	gravity::GravitySubscriptionManager subManager(context);
	subManager.start();

	return NULL;
}

static void* startRequestManager(void* context)
{
	// Create and start the GravityRequestManager
	gravity::GravityRequestManager reqManager(context);
	reqManager.start();

	return NULL;
}

static void* startServiceManager(void* context)
{
	// Create and start the GravityServiceManager
	gravity::GravityServiceManager serviceManager(context);
	serviceManager.start();

	return NULL;
}

static int s_interrupted = 0;
static void (*previousHandlerAbrt)(int); //Function Pointer
static void (*previousHandlerInt)(int); //Function Pointer
void s_restore_signals()
{
	signal(SIGABRT, previousHandlerAbrt);
	signal(SIGINT, previousHandlerInt);
}

static void s_signal_handler(int signal_value)
{
    s_interrupted = signal_value;
    s_restore_signals();
}

void s_catch_signals()
{
	previousHandlerAbrt = signal(SIGABRT, s_signal_handler);
	previousHandlerInt = signal(SIGINT, s_signal_handler);
}


namespace gravity
{

using namespace std;
using namespace std::tr1;

GravityNode::GravityNode()
{
    // Eventually to be read from a config/properties file
    serviceDirectoryNode.ipAddress = "localhost";
    serviceDirectoryNode.port = 5555;
    serviceDirectoryNode.transport = "tcp";
    serviceDirectoryNode.socket = NULL;

    //Initialize this guy so we can know whether the heartbeat thread has started.
    hbSocket = NULL;
}

GravityNode::~GravityNode()
{
	// Close the inproc sockets
	sendStringMessage(subscriptionManagerSocket, "kill", ZMQ_DONTWAIT);
	zmq_close(subscriptionManagerSocket);

	sendStringMessage(requestManagerSocket, "kill", ZMQ_DONTWAIT);
	zmq_close(requestManagerSocket);

	sendStringMessage(serviceManagerSocket, "kill", ZMQ_DONTWAIT);
	zmq_close(serviceManagerSocket);

	// Clean up any pub sockets
	for (map<string,NetworkNode*>::iterator iter = publishMap.begin(); iter != publishMap.end(); iter++)
	{
		string dataProductID = iter->first;
		unregisterDataProduct(dataProductID);
	}

    // Clean up the zmq context object
    zmq_term(context);
}

GravityReturnCode GravityNode::init()
{
    GravityReturnCode ret = GravityReturnCodes::SUCCESS;

    // Setup zmq context
    context = zmq_init(1);
    if (!context)
    {
        ret = GravityReturnCodes::FAILURE;
    }

    void* initSocket = zmq_socket(context, ZMQ_REP);
    zmq_bind(initSocket, "inproc://gravity_init");

    // Setup up communication channel to subscription manager
    subscriptionManagerSocket = zmq_socket(context, ZMQ_PUB);
    zmq_bind(subscriptionManagerSocket, "inproc://gravity_subscription_manager");

    // Setup the subscription manager
    pthread_t subscriptionManager;
    pthread_create(&subscriptionManager, NULL, startSubscriptionManager, context);

    // Setup up communication channel to request manager
    requestManagerSocket = zmq_socket(context, ZMQ_PUB);
    zmq_bind(requestManagerSocket, "inproc://gravity_request_manager");

    // Setup the request manager
    pthread_t requestManager;
    pthread_create(&requestManager, NULL, startRequestManager, context);

    // Setup up communication channel to service manager
    serviceManagerSocket = zmq_socket(context, ZMQ_PUB);
    zmq_bind(serviceManagerSocket, "inproc://gravity_service_manager");

    // Setup the service manager
    pthread_t serviceManager;
    pthread_create(&serviceManager, NULL, startServiceManager, context);

    // Configure to trap Ctrl-C (SIGINT) and SIGTERM signals
    s_catch_signals();

    // wait for the manager threads to signal their readiness
    string msgText;
    int numThreadsWaiting = 3;
    while (numThreadsWaiting && !s_interrupted)
    {
    	// Read message
    	msgText = readStringMessage(initSocket);

    	// respond with ack
    	sendStringMessage(initSocket, "ack", ZMQ_DONTWAIT);

    	// Decrement counter
    	numThreadsWaiting--;
    }
    zmq_bind(initSocket, "inproc://gravity_init");
    zmq_close(initSocket);
	
    s_restore_signals();

	if(s_interrupted)
		raise(s_interrupted);

    return ret;
}

void GravityNode::sendGravityDataProduct(void* socket, const GravityDataProduct& dataProduct)
{
    // Send data product
    zmq_msg_t data;
    zmq_msg_init_size(&data, dataProduct.getSize());
    dataProduct.serializeToArray(zmq_msg_data(&data));
    zmq_sendmsg(socket, &data, ZMQ_DONTWAIT);
    zmq_msg_close(&data);
}

GravityReturnCode GravityNode::sendRequestToServiceProvider(string url, const GravityDataProduct& request,
        GravityDataProduct& response)
{
    GravityReturnCode ret = GravityReturnCodes::SUCCESS;

    // Socket to connect to service provider
    void* socket = NULL;

    int retriesLeft = NETWORK_RETRIES;
    while (retriesLeft && !s_interrupted)
    {
        // Connect to service directory component
        socket = zmq_socket(context, ZMQ_REQ);
        zmq_connect(socket, url.c_str());

        // Send registration message to service directory
        sendGravityDataProduct(socket, request);
        retriesLeft--;

        // Poll socket for reply with a timeout
        zmq_pollitem_t items[] = {{socket, 0, ZMQ_POLLIN, 0}};
        int rc = zmq_poll(items, 1, NETWORK_TIMEOUT);
        if (rc == -1)
        {
            // Interrupted
            ret = GravityReturnCodes::INTERRUPTED;

            // Close socket
            zmq_close(socket);

            break;
        }

        // Process the directory service response
        if (items[0].revents & ZMQ_POLLIN)
        {
            // Get service directory response
            zmq_msg_t resp;
            zmq_msg_init(&resp);
            zmq_recvmsg(socket, &resp, 0);

            bool parserSuccess = true;
            try
            {
                response.parseFromArray(zmq_msg_data(&resp), zmq_msg_size(&resp));
            }
            catch (char* s)
            {
                parserSuccess = false;
            }

            // Clean up message
            zmq_msg_close(&resp);

            if (parserSuccess)
            {
                ret = GravityReturnCodes::SUCCESS;
                retriesLeft = 0;

                // Close socket
                zmq_close(socket);
                break;
            }
            else
            {
                // Bad response.
                ret = GravityReturnCodes::LINK_ERROR;
            }
        }
        else
        {
            ret = GravityReturnCodes::NO_SERVICE_PROVIDER;
        }

        // Close socket
        zmq_close(socket);
    }
	
	if(s_interrupted)
		raise(s_interrupted);

    return ret;
}

GravityReturnCode GravityNode::sendRequestToServiceDirectory(const GravityDataProduct& request,
        GravityDataProduct& response)
{
	stringstream ss;
	ss << serviceDirectoryNode.transport << "://" << serviceDirectoryNode.ipAddress <<
	                ":" << serviceDirectoryNode.port;
	string serviceDirectoryURL = ss.str();

	return sendRequestToServiceProvider(serviceDirectoryURL, request, response);
}

GravityReturnCode GravityNode::registerDataProduct(string dataProductID, unsigned short networkPort, string transportType)
{
    GravityReturnCode ret = GravityReturnCodes::SUCCESS;

    // Build the connection string
    stringstream ss;
    string ipAddr = getIP();
    ss << transportType << "://" << ipAddr << ":" << networkPort;
    string connectionString = ss.str();

    // Create the publish socket
    void* pubSocket = zmq_socket(context, ZMQ_PUB);
    if (!pubSocket)
    {
        return GravityReturnCodes::FAILURE;
    }

    // Bind socket to url
    int rc = zmq_bind(pubSocket, connectionString.c_str());
    if (rc != 0)
    {
        zmq_close(pubSocket);
        return GravityReturnCodes::REGISTRATION_CONFLICT;
    }

    // Track dataProductID->socket mapping
    NetworkNode* node = new NetworkNode;
    node->ipAddress = ipAddr;
    node->port = networkPort;
    node->transport = transportType;
    node->socket = pubSocket;
    publishMap[dataProductID] = node;

    if (ret == GravityReturnCodes::SUCCESS && !serviceDirectoryNode.ipAddress.empty())
    {
        // Create the object describing the data product to register
        ServiceDirectoryRegistrationPB registration;
        registration.set_id(dataProductID);
        registration.set_url(connectionString);
        registration.set_type(ServiceDirectoryRegistrationPB::DATA);

        // Wrap request in GravityDataProduct
        GravityDataProduct request("RegistrationRequest");
        request.setData(registration);

        // GravityDataProduct for response
        GravityDataProduct response("RegistrationResponse");

        // Send request to service directory
        ret = sendRequestToServiceDirectory(request, response);
        if (ret == GravityReturnCodes::SUCCESS)
        {
            ServiceDirectoryResponsePB pb;
            bool parserSuccess = true;
            try
            {
                response.populateMessage(pb);
            }
            catch (char* s)
            {
                parserSuccess = false;
            }

            if (parserSuccess)
            {
                switch (pb.returncode())
                {
                case ServiceDirectoryResponsePB::SUCCESS:
                    ret = GravityReturnCodes::SUCCESS;
                    break;
                case ServiceDirectoryResponsePB::REGISTRATION_CONFLICT:
                    ret = GravityReturnCodes::REGISTRATION_CONFLICT;
                    break;
                case ServiceDirectoryResponsePB::DUPLICATE_REGISTRATION:
                    ret = GravityReturnCodes::DUPLICATE;
                    break;
                case ServiceDirectoryResponsePB::NOT_REGISTERED:
                    ret = GravityReturnCodes::LINK_ERROR;
                    break;
                }
            }
            else
            {
                ret = GravityReturnCodes::LINK_ERROR;
            }
        }
    }

    return ret;
}

GravityReturnCode GravityNode::unregisterDataProduct(string dataProductID)
{
    GravityReturnCode ret = GravityReturnCodes::SUCCESS;

    // Track dataProductID->socket mapping
    NetworkNode* node = publishMap[dataProductID];
    if (!node || !node->socket)
    {
        ret = GravityReturnCodes::REGISTRATION_CONFLICT;
    }
    else
    {
        stringstream ss;
        ss << node->transport << "://" << node->ipAddress << ":" << node->port;
        zmq_unbind(node->socket, ss.str().c_str());
        zmq_close(node->socket);
        publishMap[dataProductID] = NULL;
        free(node);

        ServiceDirectoryUnregistrationPB unregistration;
        unregistration.set_id(dataProductID);
        unregistration.set_url(ss.str());
        unregistration.set_type(ServiceDirectoryUnregistrationPB::DATA);

        GravityDataProduct request("UnregistrationRequest");
        request.setData(unregistration);

        // GravityDataProduct for response
        GravityDataProduct response("UnregistrationResponse");

        // Send request to service directory
        ret = sendRequestToServiceDirectory(request, response);

        if (ret == GravityReturnCodes::SUCCESS)
        {
            ServiceDirectoryResponsePB pb;
            bool parserSuccess = true;
            try
            {
                response.populateMessage(pb);
            }
            catch (char* s)
            {
                parserSuccess = false;
            }

            if (parserSuccess)
            {
                switch (pb.returncode())
                {
                case ServiceDirectoryResponsePB::SUCCESS:
                    ret = GravityReturnCodes::SUCCESS;
                    break;
                case ServiceDirectoryResponsePB::NOT_REGISTERED:
                    ret = GravityReturnCodes::REGISTRATION_CONFLICT;
                    break;
                default:
                	ret = GravityReturnCodes::FAILURE;
                	break;
                }
            }
            else
            {
                ret = GravityReturnCodes::LINK_ERROR;
            }
        }

    }

    return ret;
}

GravityReturnCode GravityNode::subscribe(string dataProductID, const GravitySubscriber& subscriber, string filter)
{
    // Create the object describing the data product to lookup
    ComponentLookupRequestPB lookup;
    lookup.set_lookupid(dataProductID);
    lookup.set_type(ComponentLookupRequestPB::DATA);

    // Wrap request in GravityDataProduct
    GravityDataProduct request("ComponentLookupRequest");
    request.setData(lookup);

    // GravityDataProduct for response
    GravityDataProduct response("ComponentLookupResponse");

    // Send request to service directory
    GravityReturnCode ret = sendRequestToServiceDirectory(request, response);

    if (ret == GravityReturnCodes::SUCCESS)
    {
        ComponentDataLookupResponsePB pb;
        bool parserSuccess = true;
        try
        {
            response.populateMessage(pb);
        }
        catch (char* s)
        {
            parserSuccess = false;
        }

        if (parserSuccess)
        {
            if (pb.url_size() > 0)
            {
                // Subscribe to all published data products
            	for (int i = 0; i < pb.url_size(); i++)
            	{
            		subscribe(pb.url(i), dataProductID, subscriber, filter);
            	}
            }
            else
            {
                ret = GravityReturnCodes::NO_SUCH_DATA_PRODUCT;
            }
        }
        else
        {
            ret = GravityReturnCodes::LINK_ERROR;
        }
    }
    else
    {
        ret = GravityReturnCodes::NO_SERVICE_DIRECTORY;
    }

    return ret;
}

void GravityNode::sendStringMessage(void* socket, string str, int flags)
{
	zmq_msg_t msg;
	zmq_msg_init_size(&msg, str.length());
	memcpy(zmq_msg_data(&msg), str.c_str(), str.length());
	zmq_sendmsg(socket, &msg, flags);
	zmq_msg_close(&msg);
}

string GravityNode::readStringMessage(void* socket)
{
	zmq_msg_t msg;
    zmq_msg_init(&msg);
    zmq_recvmsg(socket, &msg, 0);
    int size = zmq_msg_size(&msg);
    char* s = (char*)malloc(size+1);
    memcpy(s, zmq_msg_data(&msg), size);
    s[size] = 0;
    std::string str(s, size);
    delete s;
  	zmq_msg_close(&msg);

  	return str;
}

GravityReturnCode GravityNode::subscribe(string connectionURL, string dataProductID,
        const GravitySubscriber& subscriber, string filter)
{
	// Send subscription details
	sendStringMessage(subscriptionManagerSocket, "subscribe", ZMQ_SNDMORE);
	sendStringMessage(subscriptionManagerSocket, dataProductID, ZMQ_SNDMORE);
	sendStringMessage(subscriptionManagerSocket, connectionURL, ZMQ_SNDMORE);
	sendStringMessage(subscriptionManagerSocket, filter, ZMQ_SNDMORE);

	zmq_msg_t msg;
	zmq_msg_init_size(&msg, sizeof(&subscriber));
	void* v = (void*)&subscriber;
	memcpy(zmq_msg_data(&msg), &v, sizeof(&subscriber));
	zmq_sendmsg(subscriptionManagerSocket, &msg, ZMQ_DONTWAIT);
	zmq_msg_close(&msg);

    return GravityReturnCodes::SUCCESS;
}

GravityReturnCode GravityNode::unsubscribe(string dataProductID, const GravitySubscriber& subscriber, string filter)
{
	// Send unsubscribe details
	sendStringMessage(subscriptionManagerSocket, "unsubscribe", ZMQ_SNDMORE);
	sendStringMessage(subscriptionManagerSocket, dataProductID, ZMQ_SNDMORE);
	sendStringMessage(subscriptionManagerSocket, filter, ZMQ_SNDMORE);

	// Send subscriber
	zmq_msg_t msg;
	zmq_msg_init_size(&msg, sizeof(&subscriber));
	void* v = (void*)&subscriber;
	memcpy(zmq_msg_data(&msg), &v, sizeof(&subscriber));
	zmq_sendmsg(subscriptionManagerSocket, &msg, ZMQ_DONTWAIT);
	zmq_msg_close(&msg);

    return GravityReturnCodes::SUCCESS;
}

GravityReturnCode GravityNode::publish(const GravityDataProduct& dataProduct, std::string filterText)
{
    string dataProductID = dataProduct.getDataProductID();
    NetworkNode* node = this->publishMap[dataProductID];
    if (!node)
        return GravityReturnCodes::FAILURE;

    void* socket = node->socket;

    // Create message & send filter text
    sendStringMessage(socket, filterText, ZMQ_SNDMORE);

    //Set Timestamp
    dataProduct.setTimestamp(getCurrentTime());

    // Serialize data
    zmq_msg_t data;
    zmq_msg_init_size(&data, dataProduct.getSize());
    dataProduct.serializeToArray(zmq_msg_data(&data));

    // Publish data
    zmq_sendmsg(socket, &data, ZMQ_DONTWAIT);

    // Clean up
    zmq_msg_close(&data);

    return GravityReturnCodes::SUCCESS;
}

GravityReturnCode GravityNode::request(string serviceID, const GravityDataProduct& dataProduct,
        const GravityRequestor& requestor, string requestID)
{
	// Create the object describing the data product to lookup
	ComponentLookupRequestPB lookup;
	lookup.set_lookupid(serviceID);
	lookup.set_type(ComponentLookupRequestPB::SERVICE);

	// Wrap request in GravityDataProduct
	GravityDataProduct requestDataProduct("ComponentLookupRequest");
	requestDataProduct.setData(lookup);

	// GravityDataProduct for response
	GravityDataProduct responseDataProduct("ComponentLookupResponse");

	// Send request to service directory
	GravityReturnCode ret = sendRequestToServiceDirectory(requestDataProduct, responseDataProduct);

	if (ret == GravityReturnCodes::SUCCESS)
	{
		ComponentServiceLookupResponsePB pb;
		bool parserSuccess = true;
		try
		{
			responseDataProduct.populateMessage(pb);
		}
		catch (char* s)
		{
			parserSuccess = false;
		}

		if (parserSuccess)
		{

			if (!pb.url().empty())
			{
				request(pb.url(), serviceID, dataProduct, requestor, requestID);
			}
			else
			{
				ret = GravityReturnCodes::NO_SUCH_DATA_PRODUCT;
			}
		}
		else
		{
			ret = GravityReturnCodes::LINK_ERROR;
		}
	}
	else
	{
		ret = GravityReturnCodes::NO_SERVICE_DIRECTORY;
	}

	return ret;
}

GravityReturnCode GravityNode::request(string connectionURL, string serviceID, const GravityDataProduct& dataProduct,
        const GravityRequestor& requestor, string requestID)
{
	// Send subscription details
	sendStringMessage(requestManagerSocket, "request", ZMQ_SNDMORE);
	sendStringMessage(requestManagerSocket, serviceID, ZMQ_SNDMORE);
	sendStringMessage(requestManagerSocket, connectionURL, ZMQ_SNDMORE);
	sendStringMessage(requestManagerSocket, requestID, ZMQ_SNDMORE);

	zmq_msg_t msg;
	zmq_msg_init_size(&msg, dataProduct.getSize());
	dataProduct.serializeToArray(zmq_msg_data(&msg));
	zmq_sendmsg(requestManagerSocket, &msg, ZMQ_SNDMORE);
	zmq_msg_close(&msg);

	zmq_msg_init_size(&msg, sizeof(&requestor));
	void* v = (void*)&requestor;
	memcpy(zmq_msg_data(&msg), &v, sizeof(&requestor));
	zmq_sendmsg(requestManagerSocket, &msg, ZMQ_DONTWAIT);
	zmq_msg_close(&msg);

	return GravityReturnCodes::SUCCESS;
}

GravityReturnCode GravityNode::registerService(string serviceID, unsigned short networkPort,
        string transportType, const GravityServiceProvider& server)
{
	// Build the connection string
	stringstream ss;
	string ipAddr = getIP();
	ss << transportType << "://" << ipAddr << ":" << networkPort;
	string connectionString = ss.str();

	// Send subscription details
	sendStringMessage(serviceManagerSocket, "register", ZMQ_SNDMORE);
	sendStringMessage(serviceManagerSocket, serviceID, ZMQ_SNDMORE);
	sendStringMessage(serviceManagerSocket, connectionString, ZMQ_SNDMORE);

	// Include the server
	zmq_msg_t msg;
	zmq_msg_init_size(&msg, sizeof(&server));
	void* v = (void*)&server;
	memcpy(zmq_msg_data(&msg), &v, sizeof(&server));
	zmq_sendmsg(serviceManagerSocket, &msg, ZMQ_DONTWAIT);
	zmq_msg_close(&msg);

    GravityReturnCode ret = GravityReturnCodes::SUCCESS;

    serviceMap[serviceID] = connectionString;

    if (ret == GravityReturnCodes::SUCCESS && !serviceDirectoryNode.ipAddress.empty())
    {
        // Create the object describing the data product to register
        ServiceDirectoryRegistrationPB registration;
        registration.set_id(serviceID);
        registration.set_url(connectionString);
        registration.set_type(ServiceDirectoryRegistrationPB::SERVICE);

        // Wrap request in GravityDataProduct
        GravityDataProduct request("RegistrationRequest");
        request.setData(registration);

        // GravityDataProduct for response
        GravityDataProduct response("RegistrationResponse");

        // Send request to service directory
        ret = sendRequestToServiceDirectory(request, response);

        if (ret == GravityReturnCodes::SUCCESS)
        {
            ServiceDirectoryResponsePB pb;
            bool parserSuccess = true;
            try
            {
                response.populateMessage(pb);
            }
            catch (char* s)
            {
                parserSuccess = false;
            }

            if (parserSuccess)
            {
                switch (pb.returncode())
                {
                case ServiceDirectoryResponsePB::SUCCESS:
                    ret = GravityReturnCodes::SUCCESS;
                    break;
                case ServiceDirectoryResponsePB::REGISTRATION_CONFLICT:
                    ret = GravityReturnCodes::REGISTRATION_CONFLICT;
                    break;
                case ServiceDirectoryResponsePB::DUPLICATE_REGISTRATION:
                    ret = GravityReturnCodes::DUPLICATE;
                    break;
                case ServiceDirectoryResponsePB::NOT_REGISTERED:
                    ret = GravityReturnCodes::LINK_ERROR;
                    break;
                }
            }
            else
            {
                ret = GravityReturnCodes::LINK_ERROR;
            }
        }
    }

    return ret;
}

GravityReturnCode GravityNode::unregisterService(string serviceID)
{
	GravityReturnCode ret = GravityReturnCodes::SUCCESS;

	// Send subscription details
	sendStringMessage(serviceManagerSocket, "unregister", ZMQ_SNDMORE);
	sendStringMessage(serviceManagerSocket, serviceID, ZMQ_SNDMORE);


	ServiceDirectoryUnregistrationPB unregistration;
	unregistration.set_id(serviceID);
	unregistration.set_url(serviceMap[serviceID]);
	unregistration.set_type(ServiceDirectoryUnregistrationPB::SERVICE);

	GravityDataProduct request("UnregistrationRequest");
	request.setData(unregistration);

	// GravityDataProduct for response
	GravityDataProduct response("UnregistrationResponse");

	// Send request to service directory
	ret = sendRequestToServiceDirectory(request, response);

	if (ret == GravityReturnCodes::SUCCESS)
	{
		ServiceDirectoryResponsePB pb;
		bool parserSuccess = true;
		try
		{
			response.populateMessage(pb);
		}
		catch (char* s)
		{
			parserSuccess = false;
		}

		if (parserSuccess)
		{
			switch (pb.returncode())
			{
			case ServiceDirectoryResponsePB::SUCCESS:
				ret = GravityReturnCodes::SUCCESS;
				break;
			case ServiceDirectoryResponsePB::NOT_REGISTERED:
				ret = GravityReturnCodes::REGISTRATION_CONFLICT;
				break;
			default:
				ret = GravityReturnCodes::FAILURE;
				break;
			}
		}
		else
		{
			ret = GravityReturnCodes::LINK_ERROR;
		}
	}

    return ret;
}

GravityReturnCode GravityNode::startHeartbeat(std::string componentID, int interval_in_microseconds, unsigned short port)
{
	ZMQSemephore::init(context);

	if(interval_in_microseconds < 0)
		return gravity::GravityReturnCodes::FAILURE;

	static bool started = false;

	if(started)
		return gravity::GravityReturnCodes::FAILURE; //We shouldn't be able to start this guy twice

	this->registerDataProduct(componentID, 54541, "tcp");

	void* Heartbeat(void* thread_context); //Forward Declaration

	HBParams* params = new HBParams(); //(freed by thread)
	params->zmq_context = context;
	params->interval_in_microseconds = interval_in_microseconds;
	params->componentID = componentID;
	params->gn = this;

	pthread_t heartbeatThread;
	pthread_create(&heartbeatThread, NULL, Heartbeat, (void*)params);

	return gravity::GravityReturnCodes::SUCCESS;
}

GravityReturnCode GravityNode::registerHeartbeatListener(string componentID, uint64_t timebetweenMessages, const GravityHeartbeatListener& listener)
{
	void* HeartbeatListener(void*); //Forward declaration.
	static Heartbeat hbSub;

	ZMQSemephore::init(context);

	if(hbSocket == NULL)
	{
		//Initialize Heartbeat thread.
		hbSocket = zmq_socket(context, ZMQ_REQ);
		zmq_bind(hbSocket, "inproc://heartbeat_listener");
		HBListenerContext* thread_context = new HBListenerContext();
		thread_context->zmq_context = this->context;
		pthread_t heartbeatListenerThread;
		pthread_create(&heartbeatListenerThread, NULL, Heartbeat::HeartbeatListenerThrFunc, thread_context);
	}

	this->subscribe(componentID, hbSub);

	//Send the DataproductID
	sendStringMessage(hbSocket, componentID, ZMQ_SNDMORE);

	//Send the address of the listener
	zmq_msg_t msg1;
	zmq_msg_init_size(&msg1, sizeof(GravityHeartbeatListener*));
	intptr_t p = (intptr_t) &listener;
	memcpy(zmq_msg_data(&msg1), &p, sizeof(GravityHeartbeatListener*));
	zmq_sendmsg(hbSocket, &msg1, ZMQ_SNDMORE);
	zmq_msg_close(&msg1);

	//Send the Max time between messages
	zmq_msg_t msg;
	zmq_msg_init_size(&msg, 8);
	memcpy(zmq_msg_data(&msg), &timebetweenMessages, 8);
	zmq_sendmsg(hbSocket, &msg, 0);
	zmq_msg_close(&msg);

	return GravityReturnCodes::SUCCESS;
}

//Replace the clock_gettime for Windows.  
#ifdef WIN32
//From http://stackoverflow.com/questions/5404277/porting-clock-gettime-to-windows
LARGE_INTEGER
getFILETIMEoffset()
{
    SYSTEMTIME s;
    FILETIME f;
    LARGE_INTEGER t;

    s.wYear = 1970;
    s.wMonth = 1;
    s.wDay = 1;
    s.wHour = 0;
    s.wMinute = 0;
    s.wSecond = 0;
    s.wMilliseconds = 0;
    SystemTimeToFileTime(&s, &f);
    t.QuadPart = f.dwHighDateTime;
    t.QuadPart <<= 32;
    t.QuadPart |= f.dwLowDateTime;
    return (t);
}

//T. Ludwinski: changed timeval to timespec and microseconds to nanoseconds
int
clock_gettime(int X, struct timespec *tv)
{
    LARGE_INTEGER           t;
    FILETIME            f;
    double                  nanoseconds;
    static LARGE_INTEGER    offset;
    static double           frequencyToNanoseconds;
    static int              initialized = 0;
    static BOOL             usePerformanceCounter = 0;
    static LARGE_INTEGER 	startTime;

    if (!initialized) {
        LARGE_INTEGER performanceFrequency;
        initialized = 1;
        usePerformanceCounter = QueryPerformanceFrequency(&performanceFrequency);
        if (usePerformanceCounter) {
            QueryPerformanceCounter(&offset);
            frequencyToNanoseconds = (double)performanceFrequency.QuadPart / 1000000000.;
        } else {
            offset = getFILETIMEoffset();
            frequencyToNanoseconds = .01;
        }

        GetSystemTimeAsFileTime(&f);
        startTime.QuadPart = f.dwHighDateTime;
        startTime.QuadPart <<= 32;
        startTime.QuadPart |= f.dwLowDateTime;

        startTime.QuadPart -= 116444736000000000ULL; //Convert from Window time to UTC (100ns)
        startTime.QuadPart = startTime.QuadPart * 100; //To nanoseconds
    }
    if (usePerformanceCounter) QueryPerformanceCounter(&t);
    else {
        GetSystemTimeAsFileTime(&f);
        t.QuadPart = f.dwHighDateTime;
        t.QuadPart <<= 32;
        t.QuadPart |= f.dwLowDateTime;
    }

    t.QuadPart -= offset.QuadPart;
    nanoseconds = (double)t.QuadPart / frequencyToNanoseconds;
    nanoseconds += startTime.QuadPart;
    t.QuadPart = nanoseconds;
    tv->tv_sec = t.QuadPart / 1000000000LL;
    tv->tv_nsec = t.QuadPart % 1000000000LL;
    return (0);
}
#endif

uint64_t getCurrentTime()
{
    timespec ts;
    clock_gettime(0, &ts);
    return (uint64_t)ts.tv_sec * 1000000LL + (uint64_t)ts.tv_nsec / 1000LL;
}

#ifdef WIN32
//convert binary address to string.  
const char *inet_ntop(int af, const void * src, char* dest, int dest_length)
{
	assert(af == AF_INET); //We only support IPV4
	
	const char* new_src = (const char*)src;
	std::stringstream ss;
	ss << new_src[0] << "." << new_src[1]  << "." << new_src[2]   << "." << new_src[3]; //TODO: verify Byte Order.  
	if(dest_length < (int) ss.str().length() + 1)
		return NULL;
	
	memcpy(dest, ss.str().c_str(), ss.str().length() + 1);
	
	return dest;
}

typedef int socklen_t;
#endif

string GravityNode::getIP()
{
    string ip = "127.0.0.1";

    if (!serviceDirectoryNode.ipAddress.empty() && serviceDirectoryNode.ipAddress != "localhost")
    {
		//Reads the IP used to connect to the Service Directory.  
        int buflen = 16;
        char* buffer = (char*)malloc(buflen);

        int sock = socket(AF_INET, SOCK_DGRAM, 0);
        assert(sock != -1);

        const char* otherIP = serviceDirectoryNode.ipAddress.c_str();
        uint16_t otherPort = serviceDirectoryNode.port;

        struct sockaddr_in serv;
        memset(&serv, 0, sizeof(serv));
        serv.sin_family = AF_INET;
        serv.sin_addr.s_addr = inet_addr(otherIP);
        serv.sin_port = htons(otherPort);

        int err = connect(sock, (const sockaddr*)&serv, sizeof(serv));
        assert(err != -1);

        sockaddr_in name;
        socklen_t namelen = sizeof(name);
        err = getsockname(sock, (sockaddr*)&name, &namelen);
        assert(err != -1);

        const char* p = inet_ntop(AF_INET, &name.sin_addr, buffer, buflen);
        assert(p);

#ifdef WIN32
		closesocket(sock);
#else
        close(sock);
#endif

        ip.assign(buffer);
    }

    return ip;
}

} /* namespace gravity */
