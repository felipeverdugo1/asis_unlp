import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient,withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import 'zone.js';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideBrowserGlobalErrorListeners(),
    //provideZonelessChangeDetection(),
    provideHttpClient(withFetch())
  ]
};
