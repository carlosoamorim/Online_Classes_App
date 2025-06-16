from sqlalchemy.orm import Session
from . import models, schemas

def create_user(db: Session, user: schemas.UserCreate):
    db_user = models.User(**user.model_dump())
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_users(db: Session):
    return db.query(models.User).all()

def create_subject(db: Session, subject: schemas.SubjectCreate):
    db_subject = models.Subject(**subject.model_dump())
    db.add(db_subject)
    db.commit()
    db.refresh(db_subject)
    return db_subject

def get_subjects(db: Session):
    return db.query(models.Subject).all()


def create_classes(db: Session, classes: schemas.ClassesCreate):
    db_classes = models.Classes(**classes.dict())
    db.add(db_classes)
    db.commit()
    db.refresh(db_classes)
    return db_classes

def get_classes(db: Session):
    return db.query(models.Classes).all()
