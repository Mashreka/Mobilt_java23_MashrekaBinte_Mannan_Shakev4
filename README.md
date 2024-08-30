# Simple Sensor App

This Android app demonstrates basic usage of the accelerometer sensor. It includes:

- **`MainActivity`**: Hosts a `SensorFragment` that displays accelerometer data.
- **`SensorFragment`**: Monitors and responds to sensor changes.

## Features

- **TextViews**: Show real-time x, y, z values from the accelerometer.
- **ProgressBar**: Updates based on the z-axis value, reflecting movement intensity.
- **ImageView**: Rotates based on the x-axis value.
- **Button**: Changes color and text when a shake is detected.
