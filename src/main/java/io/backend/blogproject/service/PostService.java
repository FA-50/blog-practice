package io.backend.blogproject.service;

import io.backend.blogproject.repository.CategoryRepository;
import io.backend.blogproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
}
