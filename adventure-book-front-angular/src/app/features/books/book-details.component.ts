import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { BookService } from '../../core/services/book.service';
import { BookResponse, BookValidationResponse, UnreachableSectionsResponse } from '../../core/models/book.model';

@Component({
  selector: 'app-book-details',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <a class="back-link" routerLink="/books">← Back to library</a>
    <div class="loading" *ngIf="loading">Loading Book...</div>
    <div class="notice error" *ngIf="error">{{ error }}</div>

    <ng-container *ngIf="!loading && book && validation?.valid">
      <section class="detail-hero">
        <div class="detail-cover"><span>✧</span><small>ADVENTURE BOOK · {{ book.id }}</small></div>
        <div class="detail-info">
          <span class="status valid"><span class="status-dot"></span> Valid book</span>
          <p class="eyebrow">YOUR NEXT ADVENTURE</p>
          <h1>{{ book.title }}</h1>
          <p class="author">por {{ book.author }}</p>
          <a class="button button-primary" [routerLink]="['/books', book.id, 'start']">
            Start adventure <span>→</span>
          </a>
        </div>
      </section>

      <section class="panel">
        <div class="section-heading compact">
          <div><p class="eyebrow">BOOK ANALYSIS</p><h2>Unreachable Sections</h2></div>
          <span class="count">{{ unreachable?.unreachableSectionIds?.length ?? 0 }}</span>
        </div>
        <p class="muted" *ngIf="!unreachable?.unreachableSectionIds?.length">
          Great! All sections are reachable from the start.
        </p>
        <ng-container *ngIf="unreachable?.unreachableSectionIds?.length">
          <p class="muted">This sections have no path from the beginning section:</p>
          <div class="tag-list">
            <span class="tag" *ngFor="let sectionId of unreachable?.unreachableSectionIds">
              Section {{ sectionId }}
            </span>
          </div>
        </ng-container>
      </section>
    </ng-container>
  `
})
export class BookDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly bookService = inject(BookService);

  book: BookResponse | null = null;
  validation: BookValidationResponse | null = null;
  unreachable: UnreachableSectionsResponse | null = null;
  loading = true;
  error = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.router.navigate(['/books']);
      return;
    }

    this.bookService.getValidation(id).subscribe({
      next: validation => {
        this.validation = validation;
        if (!validation.valid) {
          this.router.navigate(['/books', id, 'error']);
          return;
        }

        forkJoin({
          book: this.bookService.getBook(id),
          unreachable: this.bookService.getUnreachableSections(id)
        }).subscribe({
          next: result => {
            this.book = result.book;
            this.unreachable = result.unreachable;
            this.loading = false;
          },
          error: () => {
            this.error = 'Não foi possível obter os detalhes deste livro.';
            this.loading = false;
          }
        });
      },
      error: () => {
        this.error = 'Não foi possível verificar a validade deste livro.';
        this.loading = false;
      }
    });
  }
}
