package com.midas.post.service;

import com.midas.post.domain.auth.Member;
import com.midas.post.domain.auth.MemberRepository;
import com.midas.post.domain.post.PostRepository;
import com.midas.post.web.dto.PostSaveRequestDto;
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
            throw new IllegalStateException("존재하지 않는 회원입니다. member_id: " + requestDto.getMemberId());
        }

        return postRepository.save(requestDto.toEntity(findMember.get())).getId();
    }
}
