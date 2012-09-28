/**
 * 
 */
package com.aphysci.gravity;

import com.aphysci.gravity.protobuf.GravityDataProductContainer.GravityDataProductPB;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author mbarger
 *
 */
public class GravityDataProduct {
	
	private GravityDataProductPB.Builder gdp = GravityDataProductPB.newBuilder();

    /**
     * Constructor
     * @param dataProductID string descriptor for this data product. Name by which subscribers will configure subscriptions
     */
    public GravityDataProduct(String dataProductID) {
    	gdp.setDataProductID(dataProductID);
    }

    /**
     * Constructor that deserializes this GravityDataProduct from array of bytes
     * @param arrayPtr pointer to array of bytes containing serialized GravityDataProduct
     * @param size size of serialized data
     */
    public GravityDataProduct(byte[] data) {
    	try {
			gdp.mergeFrom(data);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Method to return the timestamp associated with this data product
     * @return timestamp for data
     */
    public long getGravityTimestamp() {
    	return gdp.getTimestamp();
    }

    /**
     * Method to return the data product ID for this data
     * @return data product ID
     */
    public String getDataProductID() {
    	return gdp.getDataProductID();
    }

    /**
     * Setter for the specification of software version information
     * @param softwareVersion string specifying the version number
     */
    public void setSoftwareVersion(String softwareVersion) {
    	gdp.setSoftwareVersion(softwareVersion);
    }

    /**
     * Getter for the software version specified on this data product
     * @return the software version number associated with this data product
     */
    public String getSoftwareVersion() {
    	return gdp.getSoftwareVersion();
    }

    /**
     * Set the application-specific data for this data product
     * @param data pointer to arbitrary data
     * @param size length of data
     */
    public void setData(byte[] data) {
    	gdp.setData(ByteString.copyFrom(data));
    }

    /**
     * Set the application-specific data for this data product
     * @param data A Google Protocol Buffer Message object containing the data
     */
    public void setData(GeneratedMessage data) {
    	gdp.setData(data.toByteString());
    }

    /**
     * Getter for the application-specific data contained within this data product
     * @return byte array containing data, or null if no data is available. 
     */
    public byte[] getData() {
    	if (gdp.getData() != null) {
    		return gdp.getData().toByteArray();
    	}
    	return null;
    }

    /**
     * Get the size of the data contained within this data product
     * @return size in bytes of contained data
     */
    public int getDataSize() {
    	if (gdp.getData() != null) {
    		return gdp.getData().size();
    	}
    	return 0;
    }

    /**
     * Populate a protobuf object with the data contained in this data product
     * @param data Google Protocol Buffer Message object to populate
     * @return success flag
     */
    public GeneratedMessage populateMessage(GeneratedMessage data) {
    	// TODO: fill in this stub
    	return null;
    }

    /**
     * Get the size for this message
     * @return size in bytes for this data product
     */
    public int getSize() {
    	return gdp.build().toByteArray().length;
    }

    /**
     * Deserialize this GravityDataProduct from array of bytes
     * @param arrayPtr pointer to array of bytes containing serialized GravityDataProduct
     * @param size size of serialized data
     */
    public void parseFromArray(byte[] data) {
    	try {
			gdp.mergeFrom(data);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Serialize this GravityDataProduct
     * \arrayPtr pointer to array into which to serialize this GravityDataProduct
     * @return success flag
     */
    public byte[] serializeToArray() {
    	return gdp.build().toByteArray();
    }

}