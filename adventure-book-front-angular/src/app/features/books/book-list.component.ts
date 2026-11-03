import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BookService } from '../../core/services/book.service';
import { BookSummary } from '../../core/models/book.model';

@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <section class="hero">
      <p class="eyebrow">A WORLD OF CHOICES</p>
      <h1>Chose your <span>adventure.</span></h1>
    </section>

    <section class="section-heading">
      <div>
        <p class="eyebrow">LIBRARY</p>
        <h2>All Books</h2>
      </div>
      <span class="count" *ngIf="!loading">{{ books.length }} books</span>
    </section>

    <div class="notice error" *ngIf="error">{{ error }}</div>
    <div class="loading" *ngIf="loading">Loading books...</div>

    <div class="empty-state" *ngIf="!loading && !error && books.length === 0">
      There are no books available
    </div>

    <div class="book-grid" *ngIf="!loading && books.length">
      <article class="book-card" *ngFor="let book of books">
        <div class="book-art">
          <span class="book-symbol">✧</span>
          <span class="book-id">BOOK Nº {{ book.id }}</span>
        </div>
        <div class="book-content">
          <div class="card-topline">
            <span class="status" [class.valid]="book.valid" [class.invalid]="!book.valid">
              <span class="status-dot"></span>
              {{ book.valid ? 'Valid' : 'Invalid' }}
            </span>
          </div>
          <h3>{{ book.title }}</h3>
          <p class="author">por {{ book.author || 'Unnkown Autor' }}</p>
          <a class="button" [routerLink]="['/books', book.id]">
            {{ book.valid ? 'See Book' : 'See Errors' }}
            <span>→</span>
          </a>
        </div>
      </article>
    </div>
  `
})
export class BookListComponent implements OnInit {
  private readonly bookService = inject(BookService);
  books: BookSummary[] = [];
  loading = true;
  error = '';

  ngOnInit(): void {
    this.bookService.getBooks().subscribe({
      next: books => { this.books = books; this.loading = false; },
      error: () => {
        this.error = 'Unable to load books, is the backend running?';
        this.loading = false;
      }
    });
  }
}
