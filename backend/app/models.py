from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Boolean
from sqlalchemy.orm import relationship
from .database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255))
    email = Column(String(255), unique=True)
    password = Column(String(255))
    role = Column(String(50))  # "student", "teacher" or "admin"

    # For teachers: classes they teach
    subject = relationship("Subject", back_populates="teacher")

    # For students: classes enrolled in (via Enrollment)
    enrollment = relationship("Enrollment", back_populates="student", cascade="all, delete")
    attendance = relationship("Attendance", back_populates="student", cascade="all, delete")

class Subject(Base):
    __tablename__ = "subjects"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255))
    teacher_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))

    teacher = relationship("User", foreign_keys=[teacher_id])
    classes = relationship("Classes", cascade="all, delete", back_populates="subject")

class Enrollment(Base):
    __tablename__ = "enrollments"

    id = Column(Integer, primary_key=True, index=True, unique=True)

    student_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))
    subject_id = Column(Integer, ForeignKey("subjects.id", ondelete="CASCADE"))

    student = relationship("User", foreign_keys=[student_id])
    subject = relationship("Subject", foreign_keys=[subject_id])

class Classes(Base):
    __tablename__ = "classes"

    id = Column(Integer, primary_key=True, index=True)
    auditorium = Column(String(255))
    start_time = Column(DateTime)
    end_time = Column(DateTime)
    subject_id = Column(Integer, ForeignKey("subjects.id", ondelete="CASCADE"))

    subject = relationship("Subject", foreign_keys=[subject_id])
    attendance = relationship("Attendance", cascade="all, delete")

class Attendance(Base):
    __tablename__ = "attendances"
    id = Column(Integer, primary_key=True, index=True, unique=True)
    student_id = Column(Integer, ForeignKey("users.id", ondelete="CASCADE"))
    class_id = Column(Integer, ForeignKey("classes.id", ondelete="CASCADE"))
    mark = Column(Boolean, nullable=False, default=False)

    student = relationship("User", foreign_keys=[student_id])
    classes = relationship("Classes", foreign_keys=[class_id])