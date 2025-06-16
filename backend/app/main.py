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
    """
    Creates a new user based on the provided user data.

    This function is responsible for handling the creation of a new user
    record in the database. It accepts user input in the format of
    `schemas.UserCreate` and uses it to generate a new user entry through
    the CRUD operation defined in the corresponding module.

    :param user: The data for the user to be created, including fields
        such as username, email, and password.
    :type user: schemas.UserCreate
    :param db: The database session dependency used to perform operations
        on the database.
    :type db: Session
    :return: The newly created user object.
    :rtype: schemas.User
    """
    return crud.create_user(db, user)

# Get list of all users handle
@app.get("/users/", response_model=list[schemas.User])
def read_users(db: Session = Depends(get_db)):
    """
    Retrieves a list of users from the database.

    This endpoint fetches all the user records using the database session
    and returns them as a list of `User` schema objects. The method relies on
    the `crud.get_users` function for retrieving data from the database.

    :param db: Database session dependency for querying the database
    :type db: Session
    :return: List of users retrieved from the database
    :rtype: list[schemas.User]
    """
    return crud.get_users(db)

# Delete user
@app.delete("/users/", response_model=list[schemas.User])
def delete_user(email: str, db: Session = Depends(get_db)):
    """
    Deletes a user from the database based on the provided email address. It utilizes the
    dependency-injected database session to perform the user removal operation.

    :param email: The email address of the user to be deleted.
    :param db: The database session object provided by dependency injection.
    :return: A list of user details after the specified user has been removed.
    """
    return crud.remove_user(db, email)

# CLASS HANDLE ===========================================================

# Post new Class handle
@app.post("/classes/", response_model=list[schemas.Classes])
def create_schedule(classes: schemas.ClassesCreate, db: Session = Depends(get_db)):
    """
    Create a new schedule along with attendance records for all students enrolled in
    the class's subject.

    :param classes: The data for creating a new class schedule.
    :type classes: schemas.ClassesCreate
    :param db: The database session used to perform database operations.
    :type db: Session
    :return: The newly created class schedule information.
    :rtype: schemas.Classes
    """
    return crud.create_classes(db, classes)

@app.get("/classes/{id}", response_model=list[schemas.Enrollment])
def get_students_by_course(course_id: int, db: Session = Depends(get_db)):
    return crud.get_students_by_subject(course_id, db)
# Get list of all classes handle
@app.get("/classes/", response_model=list[schemas.Classes])
def read_schedules(db: Session = Depends(get_db)):
    """
    Retrieves a list of all classes.

    :param db: Database session dependency
    :type db: Session
    :return: A list of classes retrieved from the database
    :rtype: list[schemas.Classes]
    """
    return crud.get_classes(db)

@app.delete("/subjects/", response_model=list[schemas.Subject])
def delete_class(id: int, db: Session = Depends(get_db)):
    """
    This function is responsible for deleting a subject record from the database
    based on the given subject ID. It uses dependency injection to provide
    the database session and performs the delete operation by interacting
    with the CRUD layer. The method returns a list of remaining subject
    records after deletion.

    :param id: The ID of the subject to be deleted.
    :type id: int
    :param db: The database session used for the delete operation.
    :type db: Session
    :return: A list of remaining subject records after the delete operation.
    :rtype: list[schemas.Subject]
    """
    return crud.remove_class(db, id)

# SUBJECT HANDLE ===========================================================

# Post new subject handle
@app.post("/subjects/", response_model=schemas.Subject)
def create_subject(subject: schemas.SubjectCreate, db: Session = Depends(get_db)):
    """
    Create a new subject in the database.

    This function handles the creation of a new subject using the provided
    data via the `schemas.SubjectCreate` model. It interacts with the
    database through the CRUD method to insert the subject details
    into the database.

    :param subject: The details of the subject to be created.
    :param db: The database session dependency.
    :return: The created subject object.
    """
    return crud.create_subject(db, subject)

# Get list of all subjects handle
@app.get("/subjects/", response_model=list[schemas.Subject])
def read_subjects(db: Session = Depends(get_db)):
    """
    Fetch a list of subjects from the database.

    :param db: Database session dependency.
    :type db: Session
    :return: List of subjects retrieved from the database.
    :rtype: list[schemas.Subject]
    """
    return crud.get_subjects(db)

# Remove subject
@app.delete("/subjects/", response_model=list[schemas.Subject])
def delete_subject(name: str, db: Session = Depends(get_db)):
    """
    Deletes a subject by its name from the database.

    This function facilitates the removal of a subject from the database by
    utilizing the specified `name` parameter. It queries the database using the
    provided `Session` object and performs the deletion operation.

    :param name: The name of the subject to be deleted.
    :type name: str
    :param db: Database session dependency used to perform database operations.
    :type db: Session
    :return: A list of subjects remaining in the database after the subject is
        deleted.
    :rtype: list[schemas.Subject]
    """
    return crud.remove_subject(db, name)

# ENROLLMENT HANDLE ===========================================================

# Post new enrollment handle
@app.post("/enrollments/", response_model=schemas.Enrollment)
def create_enrollment_record(enrollment: schemas.EnrollmentCreate, db: Session = Depends(get_db)):
    """
    Creates a new enrollment record in the database.

    This function receives an enrollment object and a database session,
    and utilizes CRUD operations to persist the enrollment record into
    the database.

    :param enrollment: Data required for creating a new enrollment record
        encapsulated in the ``schemas.EnrollmentCreate`` model.
    :param db: Database session used to interact with the database.
    :return: The newly created enrollment record, represented by
        the ``schemas.Enrollment`` model.
    """
    return crud.create_enrollment(db, enrollment)


# Get list of all enrollments handle
@app.get("/enrollments/", response_model=list[schemas.Enrollment])
def read_enrollments(db: Session = Depends(get_db)):
    '''
    This function returns a list of all enrollment records in the database.
    :param db:
    :return: List of enrollments
    '''
    return crud.get_enrollments(db)


# Remove enrollment
@app.delete("/enrollments/", response_model=list[schemas.Enrollment])
def delete_enrollment(enrollment_id: int, db: Session = Depends(get_db)):
    """
    This function removes an enrollment record from the database.
        It takes in a single parameter, enrollment_id, which is the ID of the enrollment record to be removed.
        The function returns a list of all enrollment records in the database after the deletion."""
    return crud.remove_enrollment(db, enrollment_id)


# ATTENDANCE HANDLE ===========================================================

# Post new attendance handle
@app.post("/attendances/", response_model=schemas.Attendance)
def create_attendance_record(attendance: schemas.AttendanceCreate, db: Session = Depends(get_db)):
    '''
    This function creates a new attendance record in the database.
    :param attendance: Body that you fill in
    :param db:
    :return: New attendance record
    '''
    return crud.create_attendance(db, attendance)


# Get list of all attendances handle
@app.get("/attendances/", response_model=list[schemas.Attendance])
def read_attendances(db: Session = Depends(get_db)):
    """
    Retrieve a list of attendances from the database.

    This function is an API endpoint that fetches all attendance records
    from the database using the specified dependency for database session injection.

    :param db: Database session dependency provided by ``Depends(get_db)``,
        which manages the connection and operations for the database.
    :return: A list of attendance records retrieved from the database,
        mapped to the schema defined in ``schemas.Attendance``.
    """
    return crud.get_attendance(db)


# Remove attendance
@app.delete("/attendances/", response_model=list[schemas.Attendance])
def delete_attendance(attendance: int, db: Session = Depends(get_db)):
    """
    Deletes an attendance entry from the database and returns the updated list
    of attendances.

    This function is an API endpoint that removes a specific attendance entry,
    based on the given identifier, from the database. It depends on an active
    database session, provided by the dependency injection mechanism. Upon
    successful removal, it returns the modified list of attendance records.

    :param attendance: The unique identifier of the attendance entry to be deleted.
    :param db: The database session object used to interact with the data store.
    :return: A list of attendance records after the specified entry has been removed.
    """
    return crud.remove_attendance(db, attendance)


# Set attendance mark to true
@app.put("/attendances/true/", response_model=list[schemas.Attendance])
def set_attendance_true(attendance: int, db: Session = Depends(get_db)):
    """
    Set an attendance record to true in the database.

    Updates the attendance status for a specific record identified by the
    `attendance` ID to true.

    :param attendance: The ID of the attendance record to be updated.
    :type attendance: int
    :param db: Database session dependency.
    :type db: Session
    :return: A list of the updated attendance records.
    :rtype: list[schemas.Attendance]
    """
    return crud.set_attendance_true(db, attendance)


# Set attendance mark to false
@app.put("/attendances/false/", response_model=list[schemas.Attendance])
def set_attendance_false(attendance: int, db: Session = Depends(get_db)):
    """
    Sets an attendance record to false in the database.

    This function updates the specified attendance record in the database
    and sets its status to false. The update is performed by calling the
    `set_attendance_false` function from the `crud` module. The function
    returns the updated record.

    Parameters:
        - attendance: The ID of the attendance record to update.
        - db: The database session dependency.

    :param attendance: The ID of the attendance record to be set to false.
    :type attendance: int
    :param db: The database session object used to perform the update.
    :type db: Session
    :return: A list of updated attendance records with the respective status set to false.
    :rtype: list[schemas.Attendance]
    """
    return crud.set_attendance_false(db, attendance)
