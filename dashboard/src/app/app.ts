import { Component } from '@angular/core';
import {Auth} from './features/auth/auth';
import { TopbarComponent } from './layout/topbar/topbar.component';


@Component({
  selector: 'app-root',
  imports: [Auth, TopbarComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'dashboard';
}
