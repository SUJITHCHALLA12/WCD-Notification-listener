package com.pentester.wcd.ui.auth

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pentester.wcd.R
import com.pentester.wcd.security.PinManager

class PinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.activity_pin)

        val input = findViewById<EditText>(R.id.pinInput)
        val button = findViewById<Button>(R.id.unlockBtn)

        button.setOnClickListener {
            if (PinManager.verify(this, input.text.toString())) {
                finish()
            } else {
                input.error = "Wrong PIN"
            }
        }
    }
}