
package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Var implements Serializable
{

    @SerializedName("camera")
    @Expose
    private List<Camera> camera = null;
    @SerializedName("camera_id")
    @Expose
    private List<CameraId> cameraId = null;
    @SerializedName("camera_name")
    @Expose
    private List<CameraName> cameraName = null;
    @SerializedName("lens_prop")
    @Expose
    private List<LensProp> lensProp = null;
    @SerializedName("matrix_prop")
    @Expose
    private List<MatrixProp> matrixProp = null;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("install_time")
    @Expose
    private List<InstallTime> installTime = null;
    @SerializedName("install_height")
    @Expose
    private List<InstallHeight> installHeight = null;
    @SerializedName("orientation")
    @Expose
    private List<Orientation> orientation = null;
    @SerializedName("image")
    @Expose
    private List<Image_> image = null;
    @SerializedName("image_filename")
    @Expose
    private List<ImageFilename> imageFilename = null;
    @SerializedName("view")
    @Expose
    private List<View> view = null;
    @SerializedName("x")
    @Expose
    private List<X> x = null;
    @SerializedName("y")
    @Expose
    private List<Y> y = null;
    @SerializedName("acquisition")
    @Expose
    private List<Acquisition> acquisition = null;
    @SerializedName("acquisition_date")
    @Expose
    private List<AcquisitionDate> acquisitionDate = null;
    @SerializedName("resolution")
    @Expose
    private List<Resolution> resolution = null;
    @SerializedName("colour_depth")
    @Expose
    private List<ColourDepth> colourDepth = null;
    @SerializedName("compression_rate")
    @Expose
    private List<CompressionRate> compressionRate = null;
    private final static long serialVersionUID = 815280449221354411L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Var() {
    }

    /**
     * 
     * @param compressionRate
     * @param matrixProp
     * @param orientation
     * @param location
     * @param camera
     * @param image
     * @param cameraId
     * @param resolution
     * @param acquisition
     * @param imageFilename
     * @param cameraName
     * @param lensProp
     * @param acquisitionDate
     * @param installTime
     * @param view
     * @param colourDepth
     * @param installHeight
     * @param y
     * @param x
     */
    public Var(List<Camera> camera, List<CameraId> cameraId, List<CameraName> cameraName, List<LensProp> lensProp, List<MatrixProp> matrixProp, List<Location> location, List<InstallTime> installTime, List<InstallHeight> installHeight, List<Orientation> orientation, List<Image_> image, List<ImageFilename> imageFilename, List<View> view, List<X> x, List<Y> y, List<Acquisition> acquisition, List<AcquisitionDate> acquisitionDate, List<Resolution> resolution, List<ColourDepth> colourDepth, List<CompressionRate> compressionRate) {
        super();
        this.camera = camera;
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.lensProp = lensProp;
        this.matrixProp = matrixProp;
        this.location = location;
        this.installTime = installTime;
        this.installHeight = installHeight;
        this.orientation = orientation;
        this.image = image;
        this.imageFilename = imageFilename;
        this.view = view;
        this.x = x;
        this.y = y;
        this.acquisition = acquisition;
        this.acquisitionDate = acquisitionDate;
        this.resolution = resolution;
        this.colourDepth = colourDepth;
        this.compressionRate = compressionRate;
    }

    public List<Camera> getCamera() {
        return camera;
    }

    public void setCamera(List<Camera> camera) {
        this.camera = camera;
    }

    public List<CameraId> getCameraId() {
        return cameraId;
    }

    public void setCameraId(List<CameraId> cameraId) {
        this.cameraId = cameraId;
    }

    public List<CameraName> getCameraName() {
        return cameraName;
    }

    public void setCameraName(List<CameraName> cameraName) {
        this.cameraName = cameraName;
    }

    public List<LensProp> getLensProp() {
        return lensProp;
    }

    public void setLensProp(List<LensProp> lensProp) {
        this.lensProp = lensProp;
    }

    public List<MatrixProp> getMatrixProp() {
        return matrixProp;
    }

    public void setMatrixProp(List<MatrixProp> matrixProp) {
        this.matrixProp = matrixProp;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<InstallTime> getInstallTime() {
        return installTime;
    }

    public void setInstallTime(List<InstallTime> installTime) {
        this.installTime = installTime;
    }

    public List<InstallHeight> getInstallHeight() {
        return installHeight;
    }

    public void setInstallHeight(List<InstallHeight> installHeight) {
        this.installHeight = installHeight;
    }

    public List<Orientation> getOrientation() {
        return orientation;
    }

    public void setOrientation(List<Orientation> orientation) {
        this.orientation = orientation;
    }

    public List<Image_> getImage() {
        return image;
    }

    public void setImage(List<Image_> image) {
        this.image = image;
    }

    public List<ImageFilename> getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(List<ImageFilename> imageFilename) {
        this.imageFilename = imageFilename;
    }

    public List<View> getView() {
        return view;
    }

    public void setView(List<View> view) {
        this.view = view;
    }

    public List<X> getX() {
        return x;
    }

    public void setX(List<X> x) {
        this.x = x;
    }

    public List<Y> getY() {
        return y;
    }

    public void setY(List<Y> y) {
        this.y = y;
    }

    public List<Acquisition> getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(List<Acquisition> acquisition) {
        this.acquisition = acquisition;
    }

    public List<AcquisitionDate> getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(List<AcquisitionDate> acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public List<Resolution> getResolution() {
        return resolution;
    }

    public void setResolution(List<Resolution> resolution) {
        this.resolution = resolution;
    }

    public List<ColourDepth> getColourDepth() {
        return colourDepth;
    }

    public void setColourDepth(List<ColourDepth> colourDepth) {
        this.colourDepth = colourDepth;
    }

    public List<CompressionRate> getCompressionRate() {
        return compressionRate;
    }

    public void setCompressionRate(List<CompressionRate> compressionRate) {
        this.compressionRate = compressionRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("camera", camera).append("cameraId", cameraId).append("cameraName", cameraName).append("lensProp", lensProp).append("matrixProp", matrixProp).append("location", location).append("installTime", installTime).append("installHeight", installHeight).append("orientation", orientation).append("image", image).append("imageFilename", imageFilename).append("view", view).append("x", x).append("y", y).append("acquisition", acquisition).append("acquisitionDate", acquisitionDate).append("resolution", resolution).append("colourDepth", colourDepth).append("compressionRate", compressionRate).toString();
    }

}
