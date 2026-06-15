package com.msa4lms.domain.enrollment.controllers;

import com.msa4lms.domain.enrollment.dto.MyEnrollmentListRes;
import com.msa4lms.domain.enrollment.services.EnrollmentService;
import com.msa4lms.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//  @RestController
//   * 왜 쓰나요? 이 클래스가 외부(웹 브라우저)에서 오는 요청을 받는 통로임을 Spring에게 알려줍니다.
//   * 얻는 것: 데이터를 JSON 형식으로 알아서 변환해서 응답해 줍니다. 별도의 변환 코드 없이도 프론트엔드와 소통할 수 있습니다.
//
//   @RequiredArgsConstructor & private final ...
//  * 설명: EnrollmentService.java에서 본 것과 같습니다. 우리가 만든 EnrollmentService를 안전하게 가져와서 쓸 수 있게 해줍니다.
//
//
//  public GlobalRes<MyEnrollmentListRes> ...
//   * 설명: 반환 타입입니다. 우리가 만든 바구니(MyEnrollmentListRes)를 공통 규격(GlobalRes)으로 한 번 더 감쌌습니다.
//   * 얻는 것: 팀 컨벤션 준수. 모든 팀원이 똑같은 모양으로 대답하게 됩니다.
//
//   @RequestParam
//    왜 쓰나요? 주소창 뒤에 붙는 파라미터(예: ?studentId=501)를 자바 변수로 쏙 가져옵니다.
//    defaultValue: 만약 학기를 입력 안 하면 자동으로 2024년 1학기를 보여주도록 친절하게 설계했습니다.
//
//      result = enrollmentService.getMyEnrollments(...)
//     설명 : 실제 업무는 EnrollmentService.java에게 시킵니다. 컨트롤러는 "명령만 내리는 지휘관" 역할을 합니다.
//
//      return GlobalRes.success(result);
//   설명: EnrollmentService.java가 가져온 데이터를 GlobalRes 바구니에 담아 "성공(00)" 도장을 찍어서 내보냅니다.

/**
 * 수강 내역 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    /**
     * 내 수강 내역 목록과 총 신청 학점을 조회합니다.
     * GET /api/enrollments/my?studentId=501&year=2024&semester=1
     */
    @GetMapping("/my") // [5]
    public GlobalRes<MyEnrollmentListRes> getMyEnrollments( // [6]
            @RequestParam(name = "studentId") Long studentId, // [7]
            @RequestParam(name = "year", defaultValue = "2024") int year,
            @RequestParam(name = "semester", defaultValue = "1") int semester) {
        
        // [8] 서비스를 호출하여 데이터를 가져옵니다.
        MyEnrollmentListRes result = enrollmentService.getMyEnrollments(studentId, year, semester);
        
        // [9] 공통 응답 규격(GlobalRes)에 담아서 반환합니다.
        return GlobalRes.success(result);
    }
}
