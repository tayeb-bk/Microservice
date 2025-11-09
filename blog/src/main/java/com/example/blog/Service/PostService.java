package com.example.blog.Service;

import com.example.blog.Entity.Post;
import com.example.blog.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    public Post createPost(Post post) {
        return repository.save(post);
    }

    public void deletePost(Long id) {
        repository.deleteById(id);
    }
}

