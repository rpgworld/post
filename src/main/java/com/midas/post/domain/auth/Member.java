package com.midas.post.domain.auth;

import com.midas.post.domain.posts.Post;
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

    @OneToMany(mappedBy = "post")
    List<Post> posts = new ArrayList<>();
}
