//package pt.ola.online_classes_app.admin.classes
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import pt.ola.online_classes_app.R
//import pt.ola.online_classes_app.admin.classes.ClassInfo
//
//
//class AdminAddClass : AppCompatActivity() {
//
//    private lateinit var btnAddClass: Button
//    private lateinit var classRecyclerView: RecyclerView
//    private lateinit var adminClassAdapter: AdminClassAdapter
//
//    private val classList = mutableListOf<ClassInfo>()
//
//    // Launcher for adding a new class
//    private val addClassLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { data ->
//                handleActivityResult(data, isEdit = false)
//            }
//        }
//    }
//
//    // Launcher for editing an existing class
//    private val editClassLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { data ->
//                handleActivityResult(data, isEdit = true)
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_admin_add_class)
//
//        btnAddClass = findViewById(R.id.btnAddClass)
//        classRecyclerView = findViewById(R.id.classRecyclerView)
//
//        loadClasses()
//
//        adminClassAdapter = AdminClassAdapter(
//            context = this,
//            classList = classList,
//            onEditClick = { classInfo -> openEditClassActivity(classInfo) },
//            onRemoveClick = { classInfo -> removeClass(classInfo) }
//        )
//
//        classRecyclerView.layoutManager = LinearLayoutManager(this)
//        classRecyclerView.adapter = adminClassAdapter
//
//        btnAddClass.setOnClickListener {
//            openAddClassActivity()
//        }
//        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//
//    }
//
//    private fun loadClasses() {
//        // Dummy data — replace with real data source
//        classList.add(ClassInfo("Math 101", "9:00", "10.00","Room A", 123))
//        classList.add(ClassInfo("History 201", "10:00", "11:30", "Room B", 122))
//    }
//
//    private fun openAddClassActivity() {
//        val intent = Intent(this, AddOrEditClassActivity::class.java)
//        addClassLauncher.launch(intent)
//    }
//
//    private fun openEditClassActivity(classInfo: ClassInfo) {
//        val intent = Intent(this, AddOrEditClassActivity::class.java).apply {
//            putExtra("classId", classList.indexOf(classInfo))
//            putExtra("className", classInfo.courseName)
//            putExtra("classRoom", classInfo.classRoom)
//            putExtra("classStartTime", classInfo.classStartTime)
//            putExtra("classEndTime", classInfo.classEndTime)
//            putExtra("teacherId", classInfo.teacherId.toString())
//        }
//        editClassLauncher.launch(intent)
//    }
//
//    private fun removeClass(classInfo: ClassInfo) {
//        val position = classList.indexOf(classInfo)
//        if (position != -1) {
//            classList.removeAt(position)
//            adminClassAdapter.notifyItemRemoved(position)
//        }
//    }
//
//    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
//        val name = data.getStringExtra("className") ?: return
//        val room = data.getStringExtra("classRoom") ?: return
//        val startTime = data.getStringExtra("classStartTime") ?: return
//        val endTime = data.getStringExtra("classEndTime") ?: return
//        val teacherId = data.getIntExtra("teacherId", 0)
//        val updatedClass = ClassInfo(name, startTime, endTime, room, teacherId)
//
//        if (isEdit) {
//            val position = data.getIntExtra("classId", -1)
//            if (position != -1 && position < classList.size) {
//                classList[position] = updatedClass
//                adminClassAdapter.notifyItemChanged(position)
//            }
//        } else {
//            classList.add(updatedClass)
//            adminClassAdapter.notifyItemInserted(classList.size - 1)
//        }
//    }
//
//}
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import pt.ola.online_classes_app.R

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
                    // Assuming your API does not provide classDate separately:
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
        val subjectId = data.getIntExtra("subjectId", -1)
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

        classList.add(newClass)
        classAdapter.notifyItemInserted(classList.size - 1)
    }

    private fun deleteClass(classItem: ClassInfo) {
        Log.d("AdminAddClass", "Deleting class id=${classItem.id}")

        val url = "http://10.0.2.2:8000/classes/${classItem.id}"
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

