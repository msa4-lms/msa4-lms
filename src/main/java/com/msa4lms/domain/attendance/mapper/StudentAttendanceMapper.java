package com.msa4lms.domain.attendance.mapper;

import com.msa4lms.domain.attendance.responses.AcademicAttendanceRes;
import com.msa4lms.domain.attendance.responses.AttendanceRateRes;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentAttendanceMapper {
    List<AttendanceRes> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
    List<AcademicAttendanceRes> findAttendanceByStudentId(@Param("studentId") long studentId);
    List<AttendanceRateRes> findAttendanceRatesByStudentId(@Param("studentId") long studentId,
                                                           @Param("year") Integer year,
                                                           @Param("semester") Integer semester);
    int countOwnedEnrollment(@Param("studentId") long studentId, @Param("enrollmentId") long enrollmentId);
    int countExcuseByDateAndPeriod(@Param("enrollmentId") long enrollmentId,
                                   @Param("lectureDate") String lectureDate,
                                   @Param("period") Integer period);
    Map<String, Object> findStudentExcuseAttachment(@Param("requestId") long requestId,
                                                    @Param("studentId") long studentId);
    void insertExcuseRequest(@Param("studentId") long studentId, @Param("enrollmentId") long enrollmentId,
                             @Param("lectureDate") String lectureDate, @Param("period") Integer period,
                             @Param("reason") String reason, @Param("attachmentOriginalName") String attachmentOriginalName,
                             @Param("attachmentStoredName") String attachmentStoredName,
                             @Param("attachmentContentType") String attachmentContentType,
                             @Param("attachmentSize") Long attachmentSize);
    List<ExcuseRequestRes> findExcuseRequestsByStudentId(@Param("studentId") long studentId);
}
