import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { LayoutService } from '../../core/services/layout.service';
import {Dialog} from 'primeng/dialog';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [ButtonModule, Dialog],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.scss'
})
export class TopbarComponent {
  layoutService = inject(LayoutService);

  isHelpVisible = false;

  showDialog() {
    this.isHelpVisible = true;
  }
}
