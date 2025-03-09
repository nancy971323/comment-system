package org.lidiannan.demo.controller;

import lombok.RequiredArgsConstructor;
import org.lidiannan.demo.dto.CommentRequest;
import org.lidiannan.demo.dto.CommentResponse;
import org.lidiannan.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request, Principal principal) {
        return ResponseEntity.ok(commentService.createComment(request, principal));
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentResponse> likeComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.likeComment(commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId, Principal principal) {
        try {
            commentService.deleteComment(commentId, principal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Comment deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}