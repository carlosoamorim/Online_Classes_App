package pt.ola.online_classes_app.admin.students

data class StudentInfo(
    val id: String,
    val studentName: String,
    val studentEmail: String,
    val studentPassword: String,
    var isEnrolled: Boolean
)