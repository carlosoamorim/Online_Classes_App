package pt.ola.online_classes_app.professor


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import pt.ola.online_classes_app.R
import kotlin.Int


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
        val auditorium = intent.getStringExtra("auditorium") ?: "Unknown Place"
        val class_id = intent.getIntExtra("class_id", 0)

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/attendances/",
            null,
            Response.Listener { response ->
                var classList = ArrayList<ClassInfoPresences>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val id = item.getInt("id");
                    val cur_class_id = item.getInt("class_id")
                    val mark = item.getBoolean("mark")
                    val student_id = item.getInt("student_id")
                    val student_name= item.getJSONObject("student").getString("name")
                    val student_email= item.getJSONObject("student").getString("email")
                    if (class_id == cur_class_id){
                        val cur_class = ClassInfoPresences(
                            id = id,
                            class_id = cur_class_id,
                            student_id = student_id,
                            studentName = student_name,
                            studentEmail = student_email,
                            mark = mark
                        )
                        classList.add(cur_class)
                    }
                }

                // Initialize RecyclerView
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ClassInfoAdapterPresences(this, classList)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val subtitleTextView = findViewById<TextView>(R.id.subtitleTextView)

        titleTextView.text = courseName
        subtitleTextView.text = classTime




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
