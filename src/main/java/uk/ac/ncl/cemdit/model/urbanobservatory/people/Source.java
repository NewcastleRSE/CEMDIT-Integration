package uk.ac.ncl.cemdit.model.urbanobservatory.people;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "document",
        "db_name",
        "web_display_name",
        "third_party",
        "fancy_name"
})
public class Source {

    @JsonProperty("document")
    private Object document;
    @JsonProperty("db_name")
    private String dbName;
    @JsonProperty("web_display_name")
    private String webDisplayName;
    @JsonProperty("third_party")
    private Object thirdParty;
    @JsonProperty("fancy_name")
    private String fancyName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("document")
    public Object getDocument() {
        return document;
    }

    @JsonProperty("document")
    public void setDocument(Object document) {
        this.document = document;
    }

    @JsonProperty("db_name")
    public String getDbName() {
        return dbName;
    }

    @JsonProperty("db_name")
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @JsonProperty("web_display_name")
    public String getWebDisplayName() {
        return webDisplayName;
    }

    @JsonProperty("web_display_name")
    public void setWebDisplayName(String webDisplayName) {
        this.webDisplayName = webDisplayName;
    }

    @JsonProperty("third_party")
    public Object getThirdParty() {
        return thirdParty;
    }

    @JsonProperty("third_party")
    public void setThirdParty(Object thirdParty) {
        this.thirdParty = thirdParty;
    }

    @JsonProperty("fancy_name")
    public String getFancyName() {
        return fancyName;
    }

    @JsonProperty("fancy_name")
    public void setFancyName(String fancyName) {
        this.fancyName = fancyName;
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