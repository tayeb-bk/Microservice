package com.example.blog.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "poste")
public class Post {


    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    private String author;

    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(length = 2000)
    private String imageUrl; // ✅ chemin ou URL de l'image




}
