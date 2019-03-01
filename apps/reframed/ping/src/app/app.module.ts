import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReframedModule } from '@sparkles/reframed';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot([], { initialNavigation: 'enabled' }),
    ReframedModule.forHost({ urlScheme: 'sp://', pathPrefix: '//localhost:4440/' })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
