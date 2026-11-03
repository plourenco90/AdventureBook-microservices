import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AdventureService } from '../../core/services/adventure.service';
import { AdventureState } from '../../core/models/adventure.model';

@Component({
  selector: 'app-adventure',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="loading" *ngIf="loading">Loading your adventure...</div>
    <div class="notice error" *ngIf="error">{{ error }}</div>

    <ng-container *ngIf="!loading && state">
      <section class="game-header">
        <div>
          <p class="eyebrow">A TUA AVENTURA</p>
          <h1>{{ state.playerName }}</h1>
          <p class="muted">Livro #{{ state.bookId }} · Your story continues</p>
        </div>
        <div class="health-card">
          <div class="health-label"><span>♥</span> VIDA <strong>{{ state.health }} / 10</strong></div>
          <div class="health-track">
            <div class="health-fill" [style.width.%]="healthPercent"
                 [class.low-health]="state.health <= 3"></div>
          </div>
        </div>
      </section>

      <div class="consequence" *ngIf="state.consequenceMessage">
        <span>✦</span> {{ state.consequenceMessage }}
      </div>

      <section class="story-card" *ngIf="isPlaying">
        <div class="story-meta">
          <span class="section-number">SECÇÃO {{ state.currentSection?.id }}</span>
          <span class="status valid"><span class="status-dot"></span> Em aventura</span>
        </div>
        <div class="story-text">{{ state.currentSection?.text }}</div>

        <ng-container *ngIf="state.currentSection?.choices?.length">
          <div class="choice-heading">
            <p class="eyebrow">YOUR CHOICE</p>
            <h2>What will you do?</h2>
          </div>
          <div class="choice-list">
            <button class="choice-button"
                    *ngFor="let choice of state.currentSection?.choices"
                    (click)="choose(choice.index)"
                    [disabled]="choosing">
              <span class="choice-index">{{ choice.index + 1 }}</span>
              <span>{{ choice.description }}</span>
              <span class="choice-arrow">→</span>
            </button>
          </div>
        </ng-container>
        <div class="notice" *ngIf="!state.currentSection?.choices?.length">
          This section doesn't have any choices.
        </div>
      </section>

      <section class="ending-card won" *ngIf="isWon">
        <div class="ending-symbol">✦</div>
        <p class="eyebrow">END OF ADVENTURE</p>
        <h1>You did it, {{ state.playerName }}!</h1>
        <p>Your journey comes to an end. Thank you for playing.</p>
        <p class="final-health">Remaining health: <strong>{{ state.health }} HP</strong></p>
        <a class="button button-primary" routerLink="/books">Choose another book</a>
      </section>

      <section class="ending-card dead" *ngIf="isDead">
        <div class="ending-symbol">☠</div>
        <p class="eyebrow">END OF ADVENTURE</p>
        <h1>Your journey ended.</h1>
        <p>{{ state.playerName }}, your health points reached zero.</p>
        <p class="final-health">Remaining health: <strong>{{ state.health }} HP</strong></p>
        <a class="button button-primary" routerLink="/books">Try another adventure</a>
      </section>

      <div class="loading" *ngIf="choosing">Applying consequences of your choice...</div>
    </ng-container>
  `
})
export class AdventureComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly adventureService = inject(AdventureService);

  state: AdventureState | null = null;
  loading = true;
  choosing = false;
  error = '';

  get healthPercent(): number {
    return Math.max(0, Math.min(100, (this.state?.health ?? 0) * 10));
  }

  private get normalizedStatus(): string {
    return (this.state?.status ?? '').toUpperCase();
  }

  get isDead(): boolean {
    const status = this.normalizedStatus;
    return status.includes('DEAD') || status.includes('DEATH') ||
      status.includes('DIED') || status.includes('LOST') ||
      (this.state?.health ?? 10) <= 0;
  }

  get isWon(): boolean {
    const status = this.normalizedStatus;
    return !this.isDead && (
      status.includes('WON') || status.includes('WIN') ||
      status.includes('COMPLETED') || status.includes('FINISHED') ||
      status.includes('SUCCESS') || status.includes('VICTORY') ||
      (this.state?.currentSection?.type ?? '').toUpperCase().includes('ENDING')
    );
  }

  get isPlaying(): boolean {
    return !!this.state && !this.isDead && !this.isWon;
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isInteger(id) || id <= 0) {
      this.router.navigate(['/books']);
      return;
    }
    this.adventureService.getState(id).subscribe({
      next: state => { this.state = state; this.loading = false; },
      error: () => {
        this.error = 'Not possible to load the adventure.';
        this.loading = false;
      }
    });
  }

  choose(optionIndex: number): void {
    if (!this.state || this.choosing || !this.isPlaying) return;
    this.choosing = true;
    this.error = '';
    this.adventureService.makeChoice(this.state.id, { optionIndex }).subscribe({
      next: state => {
        this.state = state;
        this.choosing = false;
      },
      error: () => {
        this.error = 'Not possible to register the choice, try again.';
        this.choosing = false;
      }
    });
  }
}
