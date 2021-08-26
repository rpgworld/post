package com.midas.post.domain.auth;

import com.midas.post.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String email;

    @OneToMany(mappedBy = "member")
    List<Post> posts = new ArrayList<>();

    @Builder
    public Member(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Builder
    public Member(String username, String email, List<Post> posts) {
        this.username = username;
        this.email = email;
        this.posts = posts;
    }
}
