import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  AdventureState, MakeChoiceRequest, StartAdventureRequest
} from '../models/adventure.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AdventureService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/adventures`;

  start(request: StartAdventureRequest): Observable<AdventureState> {
    return this.http.post<AdventureState>(this.baseUrl, request);
  }

  getState(id: number): Observable<AdventureState> {
    return this.http.get<AdventureState>(`${this.baseUrl}/${id}`);
  }

  makeChoice(id: number, request: MakeChoiceRequest): Observable<AdventureState> {
    return this.http.post<AdventureState>(`${this.baseUrl}/${id}/choices`, request);
  }
}
