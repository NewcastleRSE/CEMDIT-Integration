
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesBroker {

    @SerializedName("brokerId")
    @Expose
    private String brokerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("meta")
    @Expose
    private Object meta;

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("brokerId", brokerId).append("name", name).append("active", active).append("meta", meta).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(active).append(brokerId).append(meta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesBroker) == false) {
            return false;
        }
        VehiclesBroker rhs = ((VehiclesBroker) other);
        return new EqualsBuilder().append(name, rhs.name).append(active, rhs.active).append(brokerId, rhs.brokerId).append(meta, rhs.meta).isEquals();
    }

}
