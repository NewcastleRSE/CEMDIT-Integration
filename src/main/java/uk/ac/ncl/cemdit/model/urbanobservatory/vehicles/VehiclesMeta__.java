
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import org.apache.commons.lang3.builder.*;

public class VehiclesMeta__ {


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
        if ((other instanceof VehiclesMeta__) == false) {
            return false;
        }
        VehiclesMeta__ rhs = ((VehiclesMeta__) other);
        return new EqualsBuilder().isEquals();
    }

}
