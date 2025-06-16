package pt.ola.online_classes_app.admin.classes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R

class AddOrEditClassActivity : AppCompatActivity() {

    private lateinit var editClassName: EditText
    private lateinit var editClassRoom: EditText
    private lateinit var editClassStartTime: EditText
    private lateinit var editClassEndTime: EditText
    private lateinit var editTeacherId: EditText
    private lateinit var btnSaveClass: Button

    private var isEditMode = false
    private var classId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_class)

        editClassName = findViewById(R.id.editClassName)
        editClassRoom = findViewById(R.id.editClassRoom)
        editClassStartTime = findViewById(R.id.editClassStartTime)
        editClassEndTime = findViewById(R.id.editClassEndTime)
        editTeacherId = findViewById(R.id.editTeacherId)
        btnSaveClass = findViewById(R.id.btnSaveClass)

        // Check if editing existing class
        if (intent.hasExtra("className")) {
            isEditMode = true
            classId = intent.getIntExtra("classId", -1)
            editClassName.setText(intent.getStringExtra("className"))
            editClassRoom.setText(intent.getStringExtra("classRoom"))
            editClassStartTime.setText(intent.getStringExtra("classStartTime"))
            editClassEndTime.setText(intent.getStringExtra("classEndTime"))
            editTeacherId.setText(intent.getStringExtra("teacherId"))
        }

        btnSaveClass.setOnClickListener {
            val name = editClassName.text.toString().trim()
            val room = editClassRoom.text.toString().trim()
            val startTime = editClassStartTime.text.toString().trim()
            val endTime = editClassEndTime.text.toString().trim()
            val teacherId = editTeacherId.text.toString().toIntOrNull() ?: 0

            if (name.isEmpty() || room.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || teacherId <= 0) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("classId", classId)
                putExtra("className", name)
                putExtra("classRoom", room)
                putExtra("classStartTime", startTime)
                putExtra("classEndTime", endTime)
                putExtra("teacherId", teacherId)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}
