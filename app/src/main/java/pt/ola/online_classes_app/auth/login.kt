package pt.ola.online_classes_app.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.professor.professor_class_schedule
import pt.ola.online_classes_app.professor.professor_dashboard

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {
            val intent = Intent(this, professor_dashboard::class.java)
            startActivity(intent)
        }
    }
}