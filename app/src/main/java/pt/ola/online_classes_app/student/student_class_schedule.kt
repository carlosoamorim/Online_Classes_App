package pt.ola.online_classes_app.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import pt.ola.online_classes_app.student.ClassInfo
import pt.ola.online_classes_app.student.ClassInfoAdapter

class student_class_schedule : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val user_id = sharedPref.getInt("user_id", -1)
        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/classes/by_student/$user_id",
            null,
            Response.Listener { response ->
                var classList = ArrayList<pt.ola.online_classes_app.student.ClassInfo>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val id = item.getInt("id");
                    val subject_id = item.getInt("subject_id");
                    val auditorium = item.getString("auditorium");
                    val start_time= item.getString("start_time");
                    val end_time= item.getString("end_time");
                    val subject_name= item.getJSONObject("subject").getString("name")
                    val teacher_id= item.getJSONObject("subject").getInt("teacher_id")

                    val cur_class = ClassInfo(
                        id = id,
                        subject_name = subject_name,
                        subject_id = subject_id,
                        auditorium = auditorium,
                        start_time = start_time,
                        end_time = end_time
                    )
                    classList.add(cur_class)


                }
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ClassInfoAdapter(classList)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)
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
        val goToCoursesButton = findViewById<Button>(R.id.goToCoursesButton)
        goToCoursesButton.setOnClickListener {
            val intent = Intent(this, student_courses::class.java)
            startActivity(intent)
        }
    }
    }
