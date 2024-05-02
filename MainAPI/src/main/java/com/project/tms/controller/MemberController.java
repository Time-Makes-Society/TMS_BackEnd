package com.project.tms.controller;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.dto.MemberDto;
import com.project.tms.dto.ReadTimeDto;
import com.project.tms.repository.TagRepository;
import com.project.tms.service.LoginService;
import com.project.tms.service.MemberService;
import com.project.tms.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final TagRepository tagRepository;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody MemberDto memberDto) {
        try {
            log.info("회원가입 실행");
            Long memberId = memberService.join(memberDto);

            // 회원가입 성공 시 응답 생성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.OK.value());
            responseBody.put("data", memberDto);
            responseBody.put("message", "회원가입이 정상적으로 처리되었습니다.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생", e);
            // 내부 서버 오류 시 응답 생성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseBody.put("data", null);
            responseBody.put("message", "회원가입 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseBody);
        }
//        return ResponseEntity.ok("회원가입이 완료되었습니다. 회원 loginId: " + memberDto.getLoginId());
    }


    /**
     * 태그 선택
     */
//    @PostMapping("/{memberId}/tag")
//    public ResponseEntity<String> selectTags(@PathVariable Long memberId, @RequestBody List<Long> tagIds) {
//        Member member = memberService.findById(memberId).orElse(null);
//        if (member == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        List<Tag> selectedTags = new ArrayList<>();
//        for (Long tagId : tagIds) {
//            Tag tag = tagRepository.findById(tagId).orElse(null);
//            if (tag != null) {
//                selectedTags.add(tag);
//            }
//        }
//
//        memberService.selectTags(member, selectedTags);
//
//
//        return ResponseEntity.ok("Tags selected successfully");
//    }

    /**
     * 파라미터 → String List
     * ex) ["ECONOMY", "ENTERTAIN", "WORLD" ]
     */
//    @PostMapping("/{memberId}/tag")
//    public ResponseEntity<String> selectTags(@PathVariable Long memberId, @RequestBody List<String> tagNames) {
//        Member member = memberService.findById(memberId).orElse(null);
//        if (member == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        memberService.selectTags(member, tagNames);
//
//        return ResponseEntity.ok("Tags selected successfully");
//    }

    @PostMapping("/{memberId}/tag")
    public ResponseEntity<Map<String, Object>> selectTags(@PathVariable Long memberId, @RequestBody List<String> tagNames) {
        Map<String, Object> responseBody = new HashMap<>();
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            responseBody.put("status", HttpStatus.NOT_FOUND.value());
            responseBody.put("message", "Member not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
        }

        memberService.selectTags(member, tagNames);

        responseBody.put("status", HttpStatus.OK.value());
        responseBody.put("message", "태그 선택 완료");
        responseBody.put("data", tagNames);
        return ResponseEntity.ok(responseBody);
    }


    @GetMapping("/{memberId}/tags")
    public ResponseEntity<List<String>> getMemberTags(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> tagNames = new ArrayList<>();
        for (MemberTag memberTag : member.getTagList()) {
            Long tagId = memberTag.getTag().getId();
            String tagName = TagIdToName(tagId);
            tagNames.add(tagName);
        }

        return ResponseEntity.ok(tagNames);
    }

    private String TagIdToName(Long tagId) {

        switch (tagId.intValue()) {
            case 1:
                return "CULTURE";
            case 2:
                return "ECONOMY";
            case 3:
                return "ENTERTAIN";
            case 4:
                return "POLITICS";
            case 5:
                return "SCIENCE";
            case 6:
                return "SOCIETY";
            case 7:
                return "SPORTS";
            case 8:
                return "TECHNOLOGY";
            case 9:
                return "WORLD";
            default:
                return null;
        }
    }

    /**
     * 회원 조회
     */
    @GetMapping("/get/{memberId}")
    public List<MemberDto> members(@PathVariable Long memberId) {
        Optional<Member> findMember = memberService.findById(memberId);

        return findMember.map(m ->
                        Collections.singletonList(new MemberDto(m.getLoginId(), m.getPassword(), m.getMemberName(), m.getMemberNickname(), m.getTotalReadTime())))
                .orElse(Collections.emptyList());
    }

    @GetMapping("/loginId")
    public String getLoginId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {

            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                return loginMember.getLoginId();
            }
        }
        return null; // 세션이 없거나 로그인 정보가 없는 경우
    }

    @GetMapping("/memberId")
    public Long getMemberId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {

            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {

                return loginService.getMemberIdByLoginId(loginMember.getLoginId());
            }
        }
        return null; // 세션이 없거나 로그인 정보가 없는 경우
    }

//    @PostMapping("/readTime")
//    public ResponseEntity<String> addReadTimeToMember(@RequestBody ReadTimeDto readTimeDto) {
//        memberService.addReadTimeToMember(readTimeDto.getMemberId(), LocalTime.parse(readTimeDto.getReadTime()));
//        return ResponseEntity.ok("읽기 시간이 추가되었습니다.");
//    }

    @PostMapping("/readTime")
    public ResponseEntity<Object> addReadTimeToMember(@RequestBody ReadTimeDto readTimeDto) {
        try {
            memberService.addReadTimeToMember(readTimeDto.getMemberId(), LocalTime.parse(readTimeDto.getReadTime()));

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", HttpStatus.OK.value());
            responseBody.put("message", "읽기 시간이 추가되었습니다.");

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "읽기 시간을 추가하는 중에 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }


}
