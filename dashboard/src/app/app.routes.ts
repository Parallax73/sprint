import { Routes } from '@angular/router';
import {MainLayout} from './layout/main-layout/main-layout';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/auth').then(m => m.Auth)
  },
  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard)
      },
      {
        path: 'home',
        loadComponent: () => import('./features/home/home').then(m => m.Home)
      },
      {
        path: 'alerts',
        loadComponent: () => import('./features/alerts/alerts').then(m => m.Alert)
      },
      {
        path: 'detection-rules',
        loadComponent: () => import('./features/rules/rules').then(m => m.Rules)
      },
      {
        path: 'audit-logs',
        loadComponent: () => import('./features/audit/audit').then(m => m.Audit)
      },
      {
        path: 'users',
        loadComponent: () => import('./features/users/users').then(m => m.Users)
      }
    ]
  }
];
