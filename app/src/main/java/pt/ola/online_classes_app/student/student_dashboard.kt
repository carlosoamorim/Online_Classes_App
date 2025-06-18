package pt.ola.online_classes_app.student

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.auth.login

class student_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)

        val classesButton = findViewById<Button>(R.id.classes_btn)
        classesButton.setOnClickListener {

            val intent = Intent(this, student_class_schedule::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.logout_btn).setOnClickListener {
            val intent = Intent(this, login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }
    }
}