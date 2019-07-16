
package uk.ac.ncl.cemdit.model.urbanobservatory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sensors {

    @SerializedName("sensors")
    @Expose
    private List<Sensor> sensors = null;

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

}
