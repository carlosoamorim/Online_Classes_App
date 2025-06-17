from datetime import datetime

from sqlalchemy import insert
from sqlalchemy.orm import Session
from . import models, crud, schemas
from .crud import get_class_by_subject_auditorium_start, get_students_by_subject, create_attendance
from .database import engine, SessionLocal, Base

def populate(db: Session):
    Base.metadata.drop_all(bind=engine)
    Base.metadata.create_all(bind=engine)
    db_users = [
        models.User(
                    name = "Mikhail Gazizov",
                    email="mikhail@uni.com",
                    password="1234",
                    role="student"),
        models.User(
            name="Liepa Petronyte",
            email="liepa@uni.com",
            password="1234",
            role="student"),
        models.User(
            name="Carlos Portuguese",
            email="carlos@uni.com",
            password="1234",
            role="student"),
        models.User(
            name="Kazi Rion Dipto",
            email="kazi@uni.com",
            password="1234",
            role="student"),
        models.User(
            name="Americo Rio",
            email="americo@uni.com",
            password="1234",
            role="teacher"),
        models.User(
            name="Dean of University",
            email="dean@uni.com",
            password="1234",
            role="admin")
                ]

    for db_user in db_users:
        db.add(db_user)
        db.commit()
        db.refresh(db_user)

    db_subjects = [
        models.Subject(
            name="Mobile Apps Development",
            teacher_id=5),
        models.Subject(
            name="Web Apps Development",
            teacher_id=5)
    ]

    for db_subject in db_subjects:
        db.add(db_subject)
        db.commit()
        db.refresh(db_subject)

    db_enrollments = [
        models.Enrollment(
            student_id=1,
            subject_id=1),
        models.Enrollment(
            student_id=2,
            subject_id=1),
        models.Enrollment(
            student_id=3,
            subject_id=1),
        models.Enrollment(
            student_id=2,
            subject_id=2),
        models.Enrollment(
            student_id=3,
            subject_id=2),
        models.Enrollment(
            student_id=4,
            subject_id=2)
    ]

    for db_enrollment in db_enrollments:
        db.add(db_enrollment)
        db.commit()
        db.refresh(db_enrollment)

    db_classess = [
        models.Classes(
            subject_id=1,
            auditorium="A101",
            start_time=datetime.fromisoformat("2025-06-17T12:00:00"),
            end_time=datetime.fromisoformat("2025-06-17T14:00:00")),
        models.Classes(
            subject_id=1,
            auditorium="A101",
            start_time=datetime.fromisoformat("2025-06-17T14:00:00"),
            end_time=datetime.fromisoformat("2025-06-17T16:00:00")),
        models.Classes(
            subject_id=2,
            auditorium="INE auditorium",
            start_time=datetime.fromisoformat("2025-06-17T16:00:00"),
            end_time=datetime.fromisoformat("2025-06-17T18:00:00"))
    ]

    for db_classes in db_classess:
        db.execute(insert(models.Classes).values(start_time=db_classes.start_time, end_time=db_classes.end_time,
                                                 subject_id=db_classes.subject_id, auditorium=db_classes.auditorium))
        ret_info = get_class_by_subject_auditorium_start(db_classes.subject_id, db_classes.auditorium, db_classes.start_time, db)
        students = get_students_by_subject(db_classes.subject_id, db)
        for student in students:
            print(f"Creating attendance for student_id={student.student_id}, class_id={ret_info.id}")
            create_attendance(
                db,
                schemas.AttendanceCreate(
                    student_id=student.student_id,
                    class_id=ret_info.id,
                    mark=False
                )
            )
        db.commit()

populate(db = SessionLocal())