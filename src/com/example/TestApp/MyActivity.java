package com.example.TestApp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.TextView;
import org.w3c.dom.Text;

public class MyActivity extends Activity implements SensorEventListener {
    /**
     * Called when the activity is first created.
     */

    private float lastX, lastY, lastZ;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float deltaXMax = 0;
    private float deltYMax = 0;
    private float deltaZmax = 0;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float vibrateThreshold = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;
    public Vibrator v;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeViews();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            accelerometer =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
            vibrateThreshold = accelerometer.getMaximumRange() / 2;
        }
        else
        {
            // No accelerometer....
        }

        v = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void initializeViews()
    {
        currentX = (TextView)findViewById(R.id.CurrentX);
        currentY = (TextView)findViewById(R.id.CurrentY);
        currentZ = (TextView)findViewById(R.id.CurrentZ);

        maxX = (TextView)findViewById(R.id.MaxX);
        maxY = (TextView)findViewById(R.id.MaxY);
        maxZ = (TextView)findViewById(R.id.MaxZ);
    }

    protected void onResume() // registers the accelerometer for listening the events
    {
        super.onResume();
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        // clean current values
        displayCleanValues();
        //display the current x, y,z accelerometer values
        displayCurrentValues();
        // dispay the max x,y,z accelerometer values
        displayMaxValues();

        // get the change of the x, y, z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);


        if(deltaX < 2)
            deltaX = 0;
        if(deltaY < 2)
            deltaY = 0;
        if((deltaZ > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold))
            v.vibrate(50);
    }


    public void displayCleanValues()
    {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    public void displayCurrentValues()
    {
        currentX.setText(Float.toString(deltaX));
        currentY.setText(Float.toString(deltaY));
        currentX.setText(Float.toString(deltaZ));
    }

    public void displayMaxValues()
    {

    }

}
