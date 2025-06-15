package pt.ola.online_classes_app.professor


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
import pt.ola.online_classes_app.student.ClassInfo


class professor_check_presences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professor_check_presences)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Placeholders before API integration
        val classList = listOf(
            ClassInfoPresences("Math 101", "10:00 AM - 11:00 AM", "Room A120"),
            ClassInfoPresences("Physics 201", "11:30 AM - 12:30 PM", "Room B210"),
        )
        recyclerView.adapter = ClassInfoAdapterPresences(this, classList)

        val classesButton = findViewById<Button>(R.id.classes_btn)
        classesButton.setOnClickListener {
            val intent = Intent(this, professor_class_schedule::class.java)
            startActivity(intent)
        }


        val homeButton = findViewById<Button>(R.id.home_btn)
        homeButton.setOnClickListener {
            val intent = Intent(this, professor_dashboard::class.java)
            startActivity(intent)
        }
    }
}
