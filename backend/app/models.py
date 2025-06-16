from sqlalchemy import Column, Integer, String, ForeignKey, DateTime
from sqlalchemy.orm import relationship
from .database import Base


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255))
    email = Column(String(255))
    password = Column(String(255))
    role = Column(String(50))  # "student", "teacher" or "admin"


class Subject(Base):
    __tablename__ = "subjects"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255))
    teacher_id = Column(Integer, ForeignKey("users.id"))

    teacher = relationship("User", foreign_keys=[teacher_id])


class Classes(Base):
    __tablename__ = "classes"

    id = Column(Integer, primary_key=True, index=True)
    start_time = Column(DateTime)
    end_time = Column(DateTime)
    subject_id = Column(Integer, ForeignKey("subjects.id"))

    subject = relationship("Subject", foreign_keys=[subject_id])
