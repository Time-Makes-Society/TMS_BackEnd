package com.project.tms.service;


import com.project.tms.domain.Comment;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.CommentDto;
import com.project.tms.repository.CommentRepository;
import com.project.tms.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {


    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;

    private CommentDto entityToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        BeanUtils.copyProperties(comment, dto);
        dto.setArticleId(comment.getArticle().getId());
        dto.setArticleTitle(comment.getArticle().getTitle());
        dto.setUserId(comment.getUser().getId());
        dto.setMemberName(comment.getUser().getMemberName());
        dto.setMemberNickname(comment.getUser().getMemberNickname());
        dto.setCommentCreatedDate(comment.getCommentCreatedDate()
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")));

        return dto;
    }

    public CommentDto addCommentToArticle(UUIDArticle articleId, Comment comment, Long userId) {
        Member user = memberRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        comment.setArticle(articleId);
        comment.setUser(user);
        commentRepository.save(comment);
        return entityToDto(comment);
    }

    public List<CommentDto> getCommentsByArticle(UUIDArticle article) {
        List<Comment> comments = commentRepository.findByArticle(article);
        return comments.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public CommentDto getCommentDtoById(Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            return entityToDto(commentOpt.get());
        } else {
            return null;
        }
    }


    public CommentDto updateComment(UUIDArticle articleId, Long commentId, Comment updatedComment) {
        Comment comment = commentRepository.findByIdAndArticleId(commentId, articleId).orElse(null);

        if (comment != null) {
            comment.setContent(updatedComment.getContent());

            // 현재 시간을 최신 시간으로 업데이트
            LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            comment.setCommentCreatedDate(currentTime);

            CommentDto updatedCommentDto = entityToDto(commentRepository.save(comment));
            return updatedCommentDto;
        } else {
            return null; // 댓글이 존재하지 않는 경우 null 반환 또는 다른 처리를 수행할 수 있음
        }
    }

    @Transactional
    public void deleteComment(UUIDArticle articleId, Long commentId) {
        commentRepository.deleteByIdAndArticleId(commentId, articleId);
    }
}