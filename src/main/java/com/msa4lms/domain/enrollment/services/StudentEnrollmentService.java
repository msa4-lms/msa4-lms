package com.msa4lms.domain.enrollment.services;

import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import com.msa4lms.domain.enrollment.mapper.StudentEnrollmentMapper;
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
public class StudentEnrollmentService {

    private final StudentEnrollmentMapper enrollmentMapper;

    /**
     * 학생의 수강 내역 목록과 총 신청 학점 조회
     */
    public EnrollmentRes getMyEnrollments(Long userId, int year, int semester) {
        List<EnrollmentRes.EnrollmentDetail> enrollments = 
            enrollmentMapper.findMyEnrollments(userId, year, semester);
        
        int totalCredits = enrollmentMapper.calculateTotalCredits(userId, year, semester);
        
        return new EnrollmentRes(enrollments, totalCredits);
    }

    /**
     * 수강 신청
     */
    @Transactional
    public void applyEnrollment(Long userId, Long lectureId) {
        // 1. 비관적 락(Pessimistic Lock)을 통한 강의 정원 조회 및 락 획득
        int capacity = enrollmentMapper.getLectureCapacityWithLock(lectureId);

        // 2. 현재 신청 인원 조회 (락 획득 후 조회하여 동시성 문제 차단)
        int currentCount = enrollmentMapper.getCurrentEnrollmentCount(lectureId);
        if (currentCount >= capacity) {
            throw new CapacityExceededException("수강 정원이 초과되었습니다. (정원: " + capacity + "명)");
        }

        // 3. 최대 신청 가능 학점(18학점) 검증
        int lectureCredits = enrollmentMapper.getLectureCredits(lectureId);
        int currentTotalCredits = enrollmentMapper.calculateTotalCreditsByLectureSemester(userId, lectureId);
        
        if (currentTotalCredits + lectureCredits > 18) {
            throw new CapacityExceededException("한 학기에 최대 18학점까지만 수강할 수 있습니다.");
        }

        // 3. 중복 신청 방지
        if (enrollmentMapper.existsEnrollment(userId, lectureId)) {
            throw new DuplicatedRecordException("이미 신청한 강의입니다.");
        }

        // 4. 시간표 중복 방지
        if (enrollmentMapper.hasScheduleOverlap(userId, lectureId)) {
            throw new ScheduleOverlapException("이미 신청한 강의와 시간이 겹칩니다.");
        }

        // 5. 신청 등록
        enrollmentMapper.insertEnrollment(userId, lectureId);

        // 6. 신청 이력 기록
        enrollmentMapper.insertEnrollmentHistory(userId, lectureId, "ENROLL");
    }

    /**
     * 수강 취소
     */
    @Transactional
    public void cancelEnrollment(Long userId, Long lectureId) {
        enrollmentMapper.deleteEnrollment(userId, lectureId);
        
        // 취소 이력 기록
        enrollmentMapper.insertEnrollmentHistory(userId, lectureId, "CANCEL");
    }
}
