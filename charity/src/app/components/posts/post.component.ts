import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PostService, Post } from '../../services/post.service';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  filteredPosts: Post[] = []; // Posts filtrés
  newPost: Post = { title: '', content: '', author: '', imageUrl: '' };
  editingPost: Post | null = null; // Post en cours d'édition
  isEditing = false; // Mode édition activé
  loading = true;
  error: string | null = null;

  // Filtres
  searchTerm = '';
  selectedAuthor = '';
  sortBy: 'date-desc' | 'date-asc' | 'title-asc' | 'title-desc' = 'date-desc';

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  /** Obtenir la liste des auteurs uniques */
  get uniqueAuthors(): string[] {
    const authors = this.posts.map(p => p.author);
    return [...new Set(authors)].sort();
  }

  /** Charger la liste des posts */
  loadPosts(): void {
    this.loading = true;
    this.postService.getPosts().subscribe({
      next: (data: Post[]) => {
        this.posts = data;
        this.applyFilters(); // Appliquer les filtres
        this.loading = false;
      },
      error: (err: any) => {
        this.error = 'Erreur lors du chargement des posts';
        this.loading = false;
        console.error(err);
      }
    });
  }

  /** Appliquer tous les filtres */
  applyFilters(): void {
    let filtered = [...this.posts];

    // Filtre de recherche (titre, contenu, auteur)
    if (this.searchTerm.trim()) {
      const search = this.searchTerm.toLowerCase();
      filtered = filtered.filter(post =>
        post.title.toLowerCase().includes(search) ||
        post.content.toLowerCase().includes(search) ||
        post.author.toLowerCase().includes(search)
      );
    }

    // Filtre par auteur
    if (this.selectedAuthor) {
      filtered = filtered.filter(post => post.author === this.selectedAuthor);
    }

    // Tri
    filtered.sort((a, b) => {
      switch (this.sortBy) {
        case 'date-desc':
          return new Date(b.createdAt || '').getTime() - new Date(a.createdAt || '').getTime();
        case 'date-asc':
          return new Date(a.createdAt || '').getTime() - new Date(b.createdAt || '').getTime();
        case 'title-asc':
          return a.title.localeCompare(b.title);
        case 'title-desc':
          return b.title.localeCompare(a.title);
        default:
          return 0;
      }
    });

    this.filteredPosts = filtered;
  }

  /** Réinitialiser tous les filtres */
  resetFilters(): void {
    this.searchTerm = '';
    this.selectedAuthor = '';
    this.sortBy = 'date-desc';
    this.applyFilters();
  }

  /** Créer un nouveau post */
  createPost(): void {
    if (!this.newPost.title || !this.newPost.content || !this.newPost.author) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    this.postService.createPost(this.newPost).subscribe({
      next: (createdPost: Post) => {
        this.posts.unshift(createdPost);
        this.applyFilters(); // Réappliquer les filtres
        this.newPost = { title: '', content: '', author: '', imageUrl: '' };
      },
      error: (err: any) => {
        console.error('Erreur lors de la création du post', err);
      }
    });
  }

  /** Activer le mode édition pour un post */
  startEdit(post: Post): void {
    this.isEditing = true;
    this.editingPost = { ...post }; // Copie du post pour édition
    // Scroll vers le formulaire
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  /** Sauvegarder les modifications */
  saveEdit(): void {
    if (!this.editingPost || !this.editingPost.id) return;

    if (!this.editingPost.title || !this.editingPost.content || !this.editingPost.author) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    this.postService.updatePost(this.editingPost.id, this.editingPost).subscribe({
      next: (updatedPost: Post) => {
        const index = this.posts.findIndex(p => p.id === this.editingPost!.id);
        if (index !== -1) {
          this.posts[index] = updatedPost;
        }
        this.applyFilters(); // Réappliquer les filtres
        this.cancelEdit();
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour du post', err);
        alert('Erreur lors de la mise à jour du post');
      }
    });
  }

  /** Annuler l'édition */
  cancelEdit(): void {
    this.isEditing = false;
    this.editingPost = null;
  }

  /** Supprimer un post */
  deletePost(id: number | undefined): void {
    if (!id) return;
    if (confirm('Voulez-vous vraiment supprimer ce post ?')) {
      this.postService.deletePost(id).subscribe({
        next: () => {
          this.posts = this.posts.filter((p) => p.id !== id);
          this.applyFilters(); // Réappliquer les filtres
        },
        error: (err: any) => {
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
