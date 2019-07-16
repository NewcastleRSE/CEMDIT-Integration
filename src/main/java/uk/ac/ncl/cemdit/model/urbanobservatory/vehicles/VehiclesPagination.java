
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesPagination {

    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("pageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("pageNumber", pageNumber).append("pageSize", pageSize).append("pageCount", pageCount).append("total", total).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pageSize).append(pageCount).append(total).append(pageNumber).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesPagination) == false) {
            return false;
        }
        VehiclesPagination rhs = ((VehiclesPagination) other);
        return new EqualsBuilder().append(pageSize, rhs.pageSize).append(pageCount, rhs.pageCount).append(total, rhs.total).append(pageNumber, rhs.pageNumber).isEquals();
    }

}
