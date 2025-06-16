package pt.ola.online_classes_app.admin.teachers

//data class TeacherInfo(
//    val teacherId: Int,
//    val teacherName: String,
//    val teacherEmail: String,
//    val teacherPassword: String // Optional, can be empty if not needed
//)

data class TeacherInfo(
    //val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val role: String = "teacher"
)
