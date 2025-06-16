from pydantic import BaseModel
from datetime import datetime

# USER SCHEMAS
class UserBase(BaseModel):
    name: str
    email: str
    password: str
    role: str

class UserDelete(BaseModel):
    email: str
    pass

class UserCreate(UserBase):
    pass

class User(UserBase):
    id: int

    class Config:
        orm_mode = True

# SUBJECT SCHEMAS
class SubjectBase(BaseModel):
    name: str
    teacher_id: int

class SubjectCreate(SubjectBase):
    pass

class Subject(SubjectBase):
    id: int
    teacher: User

    class Config:
        orm_mode = True

# CLASSES SCHEMAS
class ClassesBase(BaseModel):
    subject_id: int
    auditorium: str
    start_time: datetime
    end_time: datetime

class ClassesCreate(ClassesBase):
    pass

class Classes(ClassesBase):
    id: int
    subject: Subject

    class Config:
        orm_mode = True

# ENROLLMENT SCHEMAS
class EnrollmentBase(BaseModel):
    subject_id: int
    student_id: int

class EnrollmentCreate(EnrollmentBase):
    pass

class Enrollment(EnrollmentBase):
    id: int
    subject: Subject
    student: User

    class Config:
        orm_mode = True

# ATTENDANCE SCHEMAS
class AttendanceBase(BaseModel):
    class_id: int
    student_id: int
    mark: bool

class AttendanceCreate(AttendanceBase):
    pass

class Attendance(AttendanceBase):
    id: int
    classes: Classes
    student: User

    class Config:
        orm_mode = True
