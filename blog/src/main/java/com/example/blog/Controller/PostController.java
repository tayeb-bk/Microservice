package com.example.blog.Controller;

import com.example.blog.Entity.Post;
import com.example.blog.Service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public List<Post> getAll() {
        return service.getAllPosts();
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return service.createPost(post);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePost(id);
    }
    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody Post post) {
        return service.updatePost(id, post);
    }

}