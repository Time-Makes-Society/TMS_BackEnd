package com.project.tms.controller;

import com.project.tms.domain.Comment;
import com.project.tms.domain.Member;
import com.project.tms.domain.UUIDArticle;
import com.project.tms.dto.CommentDto;
import com.project.tms.dto.MemberDto;
import com.project.tms.service.CommentService;
import com.project.tms.web.login.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<Object> updateComment(@PathVariable("articleId") UUIDArticle articleId,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody Comment updatedComment) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                // 현재 로그인된 사용자 정보를 가져옴

                // 댓글의 작성자 정보를 가져옴
                CommentDto commentDto = commentService.getCommentDtoById(commentId);
                if (commentDto != null && commentDto.getUserId().equals(loginMember.getId())) {
                    // 현재 로그인된 사용자와 댓글의 작성자가 동일한 경우에만 수정을 허용
                    CommentDto updatedCommentDto = commentService.updateComment(articleId, commentId, updatedComment);
                    if (updatedCommentDto != null) {
                        return ResponseEntity.ok(updatedCommentDto);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글이 존재하지 않아 수정할 수 없습니다.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인만 댓글을 수정할 수 있습니다.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
    }


    @DeleteMapping("/{articleId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("articleId") UUIDArticle articleId,
                                                @PathVariable("commentId") Long commentId) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

            if (loginMember != null) {
                // 댓글 작성자의 ID와 현재 로그인한 사용자의 ID 비교
                CommentDto commentDto = commentService.getCommentDtoById(commentId);
                if (commentDto != null && commentDto.getUserId().equals(loginMember.getId())) {
                    // 본인의 댓글인 경우에만 삭제
                    commentService.deleteComment(articleId, commentId);
                    return ResponseEntity.ok("댓글 삭제가 완료되었습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인만 댓글 삭제를 할 수 있습니다.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용해주세요.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("세션이 만료되었습니다. 다시 로그인해주세요.");
    }
}
