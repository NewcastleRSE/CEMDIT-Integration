
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesFeed {

    @SerializedName("feedId")
    @Expose
    private String feedId;
    @SerializedName("metric")
    @Expose
    private String metric;
    @SerializedName("hardwareId")
    @Expose
    private Object hardwareId;
    @SerializedName("meta")
    @Expose
    private VehiclesMeta_ meta;
    @SerializedName("provider")
    @Expose
    private Object provider;
    @SerializedName("service")
    @Expose
    private List<Object> service = new ArrayList<Object>();
    @SerializedName("technology")
    @Expose
    private Object technology;
    @SerializedName("hardware")
    @Expose
    private Object hardware;
    @SerializedName("timeseries")
    @Expose
    private List<VehiclesTimeseries> timeseries = new ArrayList<VehiclesTimeseries>();
    @SerializedName("brokerage")
    @Expose
    private List<VehiclesBrokerage> brokerage = new ArrayList<VehiclesBrokerage>();
    @SerializedName("isRestricted")
    @Expose
    private Boolean isRestricted;
    @SerializedName("links")
    @Expose
    private List<VehiclesLink_> links = new ArrayList<VehiclesLink_>();

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Object getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(Object hardwareId) {
        this.hardwareId = hardwareId;
    }

    public VehiclesMeta_ getMeta() {
        return meta;
    }

    public void setMeta(VehiclesMeta_ meta) {
        this.meta = meta;
    }

    public Object getProvider() {
        return provider;
    }

    public void setProvider(Object provider) {
        this.provider = provider;
    }

    public List<Object> getService() {
        return service;
    }

    public void setService(List<Object> service) {
        this.service = service;
    }

    public Object getTechnology() {
        return technology;
    }

    public void setTechnology(Object technology) {
        this.technology = technology;
    }

    public Object getHardware() {
        return hardware;
    }

    public void setHardware(Object hardware) {
        this.hardware = hardware;
    }

    public List<VehiclesTimeseries> getTimeseries() {
        return timeseries;
    }

    public void setTimeseries(List<VehiclesTimeseries> timeseries) {
        this.timeseries = timeseries;
    }

    public List<VehiclesBrokerage> getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(List<VehiclesBrokerage> brokerage) {
        this.brokerage = brokerage;
    }

    public Boolean getIsRestricted() {
        return isRestricted;
    }

    public void setIsRestricted(Boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

    public List<VehiclesLink_> getLinks() {
        return links;
    }

    public void setLinks(List<VehiclesLink_> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("feedId", feedId).append("metric", metric).append("hardwareId", hardwareId).append("meta", meta).append("provider", provider).append("service", service).append("technology", technology).append("hardware", hardware).append("timeseries", timeseries).append("brokerage", brokerage).append("isRestricted", isRestricted).append("links", links).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(brokerage).append(technology).append(timeseries).append(feedId).append(metric).append(hardwareId).append(provider).append(meta).append(service).append(links).append(isRestricted).append(hardware).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesFeed) == false) {
            return false;
        }
        VehiclesFeed rhs = ((VehiclesFeed) other);
        return new EqualsBuilder().append(brokerage, rhs.brokerage).append(technology, rhs.technology).append(timeseries, rhs.timeseries).append(feedId, rhs.feedId).append(metric, rhs.metric).append(hardwareId, rhs.hardwareId).append(provider, rhs.provider).append(meta, rhs.meta).append(service, rhs.service).append(links, rhs.links).append(isRestricted, rhs.isRestricted).append(hardware, rhs.hardware).isEquals();
    }

}
