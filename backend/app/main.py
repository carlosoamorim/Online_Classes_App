from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from . import models, schemas, crud
from .database import engine, SessionLocal, Base
from .models import Enrollment

Base.metadata.create_all(bind=engine)

app = FastAPI(title="Online Classes App")

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# USER HANDLE ===========================================================

# Post new user handle
@app.post("/users/", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    return crud.create_user(db, user)

# Get list of all users handle
@app.get("/users/", response_model=list[schemas.User])
def read_users(db: Session = Depends(get_db)):
    return crud.get_users(db)

# Delete user
@app.delete("/users/", response_model=list[schemas.User])
def delete_user(email: str, db: Session = Depends(get_db)):
    return crud.remove_user(db, email)

# CLASS HANDLE ===========================================================

# Post new Class handle
@app.post("/classes/", response_model=schemas.Classes)
def create_schedule(classes: schemas.ClassesCreate, db: Session = Depends(get_db)):
    ret_info = crud.create_classes(db, classes)
    students = crud.get_students_by_subject(classes.subject_id, db)
    for student in students:
        crud.create_attendance(
            db,
            schemas.AttendanceCreate(
                student_id=student.id,
                class_id=ret_info.id
            )
        )
    return ret_info

# Get list of all classes handle
@app.get("/classes/", response_model=list[schemas.Classes])
def read_schedules(db: Session = Depends(get_db)):
    return crud.get_classes(db)

@app.delete("/subjects/", response_model=list[schemas.Subject])
def delete_class(id: int, db: Session = Depends(get_db)):
    return crud.remove_class(db, id)

# SUBJECT HANDLE ===========================================================

# Post new subject handle
@app.post("/subjects/", response_model=schemas.Subject)
def create_subject(subject: schemas.SubjectCreate, db: Session = Depends(get_db)):
    return crud.create_subject(db, subject)

# Get list of all subjects handle
@app.get("/subjects/", response_model=list[schemas.Subject])
def read_subjects(db: Session = Depends(get_db)):
    return crud.get_subjects(db)

# Remove subject
@app.delete("/subjects/", response_model=list[schemas.Subject])
def delete_subject(name: str, db: Session = Depends(get_db)):
    return crud.remove_subject(db, name)

# ENROLLMENT HANDLE ===========================================================

# Post new enrollment handle
@app.post("/enrollments/", response_model=schemas.Enrollment)
def create_enrollment_record(enrollment: schemas.EnrollmentCreate, db: Session = Depends(get_db)):
    return crud.create_enrollment(db, enrollment)


# Get list of all enrollments handle
@app.get("/enrollments/", response_model=list[schemas.Enrollment])
def read_enrollments(db: Session = Depends(get_db)):
    return crud.get_enrollments(db)


# Remove enrollment
@app.delete("/enrollments/", response_model=list[schemas.Enrollment])
def delete_enrollment(enrollment: schemas.EnrollmentCreate, db: Session = Depends(get_db)):
    return crud.remove_enrollment(db, enrollment)


# ATTENDANCE HANDLE ===========================================================

# Post new attendance handle
@app.post("/attendances/", response_model=schemas.Attendance)
def create_attendance_record(attendance: schemas.AttendanceCreate, db: Session = Depends(get_db)):
    return crud.create_attendance(db, attendance)


# Get list of all attendances handle
@app.get("/attendances/", response_model=list[schemas.Attendance])
def read_attendances(db: Session = Depends(get_db)):
    return crud.get_attendance(db)


# Remove attendance
@app.delete("/attendances/", response_model=list[schemas.Attendance])
def delete_attendance(attendance: schemas.AttendanceCreate, db: Session = Depends(get_db)):
    return crud.remove_attendance(db, attendance)


# Set attendance mark to true
@app.put("/attendances/true/", response_model=list[schemas.Attendance])
def set_attendance_true(attendance: schemas.AttendanceCreate, db: Session = Depends(get_db)):
    return crud.set_attendance_true(db, attendance)


# Set attendance mark to false
@app.put("/attendances/false/", response_model=list[schemas.Attendance])
def set_attendance_false(attendance: schemas.AttendanceCreate, db: Session = Depends(get_db)):
    return crud.set_attendance_false(db, attendance)
