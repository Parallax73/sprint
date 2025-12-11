import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Button} from 'primeng/button';
import {Auth} from './features/auth/auth';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Button, Auth],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'dashboard';
}
