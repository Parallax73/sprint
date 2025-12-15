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
      }
    ]
  }
];
