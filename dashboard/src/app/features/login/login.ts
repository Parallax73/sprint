import { Component, signal} from '@angular/core';
import {ButtonDirective, ButtonModule} from "primeng/button";
import {Checkbox, CheckboxModule} from "primeng/checkbox";
import {InputText, InputTextModule} from "primeng/inputtext";
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

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
        InputTextModule

    ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  checked = signal<boolean>(true);
}
