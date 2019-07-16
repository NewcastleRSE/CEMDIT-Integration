
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesSchema {

    @SerializedName("pagination")
    @Expose
    private VehiclesPagination pagination;
    @SerializedName("items")
    @Expose
    private List<VehiclesItem> items = new ArrayList<VehiclesItem>();

    public VehiclesPagination getPagination() {
        return pagination;
    }

    public void setPagination(VehiclesPagination pagination) {
        this.pagination = pagination;
    }

    public List<VehiclesItem> getItems() {
        return items;
    }

    public void setItems(List<VehiclesItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("pagination", pagination).append("items", items).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(items).append(pagination).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesSchema) == false) {
            return false;
        }
        VehiclesSchema rhs = ((VehiclesSchema) other);
        return new EqualsBuilder().append(items, rhs.items).append(pagination, rhs.pagination).isEquals();
    }

}
