import random
import uuid

# 영남대학교 단과대 및 학과 데이터
colleges = {
    "문과대학": ["국어국문학과", "심리학과", "사회학과", "철학과", "언론정보학과"],
    "자연과학대학": ["수학과", "통계학과", "물리학과", "화학과", "생명과학과"],
    "공과대학": ["건설시스템공학과", "환경공학과", "화학공학부", "신소재공학부"],
    "기계IT대학": ["기계공학부", "전기공학과", "전자공학과", "컴퓨터공학과", "정보통신공학과"],
    "정치행정대학": ["정치외교학과", "행정학과", "새마을국제개발학과"],
    "경상대학": ["경제금융학부", "무역학부", "경영학과", "회계세무학과"],
    "사범대학": ["국어교육과", "영어교육과", "수학교육과", "체육교육과"],
    "천마인재학부": ["천마인재학부"]
}

all_depts = [dept for depts in colleges.values() for dept in depts]
last_names = ["김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안", "송", "류", "전"]
first_names = ["민준", "서연", "도윤", "서윤", "예준", "지우", "시우", "하윤", "하준", "민서", "주원", "지유", "지호", "윤서", "지후", "채원", "준서", "지아", "준우", "은서"]

def generate_name():
    return random.choice(last_names) + random.choice(first_names)

def generate_sql():
    sql_file = "msa4-lms/lms_sample_data.sql"
    
    with open(sql_file, "w", encoding="utf-8") as f:
        # 1. DDL 작성
        f.write("CREATE DATABASE IF NOT EXISTS lms;\nUSE lms;\n\n")
        
        f.write("DROP TABLE IF EXISTS enrollments;\n")
        f.write("DROP TABLE IF EXISTS lectures;\n")
        f.write("DROP TABLE IF EXISTS courses;\n")
        f.write("DROP TABLE IF EXISTS users;\n")
        f.write("DROP TABLE IF EXISTS departments;\n\n")

        f.write("CREATE TABLE departments (\n    id INT AUTO_INCREMENT PRIMARY KEY,\n    name VARCHAR(100) NOT NULL\n);\n\n")
        
        f.write("CREATE TABLE users (\n    id INT AUTO_INCREMENT PRIMARY KEY,\n    user_no VARCHAR(20) UNIQUE NOT NULL,\n    name VARCHAR(50) NOT NULL,\n    email VARCHAR(100),\n    password VARCHAR(255) NOT NULL,\n    role ENUM('STUDENT', 'PROFESSOR', 'ADMIN') NOT NULL,\n    department_id INT,\n    FOREIGN KEY (department_id) REFERENCES departments(id)\n);\n\n")
        
        f.write("CREATE TABLE courses (\n    id INT AUTO_INCREMENT PRIMARY KEY,\n    code VARCHAR(20) UNIQUE NOT NULL,\n    name VARCHAR(100) NOT NULL,\n    credits INT NOT NULL\n);\n\n")
        
        f.write("CREATE TABLE lectures (\n    id INT AUTO_INCREMENT PRIMARY KEY,\n    course_id INT NOT NULL,\n    professor_id INT NOT NULL,\n    room VARCHAR(50),\n    schedule VARCHAR(100),\n    capacity INT NOT NULL,\n    year INT NOT NULL,\n    semester INT NOT NULL,\n    FOREIGN KEY (course_id) REFERENCES courses(id),\n    FOREIGN KEY (professor_id) REFERENCES users(id)\n);\n\n")
        
        f.write("CREATE TABLE enrollments (\n    id INT AUTO_INCREMENT PRIMARY KEY,\n    student_id INT NOT NULL,\n    lecture_id INT NOT NULL,\n    grade VARCHAR(2),\n    status ENUM('ENROLLED', 'COMPLETED', 'DROPPED') DEFAULT 'ENROLLED',\n    FOREIGN KEY (student_id) REFERENCES users(id),\n    FOREIGN KEY (lecture_id) REFERENCES lectures(id)\n);\n\n")

        # 2. Departments 데이터 (약 30개)
        f.write("-- Departments\n")
        for dept in all_depts:
            f.write(f"INSERT INTO departments (name) VALUES ('{dept}');\n")
        f.write("\n")

        # 3. Professors (500명)
        f.write("-- Professors\n")
        prof_ids = []
        for i in range(1, 501):
            user_no = f"P{20000000 + i}"
            name = generate_name()
            email = f"prof{i}@yu.ac.kr"
            dept_id = random.randint(1, len(all_depts))
            f.write(f"INSERT INTO users (user_no, name, email, password, role, department_id) VALUES ('{user_no}', '{name}', '{email}', 'password123', 'PROFESSOR', {dept_id});\n")
            prof_ids.append(i) # 실제 id는 1부터 시작 (departments 이후)
        f.write("\n")

        # 4. Students (10,000명)
        f.write("-- Students\n")
        student_ids = []
        # 영남대 학번 스타일: 22410001 (년도 + 단과대코드 등 + 일련번호)
        for i in range(1, 10001):
            year = random.choice([2019, 2020, 2021, 2022, 2023, 2024])
            user_no = f"{year}{random.randint(10, 99)}{i:04d}"
            name = generate_name()
            email = f"stud{i}@yu.ac.kr"
            dept_id = random.randint(1, len(all_depts))
            f.write(f"INSERT INTO users (user_no, name, email, password, role, department_id) VALUES ('{user_no}', '{name}', '{email}', 'password123', 'STUDENT', {dept_id});\n")
            student_ids.append(500 + i) # 교수(500명) 이후 id
        f.write("\n")

        # 5. Courses (1,000개)
        f.write("-- Courses\n")
        course_subjects = ["기초", "심화", "개론", "세미나", "연습", "설계", "실험", "특강"]
        for i in range(1, 1001):
            code = f"CS{1000 + i}"
            name = f"{random.choice(all_depts)} {random.choice(course_subjects)} {i}"
            credits = random.choice([1, 2, 3])
            f.write(f"INSERT INTO courses (code, name, credits) VALUES ('{code}', '{name}', {credits});\n")
        f.write("\n")

        # 6. Lectures (2,000개)
        f.write("-- Lectures\n")
        lecture_ids = []
        for i in range(1, 2001):
            course_id = random.randint(1, 1000)
            professor_id = random.randint(1, 500)
            room = f"IT관 {random.randint(101, 505)}호"
            schedule = f"{random.choice(['월', '화', '수', '목', '금'])}{random.randint(1, 9)}"
            capacity = random.choice([30, 40, 50, 60, 100])
            f.write(f"INSERT INTO lectures (course_id, professor_id, room, schedule, capacity, year, semester) VALUES ({course_id}, {professor_id}, '{room}', '{schedule}', {capacity}, 2024, 1);\n")
            lecture_ids.append(i)
        f.write("\n")

        # 7. Enrollments (86,430개) - 총합 10만 건을 위해 조정
        f.write("-- Enrollments\n")
        grades = ["A+", "A0", "B+", "B0", "C+", "C0", "D+", "D0", "F"]
        # 학생당 약 8.6과목 신청
        enrollment_count = 86430
        for i in range(enrollment_count):
            student_id = random.choice(student_ids)
            lecture_id = random.choice(lecture_ids)
            grade = random.choice(grades) if random.random() > 0.2 else "NULL"
            status = 'COMPLETED' if grade != "NULL" else 'ENROLLED'
            grade_val = f"'{grade}'" if grade != "NULL" else "NULL"
            f.write(f"INSERT INTO enrollments (student_id, lecture_id, grade, status) VALUES ({student_id}, {lecture_id}, {grade_val}, '{status}');\n")

    print(f"Successfully generated {sql_file}")

if __name__ == "__main__":
    generate_sql()
