package pt.ola.online_classes_app.admin.teachers

data class TeacherInfo(
    val teacherId: Int,
    val teacherName: String,
    val teacherEmail: String,
    val teacherPassword: String // Optional, can be empty if not needed
)
