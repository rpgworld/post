package com.midas.post.web;

import com.midas.post.domain.auth.Member;
import com.midas.post.domain.auth.MemberRepository;
import com.midas.post.domain.post.Post;
import com.midas.post.domain.post.PostRepository;
import com.midas.post.web.dto.PostSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;

    @Autowired private PostRepository postRepository;
    @Autowired private MemberRepository memberRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postRepository.deleteAll();
        memberRepository.deleteAll();
    }
    
    @Test
    public void Post_등록하자() throws Exception {
        // given
        Member member = new Member("dooly", "email");
        memberRepository.save(member);

        String title = "title";
        String content = "content";

        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .memberId(member.getId())
                .build();

        String url = "http://localhost:" + port + "/api/v1/post";
        
        // when
        ResponseEntity<Long> responseEntity = restTemplate
                .postForEntity(url, requestDto, Long.class);
        
        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);

        System.out.println("responseEntity.getBody() : " + responseEntity.getBody());
        System.out.println("responseEntity.getBody().toString() : " + responseEntity.getBody().toString());
        System.out.println("postId : " + posts.get(0).getId());
        System.out.println("post : " + posts.get(0));
    }
}