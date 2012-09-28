#include <GravityNode.h>
#include <sstream>
#include <iostream>

using namespace std;
#ifdef WIN32
#define WIN32_LEAN_AND_MEAN //Smaller include
#include <windows.h> //For Sleep
#define sleep(x) Sleep((x)*1000)
#endif

int main(int argc, char** argv)
{
  using namespace gravity;
  const char* myDataProductID = "dummydataproduct";

  if(argc > 1)
    myDataProductID = argv[1];

  GravityNode gn;
  gn.init();
  gn.registerDataProduct(myDataProductID, 54321, "tcp");

  GravityDataProduct dataProduct(myDataProductID);
  int count = 0;

  while(true)
  {
    stringstream ss("");
    ss << count;
    string cnt_str = ss.str();

    //cout << cnt_str.length() << endl;
    cout << cnt_str << endl;
    
    dataProduct.setData((void*)cnt_str.c_str(), cnt_str.length());

    gn.publish(dataProduct);

    sleep(1);

    count++;
    count = count % 20;
  }

  gn.unregisterDataProduct(myDataProductID);

}