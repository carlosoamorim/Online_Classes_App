package pt.ola.online_classes_app.admin.students

data class StudentInfo(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "student",
    var isEnrolled: Boolean = false,
    val id:Int = 0
)
