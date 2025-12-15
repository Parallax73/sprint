import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SidebarService } from '../../core/services/sidebar.service';

@Component({
  selector: 'app-subbar',
  standalone: true,
  imports: [ButtonModule],
  templateUrl: './subbar.html',
  styleUrl: './subbar.scss'
})
export class Subbar {
  sidebarService = inject(SidebarService);
}
