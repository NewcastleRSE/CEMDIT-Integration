package uk.ac.ncl.cemdit.model.urbanobservatory.traffic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("Location (WKT)")
    @Expose
    private String locationWKT;

}
