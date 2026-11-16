package edu.virginia.cs4720.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class StepRepository(context: Context, private val dao: StepDao) : SensorEventListener {
    private val sensorManager = context.getSystemService(SensorManager::class.java)
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val hasAccelerometer: Boolean get() = accelerometer != null

    val samples: Flow<List<AccelSample>> = dao.all()
    val stepsPerDay: Flow<List<DailySteps>> = dao.stepsPerDay()
    private val _currentReading = MutableStateFlow<AccelSample?>(null)
    val currentReading: StateFlow<AccelSample?> = _currentReading


    private var smoothed = SensorManager.GRAVITY_EARTH   // starts at 9.8
    private var inStep = false
    private var lastStepTimeMs = 0L

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it,
                SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitude = sqrt(x * x + y * y + z * z)

        smoothed = 0.8f * smoothed + 0.2f * magnitude

        val nowMs = event.timestamp / 1_000_000
        if (!inStep && smoothed > STEP_HIGH) {
            inStep = true
            if (nowMs - lastStepTimeMs > MIN_STEP_INTERVAL_MS) {
                lastStepTimeMs = nowMs
                val sample = AccelSample(timestamp = System.currentTimeMillis(), x = x, y = y, z = z)
                _currentReading.value = sample
                scope.launch { dao.insert(sample) }
            }
        } else if (inStep && smoothed < STEP_LOW) {
            inStep = false
        }
    }

    companion object {
        private const val STEP_HIGH = 11.0f
        private const val STEP_LOW = 10.0f
        private const val MIN_STEP_INTERVAL_MS = 250L
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}