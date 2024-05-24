package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.MemberTag;
import com.project.tms.domain.tag.Tag;
import com.project.tms.dto.MemberDto;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.TagRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final TagRepository tagRepository;

    private final JavaMailSender emailSender;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(MemberDto memberDto) {
        validateDuplicateMember(memberDto);

        Member member = new Member();
        member.setLoginId(memberDto.getLoginId());
        member.setPassword(memberDto.getPassword());
        member.setMemberName(memberDto.getMemberName());
        member.setMemberNickname(memberDto.getMemberNickname());

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    private void validateDuplicateMember(MemberDto memberDto) {
        List<Member> findMembers = memberRepository.findByLoginId(memberDto.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // SMTP 설정
    public String generateRandomString() {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


    public void sendEmail(String to, String fromName, String fromEmail, String subject, String text) {
        // 이메일 주소가 유효한지 검사
        if (!StringUtils.hasText(to) || !isValidEmail(to)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다.");
        }

        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            // 보낸 사람의 이름과 이메일 주소 설정
            helper.setFrom(new InternetAddress(fromEmail, fromName, "UTF-8"));
            helper.setSubject(subject);
            helper.setText(text);
            emailSender.send(message);
        } catch (MessagingException | MailException e) {
            // 이메일 전송 실패 시 예외 처리
            throw new RuntimeException("이메일 전송에 실패하였습니다.", e);
        } catch (UnsupportedEncodingException e) {
            // 인코딩 예외 처리
            throw new RuntimeException("이메일 보낼 때 인코딩 오류가 발생하였습니다.", e);
        }
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // 이메일 주소의 패턴을 정의
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        // 정규 표현식을 이용하여 이메일 주소 유효성 검사
        return Pattern.compile(emailRegex).matcher(email).matches();
    }


    /**
     * 태그 선택
     */
    @Transactional
    public void selectTags(Member member, List<String> tagNames) {
        // 새로운 태그 리스트를 추가합니다.
        for (String tagName : tagNames) {
            Long tagId = findTagIdByName(tagName);
            if (tagId != null) {
                // 이미 추가된 태그는 다시 추가하지 않도록 중복을 확인합니다.
                boolean tagExists = member.getTagList().stream()
                        .anyMatch(memberTag -> memberTag.getTag().getId().equals(tagId));
                if (!tagExists) {
                    Tag tag = tagRepository.findById(tagId).orElse(null);
                    if (tag != null) {
                        MemberTag memberTag = new MemberTag();
                        memberTag.setMember(member);
                        memberTag.setTag(tag);
                        member.getTagList().add(memberTag);
                    }
                }
            }
        }
        // 새로운 태그 리스트만을 저장합니다.
        memberRepository.save(member);
    }


    private Long findTagIdByName(String tagName) {
        switch (tagName) {
            case "문화":
                return 1L;
            case "경제":
                return 2L;
            case "연예":
                return 3L;
            case "정치":
                return 4L;
            case "과학":
                return 5L;
            case "사회":
                return 6L;
            case "스포츠":
                return 7L;
            case "기술":
                return 8L;
            case "해외":
                return 9L;
            default:
                return null;
        }
    }

    /**
     * 기사 읽은 시간 누적
     */
    @Transactional
    public void addReadTimeToMember(Long memberId, LocalTime readTime) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));
        member.addReadTime(readTime);
        memberRepository.save(member);
    }


    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
