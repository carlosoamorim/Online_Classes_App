package pt.ola.online_classes_app.admin.students

data class ClassInfoPresences(
    val id: String,
    val studentName: String,
    val studentEmail: String,
    var isEnrolled: Boolean
)