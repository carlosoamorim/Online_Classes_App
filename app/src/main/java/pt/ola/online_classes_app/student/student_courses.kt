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

class student_courses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_courses)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Placeholder data for course names
        val courseNames = listOf("Mathematics", "Physics", "Chemistry", "History")

        recyclerView.adapter = StudentCoursesAdapter(this, courseNames)

        val homeButton = findViewById<Button>(R.id.home_btn)
        val classesButton = findViewById<Button>(R.id.classes_btn)

        homeButton.setOnClickListener {
            val intent = Intent(this, student_dashboard::class.java)
            startActivity(intent)
        }

        classesButton.setOnClickListener {
            val intent = Intent(this, student_class_schedule::class.java)
            startActivity(intent)
        }
    }
}