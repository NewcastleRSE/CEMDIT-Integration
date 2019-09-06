package uk.ac.ncl.cemdit.model.integration.lookupDB;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProvQueryTypes {

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
