
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesUnit {

    @SerializedName("unitId")
    @Expose
    private String unitId;
    @SerializedName("name")
    @Expose
    private String name;

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("unitId", unitId).append("name", name).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(unitId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesUnit) == false) {
            return false;
        }
        VehiclesUnit rhs = ((VehiclesUnit) other);
        return new EqualsBuilder().append(name, rhs.name).append(unitId, rhs.unitId).isEquals();
    }

}
