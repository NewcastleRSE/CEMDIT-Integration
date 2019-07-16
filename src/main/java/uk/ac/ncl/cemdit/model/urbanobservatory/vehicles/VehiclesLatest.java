
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesLatest {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("duration")
    @Expose
    private Double duration;
    @SerializedName("value")
    @Expose
    private String value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("time", time).append("duration", duration).append("value", value).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(duration).append(time).append(value).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesLatest) == false) {
            return false;
        }
        VehiclesLatest rhs = ((VehiclesLatest) other);
        return new EqualsBuilder().append(duration, rhs.duration).append(time, rhs.time).append(value, rhs.value).isEquals();
    }

}
