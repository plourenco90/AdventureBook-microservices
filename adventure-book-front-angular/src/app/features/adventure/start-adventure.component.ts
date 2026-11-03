import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AdventureService } from '../../core/services/adventure.service';
import { BookService } from '../../core/services/book.service';
import { BookResponse } from '../../core/models/book.model';

@Component({
  selector: 'app-start-adventure',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <a class="back-link" routerLink="/books">← Back to library</a>
    <section class="start-layout">
      <div class="start-art"><span>✧</span><p>YOUR STORY<br>STARTS HERE</p></div>
      <div class="start-content">
        <p class="eyebrow">BEFORE START</p>
        <h1>Who is the hero of the story?</h1>
        <p class="muted" *ngIf="book">Estás prestes a entrar em <strong>{{ book.title }}</strong>.</p>
        <form [formGroup]="form" (ngSubmit)="start()">
          <label for="playerName">Player name</label>
          <input id="playerName" type="text" formControlName="playerName"
                 placeholder="Write your name" maxlength="80">
          <small class="field-error" *ngIf="form.controls.playerName.touched && form.controls.playerName.invalid">
            Enter a name (max 80 characters)
          </small>
          <div class="notice error" *ngIf="error">{{ error }}</div>
          <button class="button button-primary full-width" type="submit" [disabled]="form.invalid || submitting">
            {{ submitting ? 'Preparing adventure...' : 'Start playing →' }}
          </button>
        </form>
      </div>
    </section>
  `
})
export class StartAdventureComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly adventureService = inject(AdventureService);
  private readonly bookService = inject(BookService);

  readonly bookId = Number(this.route.snapshot.paramMap.get('id'));
  book: BookResponse | null = null;
  submitting = false;
  error = '';

  form = this.fb.nonNullable.group({
    playerName: ['', [Validators.required, Validators.maxLength(80)]]
  });

  ngOnInit(): void {
    if (!Number.isInteger(this.bookId) || this.bookId <= 0) {
      this.router.navigate(['/books']);
      return;
    }
    this.bookService.getValidation(this.bookId).subscribe({
      next: validation => {
        if (!validation.valid) {
          this.router.navigate(['/books', this.bookId, 'error']);
          return;
        }
        this.bookService.getBook(this.bookId).subscribe({
          next: book => this.book = book,
          error: () => this.error = 'Not possible to load book.'
        });
      },
      error: () => this.error = 'Not possible to validate book.'
    });
  }

  start(): void {
    if (this.form.invalid || this.submitting) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.error = '';
    this.adventureService.start({
      bookId: this.bookId,
      playerName: this.form.getRawValue().playerName.trim()
    }).subscribe({
      next: state => this.router.navigate(['/adventures', state.id]),
      error: () => {
        this.error = 'Not possible to start the adventure. Try again.';
        this.submitting = false;
      }
    });
  }
}
