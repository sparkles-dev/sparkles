import { Component } from '@angular/core';

/**
 * Root app component for the sparkles app.
 *
 * The app shell is just a router outlet.
 */
@Component({
  selector: 'sp-root',
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent {}
