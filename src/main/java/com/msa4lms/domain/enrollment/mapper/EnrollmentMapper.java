package com.msa4lms.domain.enrollment.mapper;

import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnrollmentMapper {

    /**
     * 특정 학생의 특정 년도/학기 수강 내역 목록 조회
     */
    List<EnrollmentRes.EnrollmentDetail> findMyEnrollments(
        @Param("studentId") Long userId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * 특정 학생의 특정 년도/학기 총 신청 학점 계산
     */
    int calculateTotalCredits(
        @Param("studentId") Long userId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * 수강 신청 정보 등록
     */
    void insertEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * 수강 신청 정보 삭제
     */
    void deleteEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * 수강 취소 시 연관된 성적 정보 먼저 삭제
     */
    void deleteGradeByEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * 중복 신청 확인
     */
    boolean existsEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * 현재 수강 신청 인원 조회
     */
    int getCurrentEnrollmentCount(@Param("lectureId") Long lectureId);

    /**
     * 강의 수강 정원 조회
     */
    int getLectureCapacity(@Param("lectureId") Long lectureId);

    /**
     * 시간표 중복 여부 확인
     */
    boolean hasScheduleOverlap(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    com.msa4lms.domain.enrollment.responses.LectureInfoForEnrollment findLectureInfoForEnrollment(@Param("lectureId") Long lectureId);
}
