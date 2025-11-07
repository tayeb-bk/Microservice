import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PostService, Post } from '../../services/post.service'; // ✅ chemin corrigé

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  newPost: Post = { title: '', content: '', author: '', imageUrl: '' };
  loading = true;
  error: string | null = null;

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  /** Charger la liste des posts */
  loadPosts(): void {
    this.loading = true;
    this.postService.getPosts().subscribe({
      next: (data: Post[]) => {   // ✅ typé
        this.posts = data;
        this.loading = false;
      },
      error: (err: any) => {      // ✅ typé
        this.error = 'Erreur lors du chargement des posts';
        this.loading = false;
        console.error(err);
      }
    });
  }

  /** Créer un nouveau post */
  createPost(): void {
    if (!this.newPost.title || !this.newPost.content || !this.newPost.author) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    this.postService.createPost(this.newPost).subscribe({
      next: (createdPost: Post) => {   // ✅ typé
        this.posts.unshift(createdPost);
        this.newPost = { title: '', content: '', author: '', imageUrl: '' };
      },
      error: (err: any) => {           // ✅ typé
        console.error('Erreur lors de la création du post', err);
      }
    });
  }

  /** Supprimer un post */
  deletePost(id: number | undefined): void {
    if (!id) return;
    if (confirm('Voulez-vous vraiment supprimer ce post ?')) {
      this.postService.deletePost(id).subscribe({
        next: () => {
          this.posts = this.posts.filter((p) => p.id !== id);
        },
        error: (err: any) => {         // ✅ typé
          console.error('Erreur lors de la suppression du post', err);
        }
      });
    }
  }

  /** Gérer les erreurs de chargement d'image */
  handleImageError(event: any): void {
    event.target.style.display = 'none';
    const placeholder = event.target.parentElement.querySelector('.placeholder-image');
    if (placeholder) {
      placeholder.style.display = 'flex';
    } else {
      const newPlaceholder = document.createElement('div');
      newPlaceholder.className = 'placeholder-image';
      newPlaceholder.innerHTML = `
      <span class="placeholder-icon">🖼️</span>
      <p class="mb-0 small">Image non disponible</p>
    `;
      event.target.parentElement.appendChild(newPlaceholder);
    }
  }
}
