package com.msa4lms.domain.enrollment.services;

import com.msa4lms.domain.enrollment.dto.MyEnrollmentListRes;
import com.msa4lms.domain.enrollment.mapper.EnrollmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//   @Service
//     * 왜 쓰나요? 이 클래스가 비즈니스 로직(실무 로직)을 담당하는 클래스임을 Spring에게 알려주는 표식입니다.
//     * 얻는 것: Spring이 이 클래스를 관리하게 되어, 나중에 다른 곳에서 필요할 때 자동으로 "주입"받아 쓸 수 있습니다.

//   @RequiredArgsConstructor (Lombok)
//     * 왜 쓰나요? final이 붙은 변수(여기서는 Mapper)를 초기화해주는 생성자를 자동으로 만들어줍니다.
//     * 얻는 것: 코드가 깔끔해집니다. 예전에는 수동으로 생성자를 짰어야 했는데, 이제는 어노테이션 하나로 해결됩니다.

//   @Transactional(readOnly = true)
//     * 왜 쓰나요? 데이터베이스 작업을 하나의 "단위"로 묶습니다. readOnly = true는 "나는 읽기만 할 거야"라는 뜻입니다.
//     * 얻는 것: 성능 최적화. DB에게 읽기 전용임을 미리 알려주면 DB가 훨씬 빠르게 일합니다. 또한, 작업 도중 데이터가 변하지 않도록 보장해 줍니다.

//   private final EnrollmentMapper enrollmentMapper;
//    * 왜 쓰나요? 우리가 3단계에서 만든 DB 지도(Mapper)를 가져오는 겁니다. final을 붙여서 한 번 설정되면 바뀌지 않게(불변성) 보호합니다.
//    * 얻는 것: 안전성. 데이터베이스에 접근할 수 있는 유일한 통로를 확보합니다.

//   public MyEnrollmentListRes getMyEnrollments(...)
//     * 왜 쓰나요? 이 서비스의 핵심 임무입니다. 학생 ID, 년도, 학기를 주면 완성된 데이터를 돌려줍니다.
//     * 얻는 것: 캡슐화. 컨트롤러는 DB가 어떻게 생겼는지 몰라도 됩니다. 그냥 서비스한테 "데이터 줘!"라고만 하면 됩니다.

//   Mapper 호출하는 이유 :  실제로 DB에 가서 "목록"과 "학점 합계"를 가져오기 위해서
//     * 얻는 것: 필요한 원천 데이터 확보.

//   return new MyEnrollmentListRes(enrollments, totalCredits);
//     * 왜 쓰나요? 목록(List)과 숫자(int)는 서로 다른 타입이라 하나로 묶기 힘든데, 만들어 두었던 MyEnrollmentListRes.java (DTO(바구니))에 담아서 하나로 합쳐줍니다.
//     * 얻는 것: 통합된 응답. 프론트엔드 입장에서는 API를 두 번 부를 필요 없이, 한 번만 불러서 목록과 학점을 동시에 받을 수 있습니다.


/**
 * 수강 내역 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 트랜잭션으로 성능을 최적화합니다.
public class EnrollmentService {

    private final EnrollmentMapper enrollmentMapper;

    /**
     * 학생의 수강 내역 목록과 총 신청 학점을 조회하여 반환합니다.
     * 
     * @param studentId 학생 식별자
     * @param year 조회 년도
     * @param semester 조회 학기
     * @return 수강 내역 목록과 총 학점이 포함된 DTO
     */
    public MyEnrollmentListRes getMyEnrollments(Long studentId, int year, int semester) {
        // 1. Mapper를 통해 DB에서 수강 과목 상세 리스트를 가져옵니다.
        List<MyEnrollmentListRes.EnrollmentDetail> enrollments = 
            enrollmentMapper.findMyEnrollments(studentId, year, semester);
        
        // 2. Mapper를 통해 이번 학기 총 신청 학점의 합계를 가져옵니다.
        int totalCredits = enrollmentMapper.calculateTotalCredits(studentId, year, semester);
        
        // 3. 가져온 목록과 합산 학점을 하나의 응답 DTO(바구니)에 담아서 컨트롤러로 보냅니다.
        return new MyEnrollmentListRes(enrollments, totalCredits);
    }
}
