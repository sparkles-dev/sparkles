import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NxModule } from '@nrwl/nx';
import { provideDebug, Debug } from '@sparkles/shared';
import { environment } from '../environments/environment';
import { AppComponent } from './app.component';

@NgModule({
  imports: [
    BrowserModule,
    NxModule.forRoot()
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
  }

}
