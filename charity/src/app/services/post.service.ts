// src/app/services/post.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';  // Import the current env


export interface Post {
  id?: number;
  title: string;
  content: string;
  author: string;
  createdAt?: string;
  imageUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class PostService {
  private blogServiceUrl = 'http://localhost:8989/api/posts';
  //private blogServiceUrl = `${environment.blogServiceUrl}/posts`;

  constructor(private http: HttpClient) {}

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.blogServiceUrl);
  }

  createPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.blogServiceUrl, post);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.blogServiceUrl}/${id}`);
  }

  updatePost(id: number, post: Post): Observable<Post> {
    return this.http.put<Post>(`${this.blogServiceUrl}/${id}`, post);
  }

}
