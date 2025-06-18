package pt.ola.online_classes_app.admin.teachers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pt.ola.online_classes_app.R

class AddOrEditTeacher : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private var teacherId = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit_teacher)

        editName = findViewById(R.id.editTeacherName)
        editEmail = findViewById(R.id.editTeacherEmail)
        editPassword = findViewById(R.id.editTeacherPassword)
        val btnSave = findViewById<Button>(R.id.btnSaveTeacher)

        if (intent.hasExtra("teacherName")) {
            isEditMode = true
            editName.setText(intent.getStringExtra("teacherName"))
            editEmail.setText(intent.getStringExtra("teacherEmail"))
            editPassword.setText(intent.getStringExtra("teacherPassword"))
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString().trim()
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("teacherName", name)
                putExtra("teacherEmail", email)
                putExtra("teacherPassword", password)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}

