package uk.ac.ncl.cemdit.model.usecasepeoplecounter;

import uk.ac.ncl.cemdit.model.urbanobservatory.Sensor;

public class Mapper {

    public Mapper(Sensor sensor) {
        Camera camera = new Camera();
        camera.setId(sensor.getBrokerName());

        CameraId cameraId = new CameraId();
        cameraId.setValue(sensor.getBrokerName());
        cameraId.setType("String");

        CameraName cameraName = new CameraName();
        cameraName.setType("String");
        cameraName.setValue(sensor.getSensorName());


    }
}
