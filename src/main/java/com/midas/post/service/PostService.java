package com.midas.post.service;

import com.midas.post.domain.auth.Member;
import com.midas.post.domain.auth.MemberRepository;
import com.midas.post.domain.post.Post;
import com.midas.post.domain.post.PostRepository;
import com.midas.post.web.dto.PostResponseDto;
import com.midas.post.web.dto.PostSaveRequestDto;
import com.midas.post.web.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(PostSaveRequestDto requestDto) {

        Optional<Member> findMember = memberRepository.findById(requestDto.getMemberId());

        if (findMember.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 회원입니다. id: " + requestDto.getMemberId());
        }

        return postRepository.save(requestDto.toEntity(findMember.get())).getId();
    }

    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto) {
        // 게시물이 존재하는지 체크
        Optional<Post> findPost = postRepository.findById(id);
        if (findPost.isEmpty()) {
            throw new IllegalStateException("존재하지 않는 게시물입니다. id: " + id);
        }

        // 게시물 작성자와 멤버id가 일치하는지
        Long authorId = findPost.get().getMember().getId();
        if (!(requestDto.getMemberId().equals(authorId))) {
            throw new IllegalStateException("게시물 수정은 작성자만 가능합니다.");
        }

        findPost.get().update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }
}
