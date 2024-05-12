package com.project.tms.service;

import com.project.tms.domain.ArticleLike;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.LikeCountDto;
import com.project.tms.dto.UUIDArticleListDto;
import com.project.tms.repository.ArticleLikeRepository;
import com.project.tms.repository.MemberRepository;
import com.project.tms.repository.UUIDArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@Transactional
@RequiredArgsConstructor
@Log4j2
public class ArticleLikeService {

    private final UUIDArticleRepository uuidArticleRepository;

    private final MemberRepository memberRepository;

    private final ArticleLikeRepository articleLikeRepository;


    public void likeArticle(UUID uuid, Long memberId) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 기사 아이디입니다.: " + uuid));

        // 이미 좋아요를 누른 경우에는 중복해서 좋아요를 추가하지 않음
        if (isMemberLikedArticle(article, memberId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요한 기사입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 아이디입니다.: " + memberId));

        ArticleLike articleLike = new ArticleLike();
        articleLike.setUuidArticle(article);
        articleLike.setMember(member);
        articleLike.setLiked(true); // 좋아요 누름
        articleLikeRepository.save(articleLike);

        // 좋아요 추가 후 좋아요 수 증가
        article.setLikeCount(article.getLikeCount() != null ? article.getLikeCount() + 1 : 1);
        uuidArticleRepository.save(article);
    }


    public void cancelLikeArticle(UUID uuid, Long memberId) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 기사 아이디입니다.: " + uuid));

        // 좋아요를 취소할 경우 해당 회원 ID와 게시글 ID를 기준으로 좋아요를 찾아 삭제
        ArticleLike articleLike = articleLikeRepository.findByUuidArticleIdAndMemberId(article.getId(), memberId);
        if (articleLike == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요가 취소된 기사입니다.");
        }

        articleLikeRepository.delete(articleLike);

        // 좋아요 취소 후 좋아요 수 감소
        article.setLikeCount(article.getLikeCount() != null && article.getLikeCount() > 0 ? article.getLikeCount() - 1 : 0);
        uuidArticleRepository.save(article);
    }


    public LikeCountDto getLikeCount(UUID uuid, Long memberId) {
        UUIDArticle article = uuidArticleRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 기사 아이디입니다.: " + uuid));

        LikeCountDto likeCountDto = entityToDto(article, memberId);
        return likeCountDto;
    }


    public LikeCountDto entityToDto(UUIDArticle uuidArticle, Long memberId) {

        LikeCountDto likeCountDto = new LikeCountDto();
        likeCountDto.setLikeCount(uuidArticle.getLikeCount());
        likeCountDto.setLiked(isMemberLikedArticle(uuidArticle, memberId));
        return likeCountDto;
    }


    public List<UUIDArticleListDto> entityToDto(List<UUIDArticle> uuidArticles) {
        return mapToLikedArticleDto(uuidArticles);
    }

    private List<UUIDArticleListDto> mapToLikedArticleDto(List<UUIDArticle> uuidArticles) {
        List<UUIDArticleListDto> uuidArticleListDtoList = new ArrayList<>();
        for (UUIDArticle uuidArticle : uuidArticles) {
            UUIDArticleListDto uuidArticleListDto = new UUIDArticleListDto();
            uuidArticleListDto.setId(uuidArticle.getId());
            uuidArticleListDto.setTitle(uuidArticle.getTitle());
            uuidArticleListDto.setCreatedDate(uuidArticle.getCreatedDate());
            uuidArticleListDto.setArticleTime(uuidArticle.getArticleTime());
            uuidArticleListDto.setPublisher(uuidArticle.getPublisher());
            uuidArticleListDto.setImage(uuidArticle.getImage());
            uuidArticleListDto.setCategory(uuidArticle.getCategory());
            uuidArticleListDtoList.add(uuidArticleListDto);
        }
        return uuidArticleListDtoList;
    }


    public List<UUIDArticle> getLikedArticlesByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 아이디입니다.: " + memberId));

        return member.getLikedArticles().stream()
                .map(ArticleLike::getUuidArticle)
                .collect(Collectors.toList());
    }


    private boolean isMemberLikedArticle(UUIDArticle article, Long memberId) {
        return article.getLikedByMembers().stream()
                .anyMatch(member -> member.getId().equals(memberId));
    }
}
