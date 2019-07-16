
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Camera implements Serializable
{

    @SerializedName("@id")
    @Expose
    private String id;
    private final static long serialVersionUID = -1188376066845699930L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Camera() {
    }

    /**
     * 
     * @param id
     */
    public Camera(String id) {
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
