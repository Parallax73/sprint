import { Component, signal } from '@angular/core';
import { ButtonDirective, ButtonModule } from "primeng/button";
import { Checkbox, CheckboxModule } from "primeng/checkbox";
import { InputText, InputTextModule } from "primeng/inputtext";
import { PasswordModule } from "primeng/password";
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

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


  // Auth state true = login, false = register
  authState = signal<boolean>(true);

  // "Remember me" a checker
  checked = signal<boolean>(true);
  password = signal<string>('');

  authStateChange() {
    this.authState.update(state => !state);
  }


}
