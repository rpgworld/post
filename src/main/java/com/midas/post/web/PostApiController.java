package com.midas.post.web;

import com.midas.post.service.PostService;
import com.midas.post.web.dto.PostSaveRequestDto;
import com.midas.post.web.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostApiController {
    
    private final PostService postService;

    @PostMapping("/api/v1/post")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }
    
    @PutMapping("/api/v1/post/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(id, requestDto);
    }
}
