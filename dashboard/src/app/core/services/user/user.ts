import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class User {
  private apiUrl = 'api/auth';

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
  ) {
  }

  register(request: CreateUserRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/register`, request);
  }


  login(request: LoginRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/login`, request, {
      responseType: 'text'
    });
  }
}

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
  createdAt: Date;
  updatedAt: Date;
}
