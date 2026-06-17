package com.msa4lms.domain.attendance.mapper;

import com.msa4lms.domain.attendance.entities.Attendance;
import com.msa4lms.domain.attendance.responses.AttendanceRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    void insertAttendance(Attendance attendance);
    void updateAttendance(Attendance attendance);
    List<AttendanceRes> findByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
    List<AttendanceRes> findByLectureIdAndDate(@Param("lectureId") Long lectureId, @Param("date") LocalDate date);
    Attendance findById(@Param("id") Long id);
}
