
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AcquisitionDate implements Serializable
{

    @SerializedName("@value")
    @Expose
    private String value;
    @SerializedName("@type")
    @Expose
    private String type;
    private final static long serialVersionUID = -7916375171973123374L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AcquisitionDate() {
    }

    /**
     * 
     * @param value
     * @param type
     */
    public AcquisitionDate(String value, String type) {
        super();
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("value", value).append("type", type).toString();
    }

}
