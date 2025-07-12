import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  template: `
    <header class="header-container">
      <button class="header-home-btn" (click)="goHome()">Inicio</button>

        <nav class="header-breadcrumb">
          <ng-container *ngFor="let crumb of breadcrumbs; let last = last">
            <ng-container *ngIf="!last">
              <a [routerLink]="crumb.url" class="header-breadcrumb-link">{{ crumb.label }}</a>
              <span class="breadcrumb-separator"> / </span>
            </ng-container>
            <ng-container *ngIf="last">
              <span class="header-breadcrumb-current">{{ crumb.label }}</span>
            </ng-container>
          </ng-container>
        </nav>

    </header>
  `
})
export class HeaderComponent {
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  breadcrumbs: { label: string, url?: string }[] = [];

  constructor() {
    this.router.events.subscribe(() => {
      this.buildBreadcrumbs();
    });
  }

  goHome() {
    this.router.navigate(['/']);
  }

  buildBreadcrumbs() {
    const root = this.router.routerState.snapshot.root;
    const crumbs: { label: string, url?: string }[] = [];
    let fullUrl = '';
  
    function extract(route: any) {
      if (route.routeConfig?.path) {
        const segments = route.routeConfig.path.split('/');
      
        segments.forEach((segment: any) => {
          if (segment.startsWith(':')) {
            // necesito guardarme el :id para la redireccion
            const paramName = segment.slice(1);
            const paramValue = route.params[paramName];
          
            // se agrega a la url pero no al breadcrumb
            fullUrl += `/${paramValue}`;
          } else {
            // este es un elemento normal sin args
            fullUrl += `/${segment}`;

            const label = route.routeConfig?.data?.title ?? segment;
            crumbs.push({ label, url: fullUrl });
          }
        });
      }
  
      if (route.firstChild) extract(route.firstChild);
    }
  
    extract(root);
    this.breadcrumbs = crumbs;
  }


}
