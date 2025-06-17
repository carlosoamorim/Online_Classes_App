package pt.ola.online_classes_app.admin.courses


data class CourseInfo(
    val courseId: Int,
    var courseName: String,
    var teacherName: String,
    var studentList: MutableList<String> = mutableListOf()
)
