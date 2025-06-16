package pt.ola.online_classes_app.admin.students

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.admin.students.ClassInfoPresences


class AdminAddStudent : AppCompatActivity() {

    private lateinit var btnAddStudent: Button
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adminStudentAdapter: AdminStudentAdapter

    private val studentList = mutableListOf<ClassInfoPresences>()

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
            onEditClick = { classInfoPresences -> openEditStudentActivity(classInfoPresences) },
            onRemoveClick = { classInfoPresences -> removeStudent(classInfoPresences) }
        )

        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        studentRecyclerView.adapter = adminStudentAdapter

        btnAddStudent.setOnClickListener {
            openAddStudentActivity()
        }
    }

    private fun loadStudents() {
        // Dummy data — replace with real data source
        studentList.add(ClassInfoPresences("1", "Student 1", "funemail@university.pt", false))
        studentList.add(ClassInfoPresences("2","Josh", "joshmail@gmail.com", false))
    }

    private fun openAddStudentActivity() {
        val intent = Intent(this, AddOrEditStudent::class.java)
        addStudentLauncher.launch(intent)
    }

    private fun openEditStudentActivity(classInfoPresences: ClassInfoPresences) {
        val intent = Intent(this, AddOrEditStudent::class.java).apply {
            putExtra("studentId", studentList.indexOf(classInfoPresences))
            putExtra("studentName", classInfoPresences.studentName)
            putExtra("studentEmail", classInfoPresences.studentEmail)
        }
        editStudentLauncher.launch(intent)
    }

    private fun removeStudent(classInfoPresences: ClassInfoPresences) {
        val position = studentList.indexOf(classInfoPresences)
        if (position != -1) {
            studentList.removeAt(position)
            adminStudentAdapter.notifyItemRemoved(position)
        }
    }

    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val name = data.getStringExtra("studentName") ?: return
        val email = data.getStringExtra("studentEmail") ?: return

        val id = if (isEdit) {
            studentList[data.getIntExtra("studentId", -1)].id
        } else {
            System.currentTimeMillis().toString()
        }

        val updatedStudent = ClassInfoPresences(id, name, email, false)

        if (isEdit) {
            val position = data.getIntExtra("studentId", -1)
            if (position != -1 && position < studentList.size) {
                studentList[position] = updatedStudent
                adminStudentAdapter.notifyItemChanged(position)
            }
        } else {
            studentList.add(updatedStudent)
            adminStudentAdapter.notifyItemInserted(studentList.size - 1)
        }
    }
}
