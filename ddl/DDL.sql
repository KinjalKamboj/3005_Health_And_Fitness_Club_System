DROP TABLE IF EXISTS Payments;
DROP TABLE IF EXISTS EquipmentMaintenance;
DROP TABLE IF EXISTS TrainerNotifications;
DROP TABLE IF EXISTS SessionParticipants;
DROP TABLE IF EXISTS ClassParticipants;
DROP TABLE IF EXISTS SessionsAndClasses;
DROP TABLE IF EXISTS AdminStaff;
DROP TABLE IF EXISTS Trainers;
DROP TABLE IF EXISTS Members;

CREATE TABLE IF NOT EXISTS Members (
	first_name TEXT NOT NULL PRIMARY KEY,
	last_name TEXT NOT NULL UNIQUE,
	passwd TEXT,
	join_date DATE DEFAULT CURRENT_DATE,
	exercise_routine TEXT,
	height DECIMAL(5, 1),
	current_weight DECIMAL(5, 1),
	weight_goal DECIMAL(5, 1),
	achieve_by_date DATE,
	systolic INT,
	diastolic INT,
	fitness_achievement TEXT
);

CREATE TABLE IF NOT EXISTS Trainers (
	first_name TEXT NOT NULL PRIMARY KEY,
	last_name TEXT NOT NULL UNIQUE,
	passwd TEXT
);

CREATE TABLE IF NOT EXISTS AdminStaff (
	first_name TEXT NOT NULL PRIMARY KEY,
	last_name TEXT NOT NULL UNIQUE,
	passwd TEXT
);

CREATE TABLE IF NOT EXISTS SessionsAndClasses (
	room_num INT UNIQUE PRIMARY KEY,
	of_type TEXT,
	tfirst_name TEXT,
	FOREIGN KEY (tfirst_name) REFERENCES Trainers (first_name),
	start_date DATE,
	start_time TIME
);

CREATE TABLE IF NOT EXISTS ClassParticipants (
	first_name TEXT,
	FOREIGN KEY (first_name) REFERENCES Members (first_name) ON UPDATE CASCADE ON DELETE CASCADE,
	room_num INT,
	FOREIGN KEY (room_num) REFERENCES SessionsAndClasses (room_num) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE(first_name, room_num)
);

CREATE TABLE IF NOT EXISTS SessionParticipants (
	first_name TEXT,
	FOREIGN KEY (first_name) REFERENCES Members (first_name) ON UPDATE CASCADE ON DELETE CASCADE,
	room_num INT,
	FOREIGN KEY (room_num) REFERENCES SessionsAndClasses (room_num) ON UPDATE CASCADE ON DELETE CASCADE,
	UNIQUE(first_name, room_num)
);

CREATE TABLE IF NOT EXISTS TrainerNotifications (
	tfirst_name TEXT,
	FOREIGN KEY (tfirst_name) REFERENCES Trainers (first_name),
	room_num INT,
	FOREIGN KEY (room_num) REFERENCES SessionsAndClasses (room_num) ON UPDATE CASCADE ON DELETE CASCADE,
	date_ DATE DEFAULT CURRENT_DATE,
	UNIQUE(tfirst_name, room_num)
);

CREATE TABLE IF NOT EXISTS EquipmentMaintenance (
	equipment_id SERIAL PRIMARY KEY,
	equipment_name TEXT,
	room_num INT,
	FOREIGN KEY (room_num) REFERENCES SessionsAndClasses (room_num) ON UPDATE CASCADE ON DELETE CASCADE,
	date_reported DATE DEFAULT CURRENT_DATE,
	status TEXT,
	UNIQUE(equipment_name, room_num)
);

CREATE TABLE IF NOT EXISTS Payments (
	payment_id SERIAL PRIMARY KEY,
	status TEXT,
	first_name TEXT,
    FOREIGN KEY (first_name) REFERENCES Members (first_name) ON UPDATE CASCADE ON DELETE CASCADE,
	last_name TEXT,
	FOREIGN KEY (last_name) REFERENCES Members(last_name) ON UPDATE CASCADE ON DELETE CASCADE,
	fee_paid DECIMAL(5, 2) NOT NULL,
	date_paid DATE DEFAULT CURRENT_DATE
);


