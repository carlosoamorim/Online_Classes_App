package pt.ola.online_classes_app.admin.courses

data class CourseInfo(
    val courseName: String,
    val teacherName: String,
    val studentList: MutableList<String> = mutableListOf()
)
