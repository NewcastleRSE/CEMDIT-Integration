
package uk.ac.ncl.cemdit.model.integration.lookupDB;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LookupDB {

    @SerializedName("DBEntry")
    @Expose
    private List<DBEntry> collection = null;

    public List<DBEntry> getCollection() {
        return collection;
    }

    public void setCollection(List<DBEntry> collection) {
        this.collection = collection;
    }

}