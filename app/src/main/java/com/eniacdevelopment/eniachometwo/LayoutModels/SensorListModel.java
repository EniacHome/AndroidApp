package com.eniacdevelopment.eniachometwo.LayoutModels;

/**
 * Created by denni on 1/6/2017.
 */

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;
import com.eniacdevelopment.EniacHome.DataModel.Sensor.SensorType;

import java.util.ArrayList;
import java.util.List;

public class SensorListModel {

    public String sensorType;
    public List<Sensor> sensors = new ArrayList<Sensor>();

    public SensorListModel(String sensorType) {
        this.sensorType = sensorType;
    }

}