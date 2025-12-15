import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../sidebar/sidebar';
import { Subbar } from '../subbar/subbar';
import { SidebarService } from '../../core/services/sidebar.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, Sidebar, Subbar, CommonModule],
  template: `
    <app-subbar></app-subbar>

    <app-sidebar></app-sidebar>

    <div class="layout-content" [class.shifted]="sidebarService.isOpen()">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .layout-content {
      padding: 2rem;
      margin-top: 48px;
      min-height: calc(100vh - 56px);
      background-color: var(--background);
      transition: margin-left 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
    }

    .layout-content.shifted {
      margin-left: 250px;
    }
  `]
})
export class MainLayout {
  sidebarService = inject(SidebarService);
}
