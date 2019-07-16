
package uk.ac.ncl.cemdit.model.urbanobservatory.vehicles;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class VehiclesItem {

    @SerializedName("entityId")
    @Expose
    private String entityId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta")
    @Expose
    private VehiclesMeta meta;
    @SerializedName("position")
    @Expose
    private List<Object> position = new ArrayList<Object>();
    @SerializedName("feed")
    @Expose
    private List<VehiclesFeed> feed = new ArrayList<VehiclesFeed>();
    @SerializedName("links")
    @Expose
    private List<VehiclesLink__> links = new ArrayList<VehiclesLink__>();

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VehiclesMeta getMeta() {
        return meta;
    }

    public void setMeta(VehiclesMeta meta) {
        this.meta = meta;
    }

    public List<Object> getPosition() {
        return position;
    }

    public void setPosition(List<Object> position) {
        this.position = position;
    }

    public List<VehiclesFeed> getFeed() {
        return feed;
    }

    public void setFeed(List<VehiclesFeed> feed) {
        this.feed = feed;
    }

    public List<VehiclesLink__> getLinks() {
        return links;
    }

    public void setLinks(List<VehiclesLink__> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("entityId", entityId).append("name", name).append("meta", meta).append("position", position).append("feed", feed).append("links", links).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(feed).append(meta).append(name).append(entityId).append(links).append(position).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof VehiclesItem) == false) {
            return false;
        }
        VehiclesItem rhs = ((VehiclesItem) other);
        return new EqualsBuilder().append(feed, rhs.feed).append(meta, rhs.meta).append(name, rhs.name).append(entityId, rhs.entityId).append(links, rhs.links).append(position, rhs.position).isEquals();
    }

}
