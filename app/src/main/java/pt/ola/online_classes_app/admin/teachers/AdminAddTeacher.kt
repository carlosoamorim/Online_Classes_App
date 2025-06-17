package pt.ola.online_classes_app.admin.teachers


import TeacherApiService
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

class AdminAddTeacher : AppCompatActivity() {

    private lateinit var btnAddTeacher: Button
    private lateinit var teacherRecyclerView: RecyclerView
    private lateinit var teacherAdapter: AdminTeacherAdapter
    private lateinit var apiService: TeacherApiService


    private val teacherList = mutableListOf<TeacherInfo>()

    // Launcher for adding a teacher
    private val addTeacherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val name = intent.getStringExtra("teacherName") ?: return@let
                val email = intent.getStringExtra("teacherEmail") ?: return@let
                val password = intent.getStringExtra("teacherPassword") ?: return@let
                val newTeacher = TeacherInfo(-1, name, email, password, role = "teacher")
                handleActivityResult(newTeacher)
            }
        }
    }


    // Launcher for editing a teacher
//    private val editTeacherLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.let { handleActivityResult(it, isEdit = true) }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_teacher)

        btnAddTeacher = findViewById(R.id.btnAddTeacher)
        teacherRecyclerView = findViewById(R.id.teacherRecyclerView)

        apiService = TeacherApiService(this)
        loadTeachers()


        teacherAdapter = AdminTeacherAdapter(
            context = this,
            teacherList = teacherList,
            //onEditClick = { openEditTeacherActivity(it) },
            onRemoveClick = { removeTeacher(it) }
        )


        teacherRecyclerView.layoutManager = LinearLayoutManager(this)
        teacherRecyclerView.adapter = teacherAdapter

        btnAddTeacher.setOnClickListener {
            openAddTeacherActivity()
        }
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun loadTeachers() {
        apiService.getTeachers(
            onSuccess = {
                teacherList.clear()
                teacherList.addAll(it)
                teacherAdapter.notifyDataSetChanged()
            },
            onError = {
                Toast.makeText(this, "Failed to load teachers", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        )

    }

    private fun openAddTeacherActivity() {
        val intent = Intent(this, AddOrEditTeacher::class.java)
        addTeacherLauncher.launch(intent)
    }

//    private fun openEditTeacherActivity(teacherInfo: TeacherInfo) {
//        val intent = Intent(this, AddOrEditTeacher::class.java).apply {
//            putExtra("teacherId", teacherList.indexOf(teacherInfo))
//            putExtra("teacherName", teacherInfo.teacherName)
//            putExtra("teacherEmail", teacherInfo.teacherEmail)
//            putExtra("teacherPassword", teacherInfo.teacherPassword)
//        }
//        editTeacherLauncher.launch(intent)
//    }

    private fun removeTeacher(teacherInfo: TeacherInfo) {
        apiService.deleteTeacher(teacherInfo.email,
            onComplete = {
                val position = teacherList.indexOf(teacherInfo)
                if (position != -1) {
                    teacherList.removeAt(position)
                    teacherAdapter.notifyItemRemoved(position)
                }
            },
            onError = {
                Toast.makeText(this, "Failed to delete teacher", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        )
    }

    private fun handleActivityResult(newTeacher: TeacherInfo) {
        apiService.addTeacher(newTeacher,
            onComplete = {
                teacherList.add(newTeacher)
                teacherAdapter.notifyItemInserted(teacherList.size - 1)
            },
            onError = {
                Toast.makeText(this, "Failed to add teacher", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }
        )
    }


}

