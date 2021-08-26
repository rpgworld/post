package com.midas.post.web.dto;

import com.midas.post.domain.auth.Member;
import com.midas.post.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostSaveRequestDto {
    private String title;
    private String content;
    private Long memberId;

    @Builder
    public PostSaveRequestDto(String title, String content, Long memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
