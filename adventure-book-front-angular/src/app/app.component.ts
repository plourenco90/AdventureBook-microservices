import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
    <header class="topbar">
      <a class="brand" routerLink="/books">
        <span class="brand-mark">✦</span>
        <span>Adventure<span class="brand-light">Books</span></span>
      </a>
      <a class="nav-link" routerLink="/books">Library</a>
    </header>
    <main class="page-shell">
      <router-outlet />
    </main>
    <footer class="footer">Adventure Books · Interactive reading</footer>
  `
})
export class AppComponent {}
