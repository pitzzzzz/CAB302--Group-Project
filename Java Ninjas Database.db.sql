BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS "Courses" (
    "CourseID"       INTEGER PRIMARY KEY AUTOINCREMENT,
    "Code"           TEXT UNIQUE,
    "Awards"         TEXT CHECK("Awards" IN ('Diploma', 'Bachelor', 'Honours', 'Graduate Certificate', 'Graduate Diploma', 'Master')),
    "DurationYears"  REAL CHECK("DurationYears" > 0),
    "StudyChoice"    TEXT CHECK("StudyChoice" IN ('On-Campus', 'Online')),
    "Field"          TEXT,
    "CreatedAt"      DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "User" (
    "UserID"           INTEGER PRIMARY KEY AUTOINCREMENT,
    "FirstName"        TEXT NOT NULL,
    "LastName"         TEXT NOT NULL,
    "Email"            TEXT NOT NULL UNIQUE,
    "HashedPassword"   TEXT NOT NULL,
    "Age"              INTEGER NOT NULL CHECK("Age" >= 13 AND "Age" <= 120),
    "ProfileRole"      TEXT NOT NULL,
    "YearLevel"        TEXT CHECK("YearLevel" IN ('Year 10', 'Year 11', 'Year 12', 'Gap Year', 'University', 'Graduate', 'Other')),
    "TargetIntakeYear" INTEGER CHECK("TargetIntakeYear" BETWEEN 2025 AND 2040),
    "CreatedAt"        DATETIME DEFAULT CURRENT_TIMESTAMP
);

COMMIT;
