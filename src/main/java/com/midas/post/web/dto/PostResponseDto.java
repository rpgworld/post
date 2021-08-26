package com.midas.post.web.dto;

import com.midas.post.domain.post.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String username;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.username = entity.getMember().getUsername();
    }
}
