
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Acquisition implements Serializable
{

    @SerializedName("@id")
    @Expose
    private String id;
    private final static long serialVersionUID = 74327011273881888L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Acquisition() {
    }

    /**
     * 
     * @param id
     */
    public Acquisition(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).toString();
    }

}
