import { Component, inject, signal } from '@angular/core';
import { ButtonDirective, ButtonModule } from "primeng/button";
import { Checkbox, CheckboxModule } from "primeng/checkbox";
import { InputText, InputTextModule } from "primeng/inputtext";
import { PasswordModule } from "primeng/password";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {User} from '../../core/services/user/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ButtonDirective,
    Checkbox,
    InputText,
    CommonModule,
    FormsModule,
    ButtonModule,
    CheckboxModule,
    InputTextModule,
    PasswordModule
  ],
  templateUrl: './auth.html',
  styleUrl: './auth.scss',
})
export class Auth {

  private userService = inject(User);
  private router = inject(Router);

  authState = signal<boolean>(true);

  fullName = '';
  username = '';
  email = '';
  password = '';
  checked = false;

  authStateChange() {
    this.authState.update(state => !state);
  }

  onSubmit() {
    if (this.authState()) {
      // Login Logic
      const loginReq = {
        username: this.username,
        password: this.password
      };

      this.userService.login(loginReq).subscribe({
        next: (response) => {
          // Response is text "Login successful" due to responseType: 'text'
          console.log(response);
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error('Login failed', err);
        }
      });

    } else {
      // Register Logic
      const registerReq = {
        fullName: this.fullName,
        username: this.username,
        email: this.email,
        password: this.password
      };

      this.userService.register(registerReq).subscribe({
        next: (response) => {
          console.log('User registered', response);
          this.authState.set(true);
        },
        error: (err) => {
          console.error('Registration failed', err);
        }
      });
    }
  }
}
