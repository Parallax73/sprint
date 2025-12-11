import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Button} from 'primeng/button';
import {SimpleInCard} from './pages/login';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Button, SimpleInCard],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'dashboard';
}
