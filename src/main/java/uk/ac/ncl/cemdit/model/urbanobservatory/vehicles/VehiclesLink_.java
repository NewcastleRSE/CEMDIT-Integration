
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesLink_ {

    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("rel")
    @Expose
    private String rel;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("href", href).append("rel", rel).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(rel).append(href).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesLink_) == false) {
            return false;
        }
        VehiclesLink_ rhs = ((VehiclesLink_) other);
        return new EqualsBuilder().append(rel, rhs.rel).append(href, rhs.href).isEquals();
    }

}
