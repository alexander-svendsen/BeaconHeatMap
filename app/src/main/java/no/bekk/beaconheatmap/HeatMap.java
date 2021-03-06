package no.bekk.beaconheatmap;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

@DynamoDBTable(tableName = Constants.HEAT_TABLE_NAME)
public class HeatMap {
    private String beaconId;
    private String dist;
    private String timestamp;
    private String id;
    private String dummy = "ok";

    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName="_Id")
    public String getId() { return id;}
    public void setId(String id) {this.id = id;}

    @DynamoDBHashKey(attributeName = "beaconId")
    public String getBeaconId() {
        return beaconId;
    }
    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    @DynamoDBAttribute(attributeName = "dist")
    public String getDist() {
        return dist;
    }
    public void setDist(String dist) {
        this.dist = dist;
    }

    @DynamoDBAttribute(attributeName = "dummy")
    public String getDummy() {
        return dummy;
    }
    public void setDummy(String dummy) {
        this.dummy = "ok";
    }


    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
