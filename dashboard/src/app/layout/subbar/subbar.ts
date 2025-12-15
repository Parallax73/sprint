import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, NavigationEnd, ActivatedRoute, RouterModule } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ButtonModule } from 'primeng/button';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { MenuItem } from 'primeng/api';
import { SidebarService } from '../../core/services/sidebar.service';

@Component({
  selector: 'app-subbar',
  standalone: true,
  imports: [CommonModule, ButtonModule, BreadcrumbModule, RouterModule],
  templateUrl: './subbar.html',
  styleUrl: './subbar.scss'
})
export class Subbar implements OnInit {
  sidebarService = inject(SidebarService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  items: MenuItem[] = [];
  home: MenuItem = { icon: 'pi pi-home', routerLink: '/home' };

  ngOnInit() {
    this.items = this.createBreadcrumbs(this.activatedRoute.root);

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.items = this.createBreadcrumbs(this.activatedRoute.root);
      });
  }

  private createBreadcrumbs(route: ActivatedRoute, url: string = '', breadcrumbs: MenuItem[] = []): MenuItem[] {
    const children: ActivatedRoute[] = route.children;

    if (children.length === 0) {
      return breadcrumbs;
    }

    for (const child of children) {
      const routeURL: string = child.snapshot.url.map(segment => segment.path).join('/');

      if (routeURL !== '') {
        url += `/${routeURL}`;

        const label = child.snapshot.data['breadcrumb'] ||
          (routeURL.charAt(0).toUpperCase() + routeURL.slice(1));

        breadcrumbs.push({ label: label, routerLink: url });
      }

      return this.createBreadcrumbs(child, url, breadcrumbs);
    }

    return breadcrumbs;
  }
}
