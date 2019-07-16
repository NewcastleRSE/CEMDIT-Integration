
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Image_ implements Serializable
{

    @SerializedName("@id")
    @Expose
    private String id;
    private final static long serialVersionUID = 5695038692567353721L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image_() {
    }

    /**
     * 
     * @param id
     */
    public Image_(String id) {
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
