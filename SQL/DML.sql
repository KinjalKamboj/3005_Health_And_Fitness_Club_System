--SAMPLE DATA--

INSERT into Members (first_name, last_name, passwd, exercise_routine, height, current_weight, weight_goal, achieve_by_date, systolic, diastolic, fitness_achievement) VALUES
('Spongebob', 'Squarepants', 'Jelly123', 'Jumping jacks: 1 minute, Bodyweight squats: 3 sets of 10 reps, Push-ups: 3 sets of 10 reps', '1.5', '65.7', '60', '2024-05-30', '120', '120', 'Did 10 rounds of squats'),
('Patrick', 'Star', 'rock', 'Jogging or running: 20 minutes, Jump rope: 3 sets of 1 minute, High knees: 3 sets of 30 seconds', '1.7', '100', '50.5', '2024-10-08', '80', '100', 'Lost 2kg in a month!'),
('Larry', 'Lobster', 'LOBSTERS_RULE', 'Squats: 3 sets of 12 reps, Deadlifts: 3 sets of 10 reps, Leg press: 3 sets of 12 reps', '2', '80', '100', '2025-01-23', '100', '70', 'Exceeded goal of bulking.');

INSERT into Trainers (first_name, last_name, passwd) VALUES
('Sandy', 'Cheeks', 'TEXAS_FOR_LIFE'),
('Squidward', 'Tentacles', 'clarinetEnthusiast90'),
('Eugene', 'Krabs', '$$money$$');

INSERT into AdminStaff (first_name, last_name, passwd) VALUES
('Tristan', 'Wong', 'admin430'),
('Lujay', 'Sara', 'admin893'),
('Jacky', 'Silvia', 'admin343');

INSERT into SessionsAndClasses (room_num, of_type, tfirst_name, start_date, start_time) VALUES
('1', 'Class', 'Sandy', '2024-03-30', '8:30'),
('2', 'Class', 'Eugene', '2024-03-30', '9:30'),
('3', 'Session', 'Squidward', '2024-04-01', '10:30');


