package com.msa4lms.domain.academic.mapper;

import com.msa4lms.domain.academic.responses.AcademicAttendanceRes;
import com.msa4lms.domain.academic.responses.AttendanceRateRes;
import com.msa4lms.domain.academic.responses.ExcuseRequestRes;
import com.msa4lms.domain.academic.responses.SemesterGradeRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AcademicMapper {
    List<SemesterGradeRes> findGradesByStudentId(
            @Param("studentId") long studentId,
            @Param("year") Integer year,
            @Param("semester") Integer semester);

    List<AcademicAttendanceRes> findAttendanceByStudentId(@Param("studentId") long studentId);

    List<AttendanceRateRes> findAttendanceRatesByStudentId(
            @Param("studentId") long studentId,
            @Param("year") Integer year,
            @Param("semester") Integer semester);

    int countOwnedEnrollment(
            @Param("studentId") long studentId,
            @Param("enrollmentId") long enrollmentId);

    void insertExcuseRequest(
            @Param("studentId") long studentId,
            @Param("enrollmentId") long enrollmentId,
            @Param("lectureDate") String lectureDate,
            @Param("period") Integer period,
            @Param("reason") String reason,
            @Param("attachmentOriginalName") String attachmentOriginalName,
            @Param("attachmentStoredName") String attachmentStoredName,
            @Param("attachmentContentType") String attachmentContentType,
            @Param("attachmentSize") Long attachmentSize);

    List<ExcuseRequestRes> findExcuseRequestsByStudentId(@Param("studentId") long studentId);

    List<ExcuseRequestRes> findPendingExcuseRequestsByProfessorId(@Param("professorId") long professorId);

    List<ExcuseRequestRes> findExcuseRequestsByProfessorId(@Param("professorId") long professorId);

    int updateExcuseRequestStatus(
            @Param("professorId") long professorId,
            @Param("requestId") long requestId,
            @Param("status") String status,
            @Param("rejectReason") String rejectReason);

    void applyApprovedExcuse(@Param("requestId") long requestId);
}
