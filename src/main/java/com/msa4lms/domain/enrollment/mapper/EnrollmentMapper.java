package com.msa4lms.domain.enrollment.mapper;

import com.msa4lms.domain.enrollment.responses.EnrollmentRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EnrollmentMapper {

    /**
     * ?¹ì • ?™ìƒ???¹ì • ?„ë„/?™ê¸° ?˜ê°• ?´ì—­ ëª©ë¡ ì¡°íšŒ
     */
    List<EnrollmentRes.EnrollmentDetail> findMyEnrollments(
        @Param("studentId") Long userId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * ?¹ì • ?™ìƒ???¹ì • ?„ë„/?™ê¸° ì´?? ì²­ ?™ì  ê³„ì‚°
     */
    int calculateTotalCredits(
        @Param("studentId") Long userId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * ?˜ê°• ? ì²­ ?•ë³´ ?±ë¡
     */
    void insertEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * ?˜ê°• ? ì²­ ?•ë³´ ?? œ
     */
    void deleteEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * ì¤‘ë³µ ? ì²­ ?•ì¸
     */
    boolean existsEnrollment(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * ?„ì¬ ?˜ê°• ? ì²­ ?¸ì› ì¡°íšŒ
     */
    int getCurrentEnrollmentCount(@Param("lectureId") Long lectureId);

    /**
     * ê°•ì˜ ?˜ê°• ?•ì› ì¡°íšŒ
     */
    int getLectureCapacity(@Param("lectureId") Long lectureId);

    /**
     * ê°•ì˜ ?˜ê°• ?•ì› ì¡°íšŒ (ë¹„ê?????- FOR UPDATE)
     */
    int getLectureCapacityWithLock(@Param("lectureId") Long lectureId);

    /**
     * ?¹ì • ê°•ì˜???™ì  ì¡°íšŒ
     */
    int getLectureCredits(@Param("lectureId") Long lectureId);

    /**
     * ?¹ì • ?™ìƒ??(?´ë‹¹ ê°•ì˜ê°€ ?í•œ ?™ê¸°?? ?„ì¬ ì´?? ì²­ ?™ì  ê³„ì‚°
     */
    int calculateTotalCreditsByLectureSemester(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * ?œê°„??ì¤‘ë³µ ?¬ë? ?•ì¸
     */
    boolean hasScheduleOverlap(@Param("studentId") Long userId, @Param("lectureId") Long lectureId);

    /**
     * ?˜ê°• ? ì²­ ?´ë ¥ ?€??(ENROLL / CANCEL)
     */
    void insertEnrollmentHistory(@Param("studentId") Long userId, @Param("lectureId") Long lectureId, @Param("action") String action);

    /**
     * ?˜ê°• ì·¨ì†Œ ???±ì  ?•ë³´ ?¨ê»˜ ?? œ
     */
    
}
