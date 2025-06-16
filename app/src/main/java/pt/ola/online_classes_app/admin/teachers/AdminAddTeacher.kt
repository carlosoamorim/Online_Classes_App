package pt.ola.online_classes_app.admin.teachers


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R

class AdminAddTeacher : AppCompatActivity() {

    private lateinit var btnAddTeacher: Button
    private lateinit var teacherRecyclerView: RecyclerView
    private lateinit var teacherAdapter: AdminTeacherAdapter

    private val teacherList = mutableListOf<TeacherInfo>()

    // Launcher for adding a teacher
    private val addTeacherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it, isEdit = false) }
        }
    }

    // Launcher for editing a teacher
    private val editTeacherLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleActivityResult(it, isEdit = true) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_teacher)

        btnAddTeacher = findViewById(R.id.btnAddTeacher)
        teacherRecyclerView = findViewById(R.id.teacherRecyclerView)

        loadTeachers()

        teacherAdapter = AdminTeacherAdapter(
            context = this,
            teacherList = teacherList,
            onEditClick = { openEditTeacherActivity(it) },
            onRemoveClick = { removeTeacher(it) }
        )

        teacherRecyclerView.layoutManager = LinearLayoutManager(this)
        teacherRecyclerView.adapter = teacherAdapter

        btnAddTeacher.setOnClickListener {
            openAddTeacherActivity()
        }
    }

    private fun loadTeachers() {
        // Dummy data — replace with API call or DB fetch
        teacherList.add(TeacherInfo("America Rio", "2024368@uni.com"))
        teacherList.add(TeacherInfo("Jose Mamede", "9093458@uni.com"))
    }

    private fun openAddTeacherActivity() {
        val intent = Intent(this, AddOrEditTeacher::class.java)
        addTeacherLauncher.launch(intent)
    }

    private fun openEditTeacherActivity(teacherInfo: TeacherInfo) {
        val intent = Intent(this, AddOrEditTeacher::class.java).apply {
            putExtra("teacherId", teacherList.indexOf(teacherInfo))
            putExtra("teacherName", teacherInfo.teacherName)
            putExtra("teacherEmail", teacherInfo.teacherEmail)
        }
        editTeacherLauncher.launch(intent)
    }

    private fun removeTeacher(teacherInfo: TeacherInfo) {
        val position = teacherList.indexOf(teacherInfo)
        if (position != -1) {
            teacherList.removeAt(position)
            teacherAdapter.notifyItemRemoved(position)
        }
    }

    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val name = data.getStringExtra("teacherName") ?: return
        val email = data.getStringExtra("teacherEmail") ?: return
        val updatedTeacher = TeacherInfo(name, email)

        if (isEdit) {
            val position = data.getIntExtra("teacherId", -1)
            if (position in teacherList.indices) {
                teacherList[position] = updatedTeacher
                teacherAdapter.notifyItemChanged(position)
            }
        } else {
            teacherList.add(updatedTeacher)
            teacherAdapter.notifyItemInserted(teacherList.size - 1)
        }
    }
}

