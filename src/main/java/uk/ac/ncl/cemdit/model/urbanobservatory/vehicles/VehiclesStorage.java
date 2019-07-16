
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesStorage {

    @SerializedName("storageId")
    @Expose
    private Integer storageId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("suffix")
    @Expose
    private Object suffix;

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSuffix() {
        return suffix;
    }

    public void setSuffix(Object suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("storageId", storageId).append("name", name).append("suffix", suffix).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(suffix).append(storageId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesStorage) == false) {
            return false;
        }
        VehiclesStorage rhs = ((VehiclesStorage) other);
        return new EqualsBuilder().append(name, rhs.name).append(suffix, rhs.suffix).append(storageId, rhs.storageId).isEquals();
    }

}
