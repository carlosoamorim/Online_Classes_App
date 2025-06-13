package pt.ola.online_classes_app.student

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class student_class_schedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_class_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val classesButton = findViewById<Button>(R.id.home_btn)
        classesButton.setOnClickListener {

            val intent = Intent(this, student_dashboard::class.java)
            startActivity(intent)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

       // Placeholders before API integration
        val classList = listOf(
            ClassInfo("Math 101", "10:00 AM - 11:00 AM", "Room A120"),
            ClassInfo("Physics 201", "11:30 AM - 12:30 PM", "Room B210"),
            ClassInfo("Chemistry 301", "1:00 PM - 2:00 PM", "Room C310")
        )

        recyclerView.adapter = ClassInfoAdapter(classList)
    }
    }
