from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
import time
from sqlalchemy.exc import OperationalError
import os

DB_USER = os.getenv("MYSQL_USER", "root")
DB_PASS = os.getenv("MYSQL_PASSWORD", "password")
DB_HOST = os.getenv("MYSQL_HOST", "db")
DB_NAME = os.getenv("MYSQL_DATABASE", "school")

SQLALCHEMY_DATABASE_URL = f"mysql+pymysql://{DB_USER}:{DB_PASS}@{DB_HOST}/{DB_NAME}"

engine = create_engine(SQLALCHEMY_DATABASE_URL)
SessionLocal = sessionmaker(bind=engine, autocommit=False, autoflush=False)


MAX_RETRIES = 10
RETRY_DELAY = 2

# Trial of connection to db service to avoid racing condition between containers;
for i in range(MAX_RETRIES):
    try:
        engine.connect()
        break
    except OperationalError:
        print(f"Database connection failed, retrying in {RETRY_DELAY} seconds...")
        time.sleep(RETRY_DELAY)
else:
    raise RuntimeError("Failed to connect to the database after several retries.")

Base = declarative_base()
