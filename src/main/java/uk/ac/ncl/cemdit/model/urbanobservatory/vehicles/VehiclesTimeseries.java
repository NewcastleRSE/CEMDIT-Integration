
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesTimeseries {

    @SerializedName("timeseriesId")
    @Expose
    private String timeseriesId;
    @SerializedName("storage")
    @Expose
    private VehiclesStorage storage;
    @SerializedName("unit")
    @Expose
    private VehiclesUnit unit;
    @SerializedName("assessments")
    @Expose
    private List<Object> assessments = new ArrayList<Object>();
    @SerializedName("derivatives")
    @Expose
    private List<Object> derivatives = new ArrayList<Object>();
    @SerializedName("aggregation")
    @Expose
    private List<Object> aggregation = new ArrayList<Object>();
    @SerializedName("latest")
    @Expose
    private VehiclesLatest latest;
    @SerializedName("links")
    @Expose
    private List<VehiclesLink> links = new ArrayList<VehiclesLink>();

    public String getTimeseriesId() {
        return timeseriesId;
    }

    public void setTimeseriesId(String timeseriesId) {
        this.timeseriesId = timeseriesId;
    }

    public VehiclesStorage getStorage() {
        return storage;
    }

    public void setStorage(VehiclesStorage storage) {
        this.storage = storage;
    }

    public VehiclesUnit getUnit() {
        return unit;
    }

    public void setUnit(VehiclesUnit unit) {
        this.unit = unit;
    }

    public List<Object> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Object> assessments) {
        this.assessments = assessments;
    }

    public List<Object> getDerivatives() {
        return derivatives;
    }

    public void setDerivatives(List<Object> derivatives) {
        this.derivatives = derivatives;
    }

    public List<Object> getAggregation() {
        return aggregation;
    }

    public void setAggregation(List<Object> aggregation) {
        this.aggregation = aggregation;
    }

    public VehiclesLatest getLatest() {
        return latest;
    }

    public void setLatest(VehiclesLatest latest) {
        this.latest = latest;
    }

    public List<VehiclesLink> getLinks() {
        return links;
    }

    public void setLinks(List<VehiclesLink> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("timeseriesId", timeseriesId).append("storage", storage).append("unit", unit).append("assessments", assessments).append("derivatives", derivatives).append("aggregation", aggregation).append("latest", latest).append("links", links).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(derivatives).append(timeseriesId).append(unit).append(assessments).append(aggregation).append(links).append(storage).append(latest).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesTimeseries) == false) {
            return false;
        }
        VehiclesTimeseries rhs = ((VehiclesTimeseries) other);
        return new EqualsBuilder().append(derivatives, rhs.derivatives).append(timeseriesId, rhs.timeseriesId).append(unit, rhs.unit).append(assessments, rhs.assessments).append(aggregation, rhs.aggregation).append(links, rhs.links).append(storage, rhs.storage).append(latest, rhs.latest).isEquals();
    }

}
