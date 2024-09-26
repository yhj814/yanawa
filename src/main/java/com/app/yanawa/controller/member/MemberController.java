package com.app.yanawa.controller.member;

import com.app.yanawa.domain.member.MemberDTO;
import com.app.yanawa.domain.member.MemberVO;
import com.app.yanawa.service.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/yanawa/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 회원가입 페이지로 이동
    @GetMapping("signup")
    public String goToSignup(MemberDTO memberDTO ,HttpSession session) {
        return "login_page/signup"; // 회원가입 페이지 뷰
    }

    // 회원가입완료후 로그인페이지로 이동
    @PostMapping("signup")
    public RedirectView signup(MemberDTO memberDTO) {
        log.info("회원가입 요청 데이터: {}", memberDTO);

        // 데이터베이스에 저장
        memberService.join(memberDTO);
        log.info("회원가입 성공 : {}", memberDTO.getMemberName());

        return new RedirectView("/yanawa/member/login");
    }

    //    로그인페이지로 이동
    @GetMapping("login")
//    status쓸거면 직접 입력해줘야함
    public String goToLoginPage(@ModelAttribute("status") String status) {
        return "login_page/login"; // 로그인 페이지
    }

    // 이메일 중복 체크
    @GetMapping("check-email-duplicate")
    @ResponseBody
    public Map<String, Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = memberService.isEmailDuplicate(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return response;
    }

    // 닉네임 중복 체크
    @GetMapping("check-nickname-duplicate")
    @ResponseBody
    public Map<String, Boolean> checkNickNameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = memberService.isNickNameDuplicate(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("duplicate", isDuplicate);
        return response;
    }

    @PostMapping("/login")
    public RedirectView login(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
        log.info("로그인 요청 데이터: email={}, password={}", email, password);

        // 이메일과 비밀번호로 로그인 시도
        Optional<MemberVO> foundMember = memberService.login(email, password);

        if (foundMember.isPresent()) {
            log.info("로그인 성공! 회원 정보: {}", foundMember.get());
            // 로그인 성공 후 메인 페이지로 리다이렉트
            return new RedirectView("/yanawa/member/main");
        } else {
            log.info("로그인 실패! 이메일과 비밀번호를 확인하세요! ");
            redirectAttributes.addFlashAttribute("loginError", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return new RedirectView("/yanawa/member/login?status=false");
        }
    }

    @GetMapping("main")
    public String goToMainPage() {
        return "mainPage/main"; // 로그인 페이지
    }
}
