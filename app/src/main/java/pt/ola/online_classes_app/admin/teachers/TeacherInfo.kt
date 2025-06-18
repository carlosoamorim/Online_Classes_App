package pt.ola.online_classes_app.admin.teachers


data class TeacherInfo(
    val id: Int = -1,
    val name: String,
    val email: String,
    val password: String,
    val role: String = "teacher"
)
