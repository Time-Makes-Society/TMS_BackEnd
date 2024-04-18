package com.project.tms.service;

import com.project.tms.domain.Member;
import com.project.tms.domain.Scrap;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.ScrapRepository;
import com.project.tms.repository.UUIDArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final UUIDArticleRepository uuidArticleRepository;

    /**
     * 기사 스크랩
     */
    /* 속성 저장 적용 x
    @Transactional
    public void scrapArticle(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId).orElseThrow(()
                -> new IllegalArgumentException("Article not found"));

        if (!scrapRepository.existsByMemberAndUuidArticle(member, uuidArticle)) {
            Scrap scrap = new Scrap();
            scrap.setMember(member);
            scrap.setUuidArticle(uuidArticle);

            scrapRepository.save(scrap);
        } else {
            throw new IllegalStateException("이미 해당 기사를 스크랩했습니다.");
        }
    }
*/

/* setter 사용
    @Transactional
    public void scrapArticle(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId).orElseThrow(()
                -> new IllegalArgumentException("Article not found"));

        if (!scrapRepository.existsByMemberAndUuidArticle(member, uuidArticle)) {
            Scrap scrap = new Scrap();
            scrap.setMember(member);
            scrap.setUuidArticle(uuidArticle);
            scrap.setTitle(uuidArticle.getTitle());
            scrap.setContent(uuidArticle.getContent());
            scrap.setCreatedDate(uuidArticle.getCreatedDate());
            scrap.setCategory(uuidArticle.getCategory());
            scrap.setImage(uuidArticle.getImage());
            scrap.setLink(uuidArticle.getLink());

            scrapRepository.save(scrap);
        } else {
            throw new IllegalStateException("이미 해당 기사를 스크랩했습니다.");
        }
    }

*/
    // 생성자 사용 방식 & 속성 저장
    @Transactional
    public void scrapArticle(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        if (!scrapRepository.existsByMemberAndUuidArticle(member, uuidArticle)) {
            Scrap scrap = new Scrap(member, uuidArticle);
            scrapRepository.save(scrap);
        } else {
            throw new IllegalStateException("이미 해당 기사를 스크랩했습니다.");
        }
    }



    /**
     * 기사 스크랩 취소
     */
/* 속성 미저장된 스크랩 삭제
    @Transactional
    public void scrapCancel(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("Article not found"));

        scrapRepository.deleteByMemberAndUuidArticle(member, uuidArticle);
    }

    */
    @Transactional
    public void scrapCancel(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        Scrap scrap = scrapRepository.findByMemberAndUuidArticle(member, uuidArticle)
                .orElseThrow(() -> new IllegalArgumentException("Scrap not found"));

        // 스크랩 삭제
        scrapRepository.delete(scrap);
    }


}
