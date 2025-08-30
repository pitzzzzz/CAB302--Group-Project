BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Courses" (
	"CourseID"	INTEGER,
	"Code"	TEXT UNIQUE,
	"Awards"	TEXT CHECK("Awards" IN ('Diploma', 'Bachelor', 'Honours', 'Graduate Certificate', 'Graduate Diploma', 'Master')),
	"DurationYears"	REAL CHECK("DurationYears" > 0),
	"StudyChoice"	TEXT CHECK("StudyChoice" IN ('On-Campus', 'Online')),
	"Field"	TEXT,
	"CreatedAt"	datetime DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("CourseID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "User" (
	"UserID"	INTEGER,
	"Email"	TEXT NOT NULL UNIQUE,
	"HasedPassword"	TEXT NOT NULL,
	"Age"	INTEGER NOT NULL CHECK("Age" >= 13 AND "Age" <= 120),
	"Profile Role"	TEXT NOT NULL,
	"YearLevel"	TEXT,
	"CreatedAt"	datetime DEFAULT CURRENT_TIMESTAMP,
	"TargetIntakeYear"	INTEGER CHECK("TargetIntakeYear" BETWEEN 2025 AND 2040),
	PRIMARY KEY("UserID" AUTOINCREMENT),
	CHECK("YearLevel" IN ('Year 10', 'Year 11', 'Year 12', 'Gap Year', 'University', 'Graduate', 'Other'))
);
COMMIT;
