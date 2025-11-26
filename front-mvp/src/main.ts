import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';         // <-- IMPORTAR LA CLASE PRINCIPAL
import appConfig from './app/app.config'; // <-- IMPORTAR CONFIG

bootstrapApplication(App, appConfig)
  .catch(err => console.error(err));
