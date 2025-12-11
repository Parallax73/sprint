import { Routes } from '@angular/router';

export const routes: Routes = [

  {
    path: 'login',
    loadComponent: () => import('./features/auth/auth').then(m => m.Auth)
  }




];
