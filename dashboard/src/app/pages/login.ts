import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'simple-in-card',
  standalone: true,
  imports: [CommonModule, FormsModule, ButtonModule, CheckboxModule, InputTextModule],
  template: `
    <div class="login-wrapper">
      <div class="login-card">

        <div class="header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 33 32" fill="none">
              <path fill-rule="evenodd" clip-rule="evenodd" d="M7.09219 2.87829C5.94766 3.67858 4.9127 4.62478 4.01426 5.68992C7.6857 5.34906 12.3501 5.90564 17.7655 8.61335C23.5484 11.5047 28.205 11.6025 31.4458 10.9773C31.1517 10.087 30.7815 9.23135 30.343 8.41791C26.6332 8.80919 21.8772 8.29127 16.3345 5.51998C12.8148 3.76014 9.71221 3.03521 7.09219 2.87829ZM28.1759 5.33332C25.2462 2.06 20.9887 0 16.25 0C14.8584 0 13.5081 0.177686 12.2209 0.511584C13.9643 0.987269 15.8163 1.68319 17.7655 2.65781C21.8236 4.68682 25.3271 5.34013 28.1759 5.33332ZM32.1387 14.1025C28.2235 14.8756 22.817 14.7168 16.3345 11.4755C10.274 8.44527 5.45035 8.48343 2.19712 9.20639C2.0292 9.24367 1.86523 9.28287 1.70522 9.32367C1.2793 10.25 0.939308 11.2241 0.695362 12.2356C0.955909 12.166 1.22514 12.0998 1.50293 12.0381C5.44966 11.161 11.0261 11.1991 17.7655 14.5689C23.8261 17.5991 28.6497 17.561 31.9029 16.838C32.0144 16.8133 32.1242 16.7877 32.2322 16.7613C32.2441 16.509 32.25 16.2552 32.25 16C32.25 15.358 32.2122 14.7248 32.1387 14.1025ZM31.7098 20.1378C27.8326 20.8157 22.5836 20.5555 16.3345 17.431C10.274 14.4008 5.45035 14.439 2.19712 15.1619C1.475 15.3223 0.825392 15.5178 0.252344 15.7241C0.250782 15.8158 0.25 15.9078 0.25 16C0.25 24.8366 7.41344 32 16.25 32C23.6557 32 29.8862 26.9687 31.7098 20.1378Z" fill="#334155"/>
            </svg>
          </div>
          <h2 class="title">Welcome Back</h2>
          <p class="subtitle">
            Don't have an account? <a href="#" class="link">Create today!</a>
          </p>
        </div>

        <div class="form-container">

          <div class="form-group">
            <label for="email">Email Address</label>
            <input pInputText id="email" type="text" placeholder="Email address" class="input-field" />
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="password-wrapper">
              <input pInputText id="password" type="password" placeholder="Password" class="input-field" />
              <i class="pi pi-eye eye-icon"></i>
            </div>
          </div>

          <div class="form-actions">
            <div class="remember-me">
              <p-checkbox id="remember" [(ngModel)]="checked" [binary]="true"></p-checkbox>
              <label for="remember">Remember me</label>
            </div>
            <a href="#" class="link">Forgot your password?</a>
          </div>

          <button pButton class="submit-btn">
            <i class="pi pi-user"></i>
            <span>Sign In</span>
          </button>

        </div>
      </div>
    </div>
  `,
  styles: [`
    /* 1. Page Background & Centering */
    .login-wrapper {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #f8fafc; /* Very light grey background */
      padding: 20px;
    }

    /* 2. The Card Styling */
    .login-card {
      background: #ffffff;
      padding: 3rem; /* Spacious padding inside */
      border-radius: 16px; /* Rounded corners */
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03); /* Soft shadow */
      width: 100%;
      max-width: 450px; /* Limits width on large screens */
    }

    /* 3. Typography & Header */
    .header {
      text-align: center;
      margin-bottom: 2.5rem;
    }

    .logo {
      margin-bottom: 1rem;
    }

    .title {
      font-size: 1.875rem; /* ~30px */
      font-weight: 600;
      color: #0f172a; /* Dark slate */
      margin: 0 0 0.5rem 0;
    }

    .subtitle {
      color: #64748b; /* Slate grey */
      margin: 0;
    }

    .link {
      color: #10b981; /* THE GREEN COLOR from your image */
      font-weight: 500;
      text-decoration: none;
      cursor: pointer;
    }

    .link:hover {
        text-decoration: underline;
    }

    /* 4. Form Layout */
    .form-container {
      display: flex;
      flex-direction: column;
      gap: 1.5rem; /* Space between inputs */
    }

    .form-group {
      display: flex;
      flex-direction: column;
      gap: 0.5rem; /* Space between label and input */
    }

    .form-group label {
      font-weight: 500;
      color: #334155;
    }

    .input-field {
      width: 100%;
      padding: 0.75rem 1rem;
      border-radius: 8px; /* Slightly rounded inputs */
    }

    /* 5. Password Eye Icon positioning */
    .password-wrapper {
        position: relative;
    }
    .eye-icon {
        position: absolute;
        right: 15px;
        top: 50%;
        transform: translateY(-50%);
        color: #94a3b8;
        cursor: pointer;
    }

    /* 6. Checkbox & Links Row */
    .form-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: -0.5rem; /* Pull it up slightly */
    }

    .remember-me {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      color: #334155;
    }

    /* 7. The Green Button */
    .submit-btn {
      width: 100%;
      background-color: #10b981; /* Green matching the image */
      border: none;
      padding: 0.875rem;
      border-radius: 8px;
      display: flex;
      justify-content: center;
      gap: 0.5rem;
      font-weight: 600;
      margin-top: 1rem;
    }

    .submit-btn:hover {
        background-color: #059669; /* Darker green on hover */
    }
  `]
})
export class SimpleInCard {
  checked = signal<boolean>(true);
}
