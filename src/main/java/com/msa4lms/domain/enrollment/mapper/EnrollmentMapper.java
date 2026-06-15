package com.msa4lms.domain.enrollment.mapper;

import com.msa4lms.domain.enrollment.dto.MyEnrollmentListRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.Alias;

import java.util.List;

//    @Mapper 어노테이션
//   * 왜 쓰나요? Spring에게 "이 인터페이스는 MyBatis용 매퍼야. 내가 XML이랑 연결해 줄게"라고 선언하는 겁니다.
//   * 얻는 것: Spring이 이 인터페이스를 읽어서 실제로 동작하는 "구현체"를 자동으로 만들어줍니다. 우리는 인터페이스만 정의하면 끝입니다. (매우 편리!)

//    interface
//    * 왜 쓰나요? MyBatis는 실제 로직(SQL)을 XML에 따로 두기 때문에, 자바 쪽에서는 "메서드 이름"만 정해진 인터페이스가 필요합니다.
//    * 얻는 것: 유연성. SQL이 바뀌어도 자바 코드를 다시 컴파일할 필요가 줄어듭니다.
//
//    findMyEnrollments 메서드
//   * 설명: "내 수강 목록을 찾아줘"라는 기능의 이름입니다.
//   * 반환 타입: List<...>입니다. 수강 과목은 여러 개일 수 있으니까요.
//
//    @Param("studentId")
//   * 왜 쓰나요? 자바의 변수 이름(studentId)을 MyBatis XML에서 쓸 이름과 연결해 줍니다.
//   * 얻는 것: 가독성. XML 파일에서 #{studentId}라고 쓰면 자바에서 넘긴 값이 쏙 들어갑니다. 파라미터가 여러 개일 때는 이 어노테이션이 필수입니다.
//
/**
 * 수강 내역 관련 데이터베이스 접근을 위한 MyBatis Mapper 인터페이스입니다.
 */
@Mapper
public interface EnrollmentMapper {

    /**
     * 특정 학생의 특정 년도/학기 수강 내역 목록을 조회합니다.
     */
    List<MyEnrollmentListRes.EnrollmentDetail> findMyEnrollments(
        @Param("studentId") Long studentId,
        @Param("year") int year,
        @Param("semester") int semester
    );

    /**
     * 특정 학생의 특정 년도/학기 총 신청 학점을 계산합니다.
     */
    int calculateTotalCredits(
        @Param("studentId") Long studentId,
        @Param("year") int year,
        @Param("semester") int semester
    );
}
