package com.project.tms.service;

import com.project.tms.domain.ArticleLike;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.LikeCountDto;
import com.project.tms.repository.ArticleLikeRepository;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.UUIDArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {

    private final UUIDArticleRepository uuidArticleRepository;
    private final MemberRepository memberRepository;
    private final ArticleLikeRepository articleLikeRepository;

    public void likeArticle(UUID uuid, Long memberId) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + uuid));

        // 이미 좋아요를 누른 경우에는 중복해서 좋아요를 추가하지 않음
        if (!isMemberLikedArticle(article, memberId)) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found with id: " + memberId));

            ArticleLike articleLike = new ArticleLike();
            articleLike.setUuidArticle(article);
            articleLike.setMember(member);
            articleLikeRepository.save(articleLike);

            // 좋아요 추가 후 좋아요 수 증가
            article.setLikeCount(article.getLikeCount() != null ? article.getLikeCount() + 1 : 1);
            uuidArticleRepository.save(article);
        }
    }

    public void cancelLikeArticle(UUID uuid, Long memberId) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + uuid));

        // 좋아요를 취소할 경우 해당 회원 ID와 게시글 ID를 기준으로 좋아요를 찾아 삭제
        ArticleLike articleLike = articleLikeRepository.findByUuidArticleIdAndMemberId(article.getId(), memberId);
        if (articleLike != null) {
            articleLikeRepository.delete(articleLike);
            // 좋아요 취소 후 좋아요 수 감소
            article.setLikeCount(article.getLikeCount() != null && article.getLikeCount() > 0 ? article.getLikeCount() - 1 : 0);
            uuidArticleRepository.save(article);
        }
    }

    public LikeCountDto getLikeCount(UUID uuid) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found with id: " + uuid));

        LikeCountDto likeCountDto = entityToDto(article);
        return likeCountDto;
    }


    public LikeCountDto entityToDto(UUIDArticle uuidArticle) {
        LikeCountDto likeCountDto = new LikeCountDto();
        likeCountDto.setLikeCount(uuidArticle.getLikeCount());
        return likeCountDto;
    }

    private boolean isMemberLikedArticle(UUIDArticle article, Long memberId) {
        return article.getLikedByMembers().stream()
                .anyMatch(member -> member.getId().equals(memberId));
    }
}
