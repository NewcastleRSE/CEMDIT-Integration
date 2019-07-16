
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesBrokerage {

    @SerializedName("brokerageId")
    @Expose
    private String brokerageId;
    @SerializedName("sourceId")
    @Expose
    private String sourceId;
    @SerializedName("meta")
    @Expose
    private VehiclesMeta__ meta;
    @SerializedName("broker")
    @Expose
    private VehiclesBroker broker;

    public String getBrokerageId() {
        return brokerageId;
    }

    public void setBrokerageId(String brokerageId) {
        this.brokerageId = brokerageId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public VehiclesMeta__ getMeta() {
        return meta;
    }

    public void setMeta(VehiclesMeta__ meta) {
        this.meta = meta;
    }

    public VehiclesBroker getBroker() {
        return broker;
    }

    public void setBroker(VehiclesBroker broker) {
        this.broker = broker;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("brokerageId", brokerageId).append("sourceId", sourceId).append("meta", meta).append("broker", broker).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(sourceId).append(broker).append(brokerageId).append(meta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesBrokerage) == false) {
            return false;
        }
        VehiclesBrokerage rhs = ((VehiclesBrokerage) other);
        return new EqualsBuilder().append(sourceId, rhs.sourceId).append(broker, rhs.broker).append(brokerageId, rhs.brokerageId).append(meta, rhs.meta).isEquals();
    }

}
