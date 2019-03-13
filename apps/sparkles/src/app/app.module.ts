import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NxModule } from '@nrwl/nx';
import { PackageDemosModule } from '@sparkles/package-demos/app';
import { provideDebug, Debug } from '@sparkles/shared';
import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
  imports: [
    BrowserModule,
    PackageDemosModule,
    NxModule.forRoot(),
    AppRoutingModule
  ],
  providers: [
    provideDebug(environment)
  ],
  declarations: [
    AppComponent
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {

  constructor(
    debug: Debug
  ) {
    console.log("App is running in develop mode?", debug.isDevelop);
    console.log(debug.stackTrace('foo'));
    debug.enable('foo');
    debug.logger('foo').info('bar');
    debug.deprecation('foo', 'is not really deprecated');
    debug.experimental('bar', 'is the new kid on the block');
  }

}
