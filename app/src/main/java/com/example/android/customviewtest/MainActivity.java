package com.example.android.customviewtest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    MyView compassImage;

    // guarda el angulo actual de giro
    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtenemos servicio de Sensores
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //Obtenemos los sensores de Campo Magnetico y Acelerometro
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        compassImage = (MyView) findViewById(R.id.compassImage);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Convertimos esta clase en Listener, marcamos la cedencia de toma de muestras Normal
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Al pausar quitamos Listener para ahorrar recursos
        mSensorManager.unregisterListener(this);
    }

    float[] mGravity;
    float[] mGeomagnetic;
    public void onSensorChanged(SensorEvent event) {
        //Logica que obtiene los valores x, y, z. El que nos interesa es Z, que es el eje que
        //sale de la pantalla, como si clavasemos un palo en ella y lo girasemos
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);

                //Azimuth en Radianes. Va de -PI a PI counter-clockwise.
                float azimuthInRadians = orientation[0];
                //Convertimos los radianes a Grados
                float azimuthInDegress = (float)Math.toDegrees(azimuthInRadians);
                if (azimuthInDegress < 0.0f) {
                    azimuthInDegress += 360.0f;
                }

//                Log.d("BRUJULA", "Z: "+azimuthInDegress);

                // create a rotation animation (reverse turn degree degrees)
                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                // how long the animation will take place
                ra.setDuration(210);

                // set the animation after the end of the reservation status
                ra.setFillAfter(true);

                // Start the animation
                compassImage.startAnimation(ra);
                currentDegree = -azimuthInDegress;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
