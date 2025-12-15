import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TopbarComponent } from './layout/topbar/topbar.component';
import {Sidebar} from './layout/sidebar/sidebar';

@Component({
  selector: 'app-root',
  imports: [
    TopbarComponent,
    RouterOutlet,
    Sidebar
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'dashboard';
}
