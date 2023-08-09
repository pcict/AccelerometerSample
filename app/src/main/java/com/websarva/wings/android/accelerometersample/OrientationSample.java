package com.websarva.wings.android.accelerometersample;

import android.hardware.SensorManager;

import java.io.Serializable;

public class OrientationSample implements Serializable {
    //弧度法による変換用
    private final float RAD_TO_DEC = 180 / (float)Math.PI;
    private float[] accelerometer = null;//加速度を保持
    private float[] magneticFields = null;//磁界を保持

    public OrientationSample() {
    }

    public void setAccelerometer(float[] accelerometer) {
        this.accelerometer = accelerometer;
    }

    public void setMagneticFields(float[] magneticFields) {
        this.magneticFields = magneticFields;
    }

    public boolean isCheck(){
        return !(accelerometer == null || magneticFields == null);
        //ド・モルガンの法則
        //return accelerometer != null && magneticFields != null;
    }

    private void clear(){
        accelerometer = null;
        magneticFields = null;
    }

    public  float[] getCalcOrientation(){
        //行列の宣言
        float[] inR = new float[16];
        float[] outR = new float[16];

        SensorManager.getRotationMatrix(inR,null,accelerometer,magneticFields);

        SensorManager.remapCoordinateSystem(inR,SensorManager.AXIS_X,SensorManager.AXIS_Z,outR);

        float[] orientation = new float[3];
        SensorManager.getOrientation(outR, orientation);

        orientation[0] *= RAD_TO_DEC;
        orientation[1] *= RAD_TO_DEC;
        orientation[2] *= RAD_TO_DEC;

        clear();
        return orientation;
    }
}
