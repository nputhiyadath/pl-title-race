import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly BASE_URL = 'http://localhost:8080/api';

  constructor(protected http: HttpClient) {}

  protected get<T>(url: string) {
    return this.http.get<T>(`${this.BASE_URL}${url}`);
  }

  protected post<T>(url: string, body: any) {
    return this.http.post<T>(`${this.BASE_URL}${url}`, body);
  }
}
