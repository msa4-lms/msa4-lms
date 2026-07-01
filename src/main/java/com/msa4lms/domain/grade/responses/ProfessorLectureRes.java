package com.msa4lms.domain.grade.responses;

import lombok.Builder;

@Builder
public record ProfessorLectureRes(
        Long lectureId,
        String courseCode,
        String departmentName,
        String courseName,
        Integer credit,
        Integer targetGrade,
        String professorName,
        String classroom,
        String schedule,
        Integer academicYear,
        String term,
        Integer capacity

) {
}
