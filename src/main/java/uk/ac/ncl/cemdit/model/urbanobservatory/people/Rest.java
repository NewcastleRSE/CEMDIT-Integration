package uk.ac.ncl.cemdit.model.urbanobservatory.people;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "latest",
        "base_height",
        "type",
        "geom",
        "source",
        "active",
        "name",
        "sensor_height"
})
public class Rest {

    @JsonProperty("latest")
    private String latest;
    @JsonProperty("base_height")
    private Integer baseHeight;
    @JsonProperty("type")
    private String type;
    @JsonProperty("geom")
    private Geom geom;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("active")
    private String active;
    @JsonProperty("name")
    private String name;
    @JsonProperty("sensor_height")
    private Integer sensorHeight;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("latest")
    public String getLatest() {
        return latest;
    }

    @JsonProperty("latest")
    public void setLatest(String latest) {
        this.latest = latest;
    }

    @JsonProperty("base_height")
    public Integer getBaseHeight() {
        return baseHeight;
    }

    @JsonProperty("base_height")
    public void setBaseHeight(Integer baseHeight) {
        this.baseHeight = baseHeight;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("geom")
    public Geom getGeom() {
        return geom;
    }

    @JsonProperty("geom")
    public void setGeom(Geom geom) {
        this.geom = geom;
    }

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("sensor_height")
    public Integer getSensorHeight() {
        return sensorHeight;
    }

    @JsonProperty("sensor_height")
    public void setSensorHeight(Integer sensorHeight) {
        this.sensorHeight = sensorHeight;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}