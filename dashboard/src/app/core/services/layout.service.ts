import { Injectable, signal, computed, effect } from '@angular/core';

export interface LayoutConfig {
  darkTheme: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class LayoutService {
  private _layoutConfig = signal<LayoutConfig>({
    darkTheme: true
  });

  layoutConfig = this._layoutConfig.asReadonly();

  isDarkTheme = computed(() => this._layoutConfig().darkTheme);

  constructor() {
    effect(() => {
      const config = this._layoutConfig();
      if (config.darkTheme) {
        document.documentElement.classList.add('app-dark');
      } else {
        document.documentElement.classList.remove('app-dark');
      }
    });
  }

  toggleDarkMode() {
    const isDark = !this._layoutConfig().darkTheme;

    if ('startViewTransition' in document) {
      document.startViewTransition(() => {
        this._layoutConfig.update(config => ({
          ...config,
          darkTheme: isDark
        }));
      });
    } else {
      this._layoutConfig.update(config => ({
        ...config,
        darkTheme: isDark
      }));
    }
  }
}
