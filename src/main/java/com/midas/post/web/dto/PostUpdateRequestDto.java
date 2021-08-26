package com.midas.post.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private Long memberId;

    @Builder
    public PostUpdateRequestDto(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}
