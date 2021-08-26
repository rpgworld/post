package com.midas.post.domain.auth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        memberRepository.deleteAll();;
    }
    
    @Test
    public void 멤버만들기() throws Exception {
        // given
        String username = "member1";
        String email = "member1@naver.com";
        
        memberRepository.save(Member.builder()
                        .username(username)
                        .email(email)
                        .build());

        // when
        List<Member> findMember = memberRepository.findByUsername("member1");

        // then
        Member member = findMember.get(0);
        assertThat(member.getUsername()).isEqualTo(username);
        assertThat(member.getEmail()).isEqualTo(email);

    }
}