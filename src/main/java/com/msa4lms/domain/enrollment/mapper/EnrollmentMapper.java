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
        @Param("studentId") Long studentId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * 특정 학생의 특정 년도/학기 총 신청 학점 계산
     */
    int calculateTotalCredits(
        @Param("studentId") Long studentId,
        @Param("year") int year,
        @Param("semester") int semester
    );
}
