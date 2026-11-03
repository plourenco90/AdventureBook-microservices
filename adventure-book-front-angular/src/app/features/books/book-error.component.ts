import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { BookService } from '../../core/services/book.service';
import { BookValidationResponse } from '../../core/models/book.model';

@Component({
  selector: 'app-book-error',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <a class="back-link" routerLink="/books">← Voltar à biblioteca</a>
    <section class="error-page">
      <div class="error-icon">!</div>
      <p class="eyebrow">VALIDAÇÃO DO LIVRO</p>
      <h1>This adventure is not ready.</h1>
      <p class="muted">Book has validation issues and cannot be started.</p>

      <div class="panel validation-panel" *ngIf="validation">
        <h2>Errors found</h2>
        <div class="validation-error" *ngFor="let item of validation.errors">
          <div class="error-code">{{ item.code }}</div>
          <div>
            <strong>{{ item.message }}</strong>
            <p *ngIf="item.sectionId !== null && item.sectionId !== undefined">
              Secção {{ item.sectionId }}
            </p>
          </div>
        </div>
        <p *ngIf="validation.errors.length === 0" class="muted">
          The book is marked as invalid but there are no error details.
        </p>
      </div>

      <div class="loading" *ngIf="loading">Loading validation errors...</div>
      <div class="notice error" *ngIf="error">{{ error }}</div>
      <a class="button button-primary" routerLink="/books">Choose another book</a>
    </section>
  `
})
export class BookErrorComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly bookService = inject(BookService);
  validation: BookValidationResponse | null = null;
  loading = true;
  error = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.bookService.getValidation(id).subscribe({
      next: result => { this.validation = result; this.loading = false; },
      error: () => {
        this.error = 'Unable to load errors of this book.';
        this.loading = false;
      }
    });
  }
}
