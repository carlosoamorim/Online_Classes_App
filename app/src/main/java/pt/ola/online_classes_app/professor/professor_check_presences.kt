package pt.ola.online_classes_app.professor


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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


        // Retrieve course name and class time from the intent
        val courseName = intent.getStringExtra("courseName") ?: "Unknown Course"
        val classTime = intent.getStringExtra("classTime") ?: "Unknown Time"

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val subtitleTextView = findViewById<TextView>(R.id.subtitleTextView)

        titleTextView.text = courseName
        subtitleTextView.text = classTime


        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Placeholders before API integration
        val classList = listOf(
            ClassInfoPresences("Alice Johnson", "alice.johnson@example.com"),
            ClassInfoPresences("Bob Smith", "bob.smith@example.com"),
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
