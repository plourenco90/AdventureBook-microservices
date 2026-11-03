import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  BookResponse, BookSummary, BookValidationResponse,
  UnreachableSectionsResponse
} from '../models/book.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class BookService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/books`;

  getBooks(): Observable<BookSummary[]> {
    return this.http.get<BookSummary[]>(this.baseUrl);
  }

  getBook(id: number): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${this.baseUrl}/${id}`);
  }

  getValidation(id: number): Observable<BookValidationResponse> {
    return this.http.get<BookValidationResponse>(`${this.baseUrl}/${id}/validation`);
  }

  getUnreachableSections(id: number): Observable<UnreachableSectionsResponse> {
    return this.http.get<UnreachableSectionsResponse>(
      `${this.baseUrl}/${id}/unreachable-sections`
    );
  }
}
