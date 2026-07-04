package com.msa4lms.domain.attendance.mapper;

import com.msa4lms.domain.attendance.entities.Attendance;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import com.msa4lms.domain.attendance.responses.ExcuseRequestRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProfessorAttendanceMapper {
    void insertAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);
    Attendance findById(@Param("id") Long id);
    List<AttendanceRes> findByLectureIdAndDate(@Param("lectureId") Long lectureId, @Param("date") LocalDate date);
    List<ExcuseRequestRes> findPendingExcuseRequestsByProfessorId(@Param("professorId") long professorId);
    List<ExcuseRequestRes> findExcuseRequestsByProfessorId(@Param("professorId") long professorId);
    int updateExcuseRequestStatus(@Param("professorId") long professorId, @Param("requestId") long requestId,
                                  @Param("status") String status, @Param("rejectReason") String rejectReason);
    void applyApprovedExcuse(@Param("requestId") long requestId);
    Map<String, Object> findProfessorExcuseAttachment(@Param("requestId") long requestId,
                                                      @Param("professorId") long professorId);
}
