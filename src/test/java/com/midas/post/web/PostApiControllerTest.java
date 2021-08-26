package com.midas.post.web;

import com.midas.post.domain.auth.Member;
import com.midas.post.domain.auth.MemberRepository;
import com.midas.post.domain.post.Post;
import com.midas.post.domain.post.PostRepository;
import com.midas.post.service.PostService;
import com.midas.post.web.dto.PostSaveRequestDto;
import com.midas.post.web.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

    @Autowired private PostService postService;

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
    
    @Test
    public void Post_수정된다() throws Exception {
        // given
        Member member = new Member("dooly", "dooly@naver.com");

        memberRepository.save(member);

        String title = "title";
        String content = "content";

        Post savedPost = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build());

        Long updateId = savedPost.getId();
        String updateTitle = "title2";
        String updateContent = "content2";

        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .memberId(member.getId())
                .build();

        String url = "http://localhost:" + port + "/api/v1/post/" + updateId;

        HttpEntity<PostUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
        
        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> posts = postRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(updateTitle);
        assertThat(posts.get(0).getContent()).isEqualTo(updateContent);

        System.out.println("updated Post: " + posts.get(0));
    }

    @Test
    public void Post_수정오류_게시물없음() throws Exception {
        // given
        Member member = new Member("dooly", "dooly@naver.com");
        memberRepository.save(member);

        Post savedPost = postRepository.save(Post.builder()
                        .title("title")
                        .content("content")
                        .member(member)
                .build());

        // when
        Long updateId = -1L; // expect error
        String updateTitle = "title2";
        String updateContent = "content2";

        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .build();

        // then
        assertThatThrownBy(() -> {
            postService.update(updateId, requestDto);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void Post_수정오류_작성자아님() throws Exception {
        // given
        Member member = new Member("dooly", "dooly@naver.com");
        memberRepository.save(member);

        Post savedPost = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .member(member)
                .build());

        // when
        Long updateId = savedPost.getId();
        String updateTitle = "title2";
        String updateContent = "content2";

        PostUpdateRequestDto requestDto = PostUpdateRequestDto.builder()
                .title(updateTitle)
                .content(updateContent)
                .memberId(-1L) // expect error
                .build();

        // then
        assertThatThrownBy(() -> {
            postService.update(updateId, requestDto);
        }).isInstanceOf(IllegalStateException.class);
    }
}