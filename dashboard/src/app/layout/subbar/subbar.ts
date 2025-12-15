import { Component, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { SidebarService } from '../../core/services/sidebar.service';

@Component({
  selector: 'app-subbar',
  standalone: true,
  imports: [ButtonModule],
  template: `
    <div class="subbar">
      <div class="subbar-left">
        <p-button
          icon="pi pi-bars"
          [text]="true"
          severity="secondary"
          (onClick)="sidebarService.toggle()">
        </p-button>
        <span class="page-title">Dashboard</span>
      </div>
      <div class="subbar-right">
        </div>
    </div>
  `,
  styles: [`
    .subbar {
      position: fixed;
      top: 56px;
      left: 0;
      right: 0;
      height: 48px;
      background: var(--surface-card);
      border-bottom: 1px solid var(--surface-border);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 1rem;
      z-index: 998;
    }

    .subbar-left {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .page-title {
      font-weight: 600;
      font-size: 1rem;
      color: var(--text-color);
    }
  `]
})
export class Subbar {
  sidebarService = inject(SidebarService);
}
