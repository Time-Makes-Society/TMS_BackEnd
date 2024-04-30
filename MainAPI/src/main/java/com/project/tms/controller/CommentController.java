package com.project.tms.controller;

import com.project.tms.domain.Comment;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.CommentDto;
import com.project.tms.service.CommentService;
import com.project.tms.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/{articleId}")
    public ResponseEntity<String> addCommentToArticle(@PathVariable("articleId") UUIDArticle articleId,
                                                      @RequestBody Comment comment) {
        try {
            Long userId = ((Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER)).getId();
            commentService.addCommentToArticle(articleId, comment, userId);
            return ResponseEntity.ok("댓글 작성이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("댓글 작성 중 오류가 발생했습니다: " + e.toString());
        }
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<List<CommentDto>> getCommentsByArticle(@PathVariable("articleId") UUIDArticle articleId) {
        List<CommentDto> comments = commentService.getCommentsByArticle(articleId);
        if (!comments.isEmpty()) {
            return ResponseEntity.ok(comments);
        } else { // 204 No Content 반환
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{articleId}/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("articleId") UUIDArticle articleId,
                                                    @PathVariable("commentId") Long commentId,
                                                    @RequestBody Comment updatedComment) {
        CommentDto updatedCommentDto = commentService.updateComment(articleId, commentId, updatedComment);
        if (updatedCommentDto != null) {
            return ResponseEntity.ok(updatedCommentDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{articleId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("articleId") UUIDArticle articleId,
                                                @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(articleId, commentId);
        return ResponseEntity.ok("댓글 삭제가 완료되었습니다.");
    }

}
