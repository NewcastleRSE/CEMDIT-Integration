
package uk.ac.ncl.cemdit.model.urbanobservatory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** Class describing an Urban Observatory sensor. In this
 * case the sensor is a camera
 */
public class Sensor {

    @SerializedName("Sensor Name")
    @Expose
    private String sensorName;
    @SerializedName("Location (WKT)")
    @Expose
    private String locationWKT;
    @SerializedName("Sensor Centroid Latitude")
    @Expose
    private Double sensorCentroidLatitude;
    @SerializedName("Sensor Height Above Ground")
    @Expose
    private Integer sensorHeightAboveGround;
    @SerializedName("Broker Name")
    @Expose
    private String brokerName;
    @SerializedName("Ground Height Above Sea Level")
    @Expose
    private Double groundHeightAboveSeaLevel;
    @SerializedName("Sensor Centroid Longitude")
    @Expose
    private Double sensorCentroidLongitude;
    @SerializedName("Third Party")
    @Expose
    private Boolean thirdParty;

    public String getLocationWKT() {
        return locationWKT;
    }

    public void setLocationWKT(String locationWKT) {
        this.locationWKT = locationWKT;
    }

    public Double getSensorCentroidLatitude() {
        return sensorCentroidLatitude;
    }

    public void setSensorCentroidLatitude(Double sensorCentroidLatitude) {
        this.sensorCentroidLatitude = sensorCentroidLatitude;
    }

    public Integer getSensorHeightAboveGround() {
        return sensorHeightAboveGround;
    }

    public void setSensorHeightAboveGround(Integer sensorHeightAboveGround) {
        this.sensorHeightAboveGround = sensorHeightAboveGround;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public Double getGroundHeightAboveSeaLevel() {
        return groundHeightAboveSeaLevel;
    }

    public void setGroundHeightAboveSeaLevel(Double groundHeightAboveSeaLevel) {
        this.groundHeightAboveSeaLevel = groundHeightAboveSeaLevel;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Double getSensorCentroidLongitude() {
        return sensorCentroidLongitude;
    }

    public void setSensorCentroidLongitude(Double sensorCentroidLongitude) {
        this.sensorCentroidLongitude = sensorCentroidLongitude;
    }

    public Boolean getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(Boolean thirdParty) {
        this.thirdParty = thirdParty;
    }

}
