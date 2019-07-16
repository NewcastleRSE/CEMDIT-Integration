
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesMeta {

    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("lookingAt")
    @Expose
    private List<String> lookingAt = new ArrayList<String>();

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<String> getLookingAt() {
        return lookingAt;
    }

    public void setLookingAt(List<String> lookingAt) {
        this.lookingAt = lookingAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("area", area).append("lookingAt", lookingAt).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(area).append(lookingAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesMeta) == false) {
            return false;
        }
        VehiclesMeta rhs = ((VehiclesMeta) other);
        return new EqualsBuilder().append(area, rhs.area).append(lookingAt, rhs.lookingAt).isEquals();
    }

}
