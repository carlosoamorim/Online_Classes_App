from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from . import models, schemas, crud
from .database import engine, SessionLocal, Base

Base.metadata.create_all(bind=engine)

app = FastAPI(title="Online Classes App")

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Post new user handle
@app.post("/users/", response_model=schemas.User)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    return crud.create_user(db, user)

# Get list of all users handle
@app.get("/users/", response_model=list[schemas.User])
def read_users(db: Session = Depends(get_db)):
    return crud.get_users(db)

# Post new Class handle
@app.post("/classes/", response_model=schemas.Classes)
def create_schedule(classes: schemas.ClassesCreate, db: Session = Depends(get_db)):
    return crud.create_classes(db, classes)

# Get list of all classes handle
@app.get("/classes/", response_model=list[schemas.Classes])
def read_schedules(db: Session = Depends(get_db)):
    return crud.get_classes(db)

# Post new subject handle
@app.post("/subjects/", response_model=schemas.Subject)
def create_subject(subject: schemas.SubjectCreate, db: Session = Depends(get_db)):
    return crud.create_subject(db, subject)

# Get list of all subjects handle
@app.get("/subjects/", response_model=list[schemas.Subject])
def read_subjects(db: Session = Depends(get_db)):
    return crud.get_subjects(db)
