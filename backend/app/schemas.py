from pydantic import BaseModel
from datetime import datetime

# USER SCHEMAS
class UserBase(BaseModel):
    name: str
    email: str
    password: str
    role: str

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
    start_time: datetime
    end_time: datetime

class ClassesCreate(ClassesBase):
    pass

class Classes(ClassesBase):
    id: int
    subject: Subject

    class Config:
        orm_mode = True
