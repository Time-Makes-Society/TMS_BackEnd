package com.project.tms.service;

import com.project.tms.domain.Comment;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.CommentDto;
import com.project.tms.repository.CommentRepository;
import com.project.tms.repository.MemberRepository;
import com.project.tms.web.login.SessionConst;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    private CommentDto entityToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        BeanUtils.copyProperties(comment, dto);
        dto.setArticleId(comment.getArticle().getId());
        dto.setArticleTitle(comment.getArticle().getTitle());
        dto.setUserId(comment.getUser().getId());
        dto.setMemberName(comment.getUser().getMemberName());
        // Set createdAt field using the commentCreatedDate from Comment entity
        // Format commentCreatedDate as a string in the desired pattern
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

    public List<CommentDto> getCommentsByArticle(UUIDArticle articleId) {
        List<Comment> comments = commentRepository.findByArticle(articleId);
        return comments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public Comment updateComment(UUIDArticle articleId, Long commentId, Comment updatedComment) {
        Optional<Comment> optionalComment = commentRepository.findByIdAndArticleId(commentId, articleId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(updatedComment.getContent());

            return commentRepository.save(comment);
        } else {
            return null;
        }
    }


    public void deleteComment(UUIDArticle articleId, Long commentId) {
        commentRepository.deleteByIdAndArticleId(commentId, articleId);
    }

}
