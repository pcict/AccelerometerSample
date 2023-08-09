package com.websarva.wings.android.accelerometersample;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //リスナーを保持するためのフィールド
    private SensorEventListener mListener;
    //センサーマネージャーをフィールドで管理する
    private SensorManager sensorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListener = new SensorListener();
        //センサーマネージャーの取得
        sensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //ボタンの取得
        Button btRun = findViewById(R.id.btRun);
        btRun.setOnClickListener(new ButtonClickListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //後片付け
        sensorMgr.unregisterListener(mListener);
    }

    //センサー用のイベントリスナークラス
    private class SensorListener implements SensorEventListener {
        private OrientationSample orientationSample = new OrientationSample();
        private ImageView ivDroid = findViewById(R.id.ivDoroid);
        private FilterSample accFilter = new FilterSample();
        private FilterSample magFilter = new FilterSample();
        /**
         * センサーで取得した値が変化したとき発生する
         *
         * @param event センサー情報
         */
        @Override
        public void onSensorChanged(SensorEvent event) {

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    //加速度の取得
                    if(accFilter.setFilter(event.values)){
                        orientationSample.setAccelerometer(accFilter.getLowDatas());
                        //出力部品の取得
                        TextView[] tvs = {findViewById(R.id.tvX),
                                findViewById(R.id.tvY),
                                findViewById(R.id.tvZ)};
                        //値の設定
                        int i = 0;
                        for (TextView tv : tvs) {
                            tv.setText(String.valueOf(accFilter.getLowDatas()[i]));
                            i++;
                        }
                    }

                    //前回値を格納
                    accFilter=new FilterSample(event.values);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    //磁界の取得
                    if(magFilter.setFilter(event.values)){
                        orientationSample.setMagneticFields(magFilter.getLowDatas());
                    }

                    magFilter = new FilterSample(event.values);
                    break;
            }
            //傾きを算出
            if (orientationSample.isCheck()) {
                //傾き算出クラス
                float[] rad = orientationSample.getCalcOrientation();
                //ドロイド君を動かす
                ivDroid.setRotationX(rad[1]);
                ivDroid.setRotationY(-rad[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //センサーの精度が変更されたとき
            //通常使用する機会は少ない
        }
    }


    private class ButtonClickListener implements View.OnClickListener{
        /**
         * ボタンクリックイベント
         * @param view ボタン
         */
        @Override
        public void onClick(View view) {
            Button btRun = (Button)view;
            if(btRun.getText().toString().equals(getString(R.string.bt_on))){
                //ボタンのテキスト変更
                btRun.setText(R.string.bt_off);
                //センサーの登録
                Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                Sensor sensor2 = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                //センサーリスナーの登録
                sensorMgr.registerListener(mListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
                sensorMgr.registerListener(mListener, sensor2, SensorManager.SENSOR_DELAY_FASTEST);
            }else{
                //ボタンのテキスト変更
                btRun.setText(R.string.bt_on);
                //後片付け
                sensorMgr.unregisterListener(mListener);
            }
        }
    }
}