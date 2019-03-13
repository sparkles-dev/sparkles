import { NgModule, Optional, SkipSelf } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DemosAppComponent } from '@sparkles/demos/app';

const ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/welcome'
  },
  {
    path: '**',
    component: DemosAppComponent
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(ROUTES, { useHash: true, enableTracing: false })
  ],
  providers: []
})
export class AppRoutingModule {

  constructor (@Optional() @SkipSelf() parentModule: AppRoutingModule) {
    if (parentModule) {
      throw new Error(`${AppRoutingModule.name} is already loaded. Import it in the AppModule only!`);
    }
  }

}
