package com.example.mysensorapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Button sensorButton;
    private TextView xValue, yValue, zValue;

    private float lastX, lastY, lastZ;
    private boolean isInitialized = false;

    private static final float SHAKE_THRESHOLD = 2.0f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        imageView = view.findViewById(R.id.imageView);
        sensorButton = view.findViewById(R.id.sensorButton);
        xValue = view.findViewById(R.id.xValue);
        yValue = view.findViewById(R.id.yValue);
        zValue = view.findViewById(R.id.zValue);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Accelerometer not available!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (!isInitialized) {
                lastX = x;
                lastY = y;
                lastZ = z;
                isInitialized = true;
            }

            float deltaX = Math.abs(lastX - x);
            float deltaY = Math.abs(lastY - y);
            float deltaZ = Math.abs(lastZ - z);

            lastX = x;
            lastY = y;
            lastZ = z;

            xValue.setText(String.format("X: %.2f", x));
            yValue.setText(String.format("Y: %.2f", y));
            zValue.setText(String.format("Z: %.2f", z));

            int progress = (int) Math.min(100, Math.abs(z * 10));
            progressBar.setProgress(progress);

            imageView.setRotation(imageView.getRotation() + x);

            if (deltaX > SHAKE_THRESHOLD || deltaY > SHAKE_THRESHOLD || deltaZ > SHAKE_THRESHOLD) {
                sensorButton.setText("Shake Detected!");
                sensorButton.setBackgroundColor(Color.RED);
                Toast.makeText(getActivity(), "Shake Detected!", Toast.LENGTH_SHORT).show();
            } else {
                sensorButton.setText("Shake Detector");
                sensorButton.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
