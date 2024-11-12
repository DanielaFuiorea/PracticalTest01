package ro.pub.cs.systems.eim.PracticalTest01

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ro.pub.cs.systems.eim.PracticalTest01.Constants.INPUT1
import ro.pub.cs.systems.eim.PracticalTest01.Constants.INPUT2

class MainActivity : AppCompatActivity() {

    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private val intentFilter = IntentFilter()

    private var leftNumber = 0
    private var rightNumber = 0

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                Log.d("BroadcastReceiver", it.action.toString())
                Log.d("BroadcastReceiver", it.getStringExtra("extra").toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize EditTexts
        input1 = findViewById(R.id.input1)
        input2 = findViewById(R.id.input2)
        input1.setText("0")
        input2.setText("0")

        // Button: Press Me
        val pressMeButton = findViewById<Button>(R.id.press_me_button)
        pressMeButton.setOnClickListener {
            leftNumber++
            input1.setText(leftNumber.toString())
        }

        // Button: Press Me Too
        val pressMeTooButton = findViewById<Button>(R.id.press_me_too_button)
        pressMeTooButton.setOnClickListener {
            rightNumber++
            input2.setText(rightNumber.toString())
        }

        // Activity Result Launcher for navigating to another activity
        val activityResultsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "The activity returned with result OK", Toast.LENGTH_LONG).show()
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "The activity returned with result CANCELED", Toast.LENGTH_LONG).show()
            }
        }


    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(messageBroadcastReceiver)
    }

    override fun onDestroy() {
        stopService(intent)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(INPUT1, input1.text.toString())
        outState.putString(INPUT2, input2.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(INPUT1) && savedInstanceState.containsKey(INPUT2)) {
            input1.setText(savedInstanceState.getString(INPUT1))
            input2.setText(savedInstanceState.getString(INPUT2))
        }
    }


}