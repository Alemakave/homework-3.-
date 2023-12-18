SELECT student.name, student.age, faculty.name FROM student INNER JOIN faculty on faculty.id = student.faculty_id;

SELECT student.* FROM student INNER JOIN avatar on student.id = avatar.student_id;