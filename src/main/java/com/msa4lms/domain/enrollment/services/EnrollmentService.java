package com.msa4lms.domain.enrollment.services;

import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.mapper.EnrollmentMapper;
import com.msa4lms.global.errors.custom.CapacityExceededException;
import com.msa4lms.global.errors.custom.DuplicatedRecordException;
import com.msa4lms.global.errors.custom.ScheduleOverlapException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final EnrollmentMapper enrollmentMapper;

    /**
     * 학생의 수강 내역 목록과 총 신청 학점 조회
     */
    public EnrollmentRes getMyEnrollments(Long studentId, int year, int semester) {
        List<EnrollmentRes.EnrollmentDetail> enrollments = 
            enrollmentMapper.findMyEnrollments(studentId, year, semester);
        
        int totalCredits = enrollmentMapper.calculateTotalCredits(studentId, year, semester);
        
        return new EnrollmentRes(enrollments, totalCredits);
    }

    /**
     * 수강 신청
     */
    @Transactional
    public void applyEnrollment(Long studentId, Long lectureId) {
        // 1. 중복 신청 방지
        if (enrollmentMapper.existsEnrollment(studentId, lectureId)) {
            throw new DuplicatedRecordException("이미 신청한 강의입니다.");
        }

        // 2. 정원 초과 방지
        int currentCount = enrollmentMapper.getCurrentEnrollmentCount(lectureId);
        int capacity = enrollmentMapper.getLectureCapacity(lectureId);
        if (currentCount >= capacity) {
            throw new CapacityExceededException("수강 정원이 초과되었습니다. (정원: " + capacity + "명)");
        }

        // 3. 시간표 중복 방지
        if (enrollmentMapper.hasScheduleOverlap(studentId, lectureId)) {
            throw new ScheduleOverlapException("이미 신청한 강의와 시간이 겹칩니다.");
        }

        // 4. 신청 등록
        enrollmentMapper.insertEnrollment(studentId, lectureId);
    }

    /**
     * 수강 취소
     */
    @Transactional
    public void cancelEnrollment(Long studentId, Long lectureId) {
        enrollmentMapper.deleteEnrollment(studentId, lectureId);
    }
}
