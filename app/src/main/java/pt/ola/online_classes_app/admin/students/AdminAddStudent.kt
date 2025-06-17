package pt.ola.online_classes_app.admin.students

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R


class AdminAddStudent : AppCompatActivity() {

    private lateinit var btnAddStudent: Button
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adminStudentAdapter: AdminStudentAdapter

    private val studentList = mutableListOf<StudentInfo>()

    // Launcher for adding a new class
    private val addStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                handleActivityResult(data, isEdit = false)
            }
        }
    }

    // Launcher for editing an existing class
    private val editStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                handleActivityResult(data, isEdit = true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_student)

        btnAddStudent = findViewById(R.id.btnAddStudent)
        studentRecyclerView = findViewById(R.id.studentRecyclerView)

        loadStudents()

        adminStudentAdapter = AdminStudentAdapter(
            context = this,
            studentList = studentList,
            //onEditClick = { classInfoPresences -> openEditStudentActivity(classInfoPresences) },
            onRemoveClick = { classInfoPresences -> removeStudent(classInfoPresences) }
        )

        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerView.adapter = adminStudentAdapter

        btnAddStudent.setOnClickListener {
            openAddStudentActivity()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun loadStudents() {
        val apiService = StudentApiService(this)
        apiService.getStudents(
            onSuccess = {
                studentList.clear()
                studentList.addAll(it)
                adminStudentAdapter.notifyDataSetChanged()
            },
            onError = {
                Toast.makeText(this, "Failed to load students", Toast.LENGTH_SHORT).show()
            }
        )    }

    private fun openAddStudentActivity() {
        val intent = Intent(this, AddOrEditStudent::class.java)
        addStudentLauncher.launch(intent)
    }

//    private fun openEditStudentActivity(classInfoPresences: StudentInfo) {
//        val intent = Intent(this, AddOrEditStudent::class.java).apply {
//            putExtra("studentId", studentList.indexOf(classInfoPresences))
//            putExtra("studentName", classInfoPresences.name)
//            putExtra("studentEmail", classInfoPresences.email)
//            putExtra("studentPassword", classInfoPresences.password)
//        }
//        editStudentLauncher.launch(intent)
//    }

    private fun removeStudent(classInfoPresences: StudentInfo) {
        val apiService = StudentApiService(this)
        apiService.deleteStudent(
            email = classInfoPresences.email,
            onComplete = {
                Toast.makeText(this, "Student removed", Toast.LENGTH_SHORT).show()
                loadStudents()
            },
            onError = {
                Toast.makeText(this, "Failed to remove student", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val name = data.getStringExtra("studentName") ?: return
        val email = data.getStringExtra("studentEmail") ?: return
        val password = data.getStringExtra("studentPassword") ?: return

        val apiService = StudentApiService(this)
        val newStudent = StudentInfo(name = name, email = email, password = password, role = "student")

        apiService.addStudent(
            student = newStudent,
            onComplete = {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show()
                loadStudents()
            },
            onError = {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
