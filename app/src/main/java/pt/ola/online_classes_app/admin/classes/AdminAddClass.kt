package pt.ola.online_classes_app.admin.classes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ola.online_classes_app.R
import pt.ola.online_classes_app.professor.ClassInfo


class AdminAddClass : AppCompatActivity() {

    private lateinit var btnAddClass: Button
    private lateinit var classRecyclerView: RecyclerView
    private lateinit var adminClassAdapter: AdminClassAdapter

    private val classList = mutableListOf<ClassInfo>()

    // Launcher for adding a new class
    private val addClassLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                handleActivityResult(data, isEdit = false)
            }
        }
    }

    // Launcher for editing an existing class
    private val editClassLauncher = registerForActivityResult(
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
        setContentView(R.layout.activity_admin_add_class)

        btnAddClass = findViewById(R.id.btnAddClass)
        classRecyclerView = findViewById(R.id.classRecyclerView)

        loadClasses()

        adminClassAdapter = AdminClassAdapter(
            context = this,
            classList = classList,
            onEditClick = { classInfo -> openEditClassActivity(classInfo) },
            onRemoveClick = { classInfo -> removeClass(classInfo) }
        )

        classRecyclerView.layoutManager = LinearLayoutManager(this)
        classRecyclerView.adapter = adminClassAdapter

        btnAddClass.setOnClickListener {
            openAddClassActivity()
        }
    }

    private fun loadClasses() {
        // Dummy data — replace with real data source
        classList.add(ClassInfo("Math 101", "9:00 AM - 10:00 AM", "Room A", 123))
        classList.add(ClassInfo("History 201", "10:00 AM - 11:00 AM", "Room B", 122))
    }

    private fun openAddClassActivity() {
        val intent = Intent(this, AddOrEditClassActivity::class.java)
        addClassLauncher.launch(intent)
    }

    private fun openEditClassActivity(classInfo: ClassInfo) {
        val intent = Intent(this, AddOrEditClassActivity::class.java).apply {
            putExtra("classId", classList.indexOf(classInfo))
            putExtra("className", classInfo.courseName)
            putExtra("classRoom", classInfo.classRoom)
            putExtra("classTime", classInfo.classTime)
            putExtra("teacherId", classInfo.teacherId.toString())
        }
        editClassLauncher.launch(intent)
    }

    private fun removeClass(classInfo: ClassInfo) {
        val position = classList.indexOf(classInfo)
        if (position != -1) {
            classList.removeAt(position)
            adminClassAdapter.notifyItemRemoved(position)
        }
    }

    private fun handleActivityResult(data: Intent, isEdit: Boolean) {
        val name = data.getStringExtra("className") ?: return
        val room = data.getStringExtra("classRoom") ?: return
        val time = data.getStringExtra("classTime") ?: return
        val teacherId = data.getIntExtra("teacherId", 0)
        val updatedClass = ClassInfo(name, time, room, teacherId)

        if (isEdit) {
            val position = data.getIntExtra("classId", -1)
            if (position != -1 && position < classList.size) {
                classList[position] = updatedClass
                adminClassAdapter.notifyItemChanged(position)
            }
        } else {
            classList.add(updatedClass)
            adminClassAdapter.notifyItemInserted(classList.size - 1)
        }
    }
}
