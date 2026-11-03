import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'books', pathMatch: 'full' },
  {
    path: 'books',
    loadComponent: () => import('./features/books/book-list.component')
      .then(m => m.BookListComponent)
  },
  {
    path: 'books/:id',
    loadComponent: () => import('./features/books/book-details.component')
      .then(m => m.BookDetailsComponent)
  },
  {
    path: 'books/:id/error',
    loadComponent: () => import('./features/books/book-error.component')
      .then(m => m.BookErrorComponent)
  },
  {
    path: 'books/:id/start',
    loadComponent: () => import('./features/adventure/start-adventure.component')
      .then(m => m.StartAdventureComponent)
  },
  {
    path: 'adventures/:id',
    loadComponent: () => import('./features/adventure/adventure.component')
      .then(m => m.AdventureComponent)
  },
  { path: '**', redirectTo: 'books' }
];
