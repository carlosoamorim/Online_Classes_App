package pt.ola.online_classes_app.professor

data class ClassInfoPresences(
    val id: Int,
    val class_id: Int,
    val student_id: Int,
    val studentName: String,
    val studentEmail: String,
    val mark: Boolean
)