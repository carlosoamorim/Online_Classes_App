package pt.ola.online_classes_app.professor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.professor.ClassInfo
import pt.ola.online_classes_app.student.student_dashboard
import java.io.Console


class professor_class_schedule : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/classes/",
            null,
            Response.Listener { response ->
                val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val user_id = sharedPref.getInt("user_id", -1)
                var classList = ArrayList<ClassInfo>()
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    val id = item.getInt("id");
                    val subject_id = item.getInt("subject_id");
                    val auditorium = item.getString("auditorium");
                    val start_time= item.getString("start_time");
                    val end_time= item.getString("end_time");
                    val subject_name= item.getJSONObject("subject").getString("name")
                    val teacher_id= item.getJSONObject("subject").getInt("teacher_id")
                    if (user_id == teacher_id) {
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

                }
                var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = ClassInfoAdapter(this, classList.toList())
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(request)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_professor_class_schedule)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val classesButton = findViewById<Button>(R.id.home_btn)
        classesButton.setOnClickListener {

            val intent = Intent(this, professor_dashboard::class.java)
            startActivity(intent)
        }


    }
}

