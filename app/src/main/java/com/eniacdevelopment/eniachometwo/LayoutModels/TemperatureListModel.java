package com.eniacdevelopment.eniachometwo.LayoutModels;

import com.eniacdevelopment.EniacHome.DataModel.Sensor.Sensor;

/**
 * Created by denni on 1/17/2017.
 */

public class TemperatureListModel {
    private Sensor sensor;

    public TemperatureListModel(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getSensorName() {
        return sensor.Name;
    }

    public double getTemperature() {
        return sensor.SensorStatus.Value;
    }
}
