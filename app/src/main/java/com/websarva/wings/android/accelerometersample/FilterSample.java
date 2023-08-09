package com.websarva.wings.android.accelerometersample;

public class FilterSample {
    private float[] oldDatas = null;
    private float alpha = 0.2F;
    private float[] LowDatas;
    private float[] HighDatas;

    public FilterSample() {
        this.oldDatas = null;
    }

    public FilterSample(float[] oldDatas) {
        this.oldDatas = oldDatas;
    }

    public void setAlpha(float alpha) {
        if(alpha > 0 && alpha < 1){
            this.alpha = alpha;
        }
    }

    public float[] getLowDatas() {
        return LowDatas;
    }

    public float[] getHighDatas() {
        return HighDatas;
    }

    private boolean setLowDatas(float[] newDatas){
        if(oldDatas == null) return false;

        LowDatas = new float[newDatas.length];
        if(oldDatas.length == newDatas.length) {
            for (int i = 0; i < LowDatas.length; i++) {
                LowDatas[i] = newDatas[i] * alpha + oldDatas[i] * (1F - alpha);
            }
        }
        return true;
    }

    private boolean setHighDatas(float[] newDatas){
        if(oldDatas == null) return false;
        if(!setLowDatas(newDatas)) return false;

        HighDatas = new float[newDatas.length];
        for(int i = 0 ;i < HighDatas.length; i++){
            HighDatas[i] = newDatas[i] - LowDatas[i];
        }
        return true;
    }
    public boolean setFilter(float[] newDatas){
        return setHighDatas(newDatas);
    }
}
