package com.project.tms.service;

import com.project.tms.domain.Article;
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
    @Transactional
    public void scrapArticle(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(()
                -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId).orElseThrow(()
                -> new IllegalArgumentException("Article not found"));


        Scrap scrap = new Scrap();
        scrap.setMember(member);
        scrap.setUuidArticle(uuidArticle);

        scrapRepository.save(scrap);
    }

    /**
     * 기사 스크랩 취소
     */
    public void scrapCancel(Long memberId, UUID articleId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
        UUIDArticle uuidArticle = uuidArticleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("Article not found"));

        scrapRepository.deleteByMemberAndUuidArticle(member, uuidArticle);
    }
}
