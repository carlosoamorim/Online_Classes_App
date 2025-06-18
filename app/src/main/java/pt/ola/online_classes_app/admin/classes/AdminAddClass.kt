package pt.ola.online_classes_app.admin.classes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import pt.ola.online_classes_app.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Locale
import java.util.TimeZone

class AdminAddClass : AppCompatActivity() {

    private lateinit var btnAddClass: Button
    private lateinit var classRecyclerView: RecyclerView
    private lateinit var classAdapter: AdminClassAdapter

    private val classList = mutableListOf<ClassInfo>()

    private val addClassLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_class)

        btnAddClass = findViewById(R.id.btnAddClass)
        classRecyclerView = findViewById(R.id.classRecyclerView)

        classAdapter = AdminClassAdapter(
            context = this,
            classList = classList,
            onRemoveClick = { deleteClass(it) }
        )
        classRecyclerView.layoutManager = LinearLayoutManager(this)
        classRecyclerView.adapter = classAdapter

        btnAddClass.setOnClickListener {
            val intent = Intent(this, AddOrEditClassActivity::class.java)
            addClassLauncher.launch(intent)
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loadClasses()
    }

    private fun loadClasses() {
        val url = "http://10.0.2.2:8000/classes/"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                classList.clear()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)

                    val id = obj.getInt("id")
                    val subjectObj = obj.getJSONObject("subject")

                    val subjectId = subjectObj.getInt("id")
                    val courseName = subjectObj.getString("name")
                    val auditorium = obj.getString("auditorium")
                    val startTime = obj.getString("start_time")
                    val endTime = obj.getString("end_time")
                    val classDate = ""
                    val teacherId = subjectObj.getInt("teacher_id")

                    val newClass = ClassInfo(
                        id = id,
                        subjectId = subjectId,
                        courseName = courseName,
                        classDate = classDate,
                        classStartTime = startTime,
                        classEndTime = endTime,
                        classRoom = auditorium,
                        teacherId = teacherId
                    )
                    classList.add(newClass)
                }
                classAdapter.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to load classes", Toast.LENGTH_SHORT).show()
            })

        queue.add(request)
    }

    private fun handleActivityResult(data: Intent) {
        val id = data.getIntExtra("classId", 0)
        val subjectId = data.getIntExtra("subjectId", 0)
        val courseName = data.getStringExtra("courseName") ?: ""
        val auditorium = data.getStringExtra("classRoom") ?: ""
        val classDate = data.getStringExtra("classDate") ?: ""
        val startTime = data.getStringExtra("classStartTime") ?: ""
        val endTime = data.getStringExtra("classEndTime") ?: ""
        val teacherId = data.getIntExtra("teacherId", 0)

        Log.d("AdminAddClass", "Received class from result: ID=$id, SubjectID=$subjectId, Name=$courseName")

        if (courseName.isBlank() || auditorium.isBlank() || startTime.isBlank() || endTime.isBlank() || teacherId == 0) {
            Toast.makeText(this, "Invalid class data", Toast.LENGTH_SHORT).show()
            return
        }

        val newClass = ClassInfo(
            id = id,
            subjectId = subjectId,
            courseName = courseName,
            classDate = classDate,
            classStartTime = startTime,
            classEndTime = endTime,
            classRoom = auditorium,
            teacherId = teacherId
        )

        val url = "http://10.0.2.2:8000/classes/withname/?subject_name=${courseName}"
        val queue = Volley.newRequestQueue(this)


        fun toIso8601DateTime(dateStr: String, timeStr: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val combinedStr = "$dateStr $timeStr"
            val date = inputFormat.parse(combinedStr)

            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")

            return outputFormat.format(date)
        }


        val startDateTime = toIso8601DateTime(classDate, startTime)
        val endDateTime = toIso8601DateTime(classDate, endTime)

        val jsonBody = JSONObject().apply {
            put("subject_id", subjectId)
            put("start_time", startDateTime)
            put("end_time", endDateTime)
            put("auditorium", auditorium)
        }

        Log.d("AdminAddClass", "POST JSON body: $jsonBody")

        val request = object : JsonObjectRequest(
            Method.POST, url, jsonBody,
            { response ->

                Toast.makeText(this, "Class created", Toast.LENGTH_SHORT).show()


            },
            { error ->
                error.printStackTrace()
                Log.e("AdminAddClass", "POST failed: ${error.message}")
                Toast.makeText(this, "Failed to post class", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf(
                    "Content-Type" to "application/json",
                    "Accept" to "application/json"
                )
            }
        }

        queue.add(request)

        loadClasses()
    }

    private fun deleteClass(classItem: ClassInfo) {
        Log.d("AdminAddClass", "Deleting class id=${classItem.id}")

        val url = "http://10.0.2.2:8000/classes/?id=${classItem.id}"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.DELETE, url,
            {
                Log.d("AdminAddClass", "Delete successful for id=${classItem.id}")

                val index = classList.indexOf(classItem)
                if (index != -1) {
                    classList.removeAt(index)
                    classAdapter.notifyItemRemoved(index)
                }
                Toast.makeText(this, "Class deleted", Toast.LENGTH_SHORT).show()
            },
            {
                it.printStackTrace()
                it.networkResponse?.let { response ->
                    val errorMsg = String(response.data)
                    Log.e("AdminAddClass", "Server error: $errorMsg")
                }
                Toast.makeText(this, "Failed to delete class", Toast.LENGTH_SHORT).show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        queue.add(request)
    }

}

