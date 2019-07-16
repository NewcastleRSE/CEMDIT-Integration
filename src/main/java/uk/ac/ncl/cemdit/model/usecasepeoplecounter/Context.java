
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Context implements Serializable
{

    @SerializedName("ex")
    @Expose
    private String ex;
    @SerializedName("uo")
    @Expose
    private String uo;
    private final static long serialVersionUID = 999868204984715926L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Context() {
    }

    /**
     * 
     * @param ex
     * @param uo
     */
    public Context(String ex, String uo) {
        super();
        this.ex = ex;
        this.uo = uo;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getUo() {
        return uo;
    }

    public void setUo(String uo) {
        this.uo = uo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ex", ex).append("uo", uo).toString();
    }

}
