
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Image implements Serializable
{

    @SerializedName("var")
    @Expose
    private Var var;
    @SerializedName("context")
    @Expose
    private Context context;
    private final static long serialVersionUID = 7465547528870848368L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image() {
    }

    /**
     * 
     * @param var
     * @param context
     */
    public Image(Var var, Context context) {
        super();
        this.var = var;
        this.context = context;
    }

    public Var getVar() {
        return var;
    }

    public void setVar(Var var) {
        this.var = var;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("var", var).append("context", context).toString();
    }

}
