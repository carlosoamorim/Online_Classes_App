from sqlalchemy.orm import Session
from sqlalchemy import select, delete, update
from . import models, schemas

# USER FUNCTIONS =======================================================
def create_user(db: Session, user: schemas.UserCreate):
    db_user = models.User(**user.model_dump())
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_users(db: Session):
    return db.query(models.User).all()

def remove_user(db: Session, email: str):
    db_user_email = email
    print(db_user_email)
    db.execute(delete(models.User).where(models.User.email == db_user_email))
    db.commit()
    return db.query(models.User).all()


# SUBJECT FUNCTIONS =======================================================
def create_subject(db: Session, subject: schemas.SubjectCreate):
    db_subject = models.Subject(**subject.model_dump())
    db.add(db_subject)
    db.commit()
    db.refresh(db_subject)
    return db_subject

def get_subjects(db: Session):
    return db.query(models.Subject).all()

def get_subject_by_id(id: int, db: Session):
    return db.execute(select(models.Subject).where(models.Subject.id == id)).scalars().first()

def remove_subject(db: Session, name: str):
    db_subject_name = name
    print(db_subject_name)
    db.execute(delete(models.Subject).where(models.Subject.name == db_subject_name))
    db.commit()
    return db.query(models.Subject).all()

# CLASS FUNCTIONS =======================================================
def create_classes(db: Session, classes: schemas.ClassesCreate):
    db_classes = models.Classes(**classes.model_dump())
    result = db.add(db_classes)
    db.commit()
    db.refresh(db_classes)
    return result

def get_classes(db: Session):
    return db.query(models.Classes).all()

def remove_class(db: Session, id: int):
    db_class_id = id
    print(db_class_id)
    db.execute(delete(models.Classes).where(models.Classes.id == db_class_id))
    db.commit()
    return db.query(models.Classes).all()

# ENROLLMENT FUNCTIONS =======================================================

def create_enrollment(db: Session, enrollment: schemas.EnrollmentCreate):
    db_enrollments = models.Enrollment(**enrollment.model_dump())
    db.add(db_enrollments)
    db.commit()
    db.refresh(db_enrollments)
    return db_enrollments

def get_enrollments(db: Session):
    return db.query(models.Enrollment).all()

def get_enrollments_by_student(id: int, db: Session):
    return db.execute(select(models.Enrollment).where(models.Enrollment.student_id == id)).scalars().all()

def get_students_by_subject(id: int, db: Session):
    return db.execute(select(models.Enrollment).where(models.Enrollment.subject_id == id)).scalars().all()

def remove_enrollment(db: Session, enrollment: schemas.EnrollmentCreate):
    db_enrollments = models.Enrollment(**enrollment.model_dump())
    db.execute(delete(models.Enrollment).where(models.Enrollment == db_enrollments))
    db.commit()
    return db.query(models.Enrollment).all()

# ATTENDANCE FUNCTIONS =======================================================

def create_attendance(db: Session, attendance: schemas.AttendanceCreate):
    db_attendance = models.Attendance(**attendance.model_dump())
    db.add(db_attendance)
    db.commit()
    db.refresh(db_attendance)
    return db_attendance

def get_attendance(db: Session):
    return db.query(models.Attendance).all()

def get_attendance_by_class(class_id: int, db: Session):
    return db.execute(select(models.Attendance).where(models.Attendance.class_id == class_id)).scalars().all()

def remove_attendance(db: Session, attendance: schemas.AttendanceCreate):
    db_attendance = models.Attendance(**attendance.model_dump())
    db.execute(delete(models.Attendance).where(models.Attendance == db_attendance))
    db.commit()
    return db.query(models.Attendance).all()

def set_attendance_true(db: Session, attendance: schemas.AttendanceCreate):
    db_attendance = models.Attendance(**attendance.model_dump())
    db.execute(update(models.Attendance).where(models.Attendance == db_attendance).values(mark=True))
    db.commit()
    return db.query(models.Attendance).all()

def set_attendance_false(db: Session, attendance: schemas.AttendanceCreate):
    db_attendance = models.Attendance(**attendance.model_dump())
    db.execute(update(models.Attendance).where(models.Attendance == db_attendance).values(mark=False))
    db.commit()
    return db.query(models.Attendance).all()