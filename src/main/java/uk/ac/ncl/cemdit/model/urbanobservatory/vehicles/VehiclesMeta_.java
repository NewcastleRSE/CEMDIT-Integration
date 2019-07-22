
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import org.apache.commons.lang3.builder.*;

public class VehiclesMeta_ {


    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesMeta_) == false) {
            return false;
        }
        VehiclesMeta_ rhs = ((VehiclesMeta_) other);
        return new EqualsBuilder().isEquals();
    }

}
