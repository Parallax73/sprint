import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { SidebarService } from '../../core/services/sidebar.service';
import {MenuItem} from 'primeng/api';
import {Menu} from 'primeng/menu';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, ButtonModule, Menu],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar  implements OnInit {
  sidebarService = inject(SidebarService);
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Dashboard',
        items: [
          {
            label: 'Alerts',
            icon: 'pi pi-bell'
          },
          {
            label: 'Detection Rules',
            icon: 'pi pi-shield'
          },
          {
            label: 'Audit Logs ',
            icon: 'pi pi-bullseye'
          },
          {
            label: 'Settings',
            icon: 'pi pi-cog'
          },
          {
            label: 'Users',
            icon: 'pi pi-users'
          }
        ]
      }
    ];
  }

}
