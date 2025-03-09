package org.lidiannan.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentRequest {
    @NotBlank
    @Size(min = 1, max = 500)
    private String content;
    
    // 若為回覆留言，則需要設定父留言ID
    private Long parentId;
}