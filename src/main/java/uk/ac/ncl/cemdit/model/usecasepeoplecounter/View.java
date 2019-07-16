
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class View implements Serializable
{

    @SerializedName("@id")
    @Expose
    private String id;
    private final static long serialVersionUID = 8020198040685827649L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public View() {
    }

    /**
     * 
     * @param id
     */
    public View(String id) {
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
