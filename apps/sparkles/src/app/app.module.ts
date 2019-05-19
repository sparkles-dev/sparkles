import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NxModule } from '@nrwl/nx';
import { provideDebug, Debug } from '@sparkles/shared';
import { DemosAppModule } from '@sparkles/demos/app';
import { traceChangeDetection } from '@sparkles/components';
import { environment } from '../environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

/** Root app module for the sparkles app (app shell). */
@NgModule({
  imports: [
    BrowserModule,
    NxModule.forRoot(),
    AppRoutingModule,
    DemosAppModule
  ],
  providers: [provideDebug(environment), traceChangeDetection()],
  declarations: [AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(debug: Debug) {
    console.log('App is running in develop mode?', debug.isDevelop);
    console.log(debug.stackTrace('foo'));
    debug.enable('foo');
    debug.logger('foo').info('bar');
    debug.deprecation('foo', 'is not really deprecated');
    debug.experimental('bar', 'is the new kid on the block');
  }
}
