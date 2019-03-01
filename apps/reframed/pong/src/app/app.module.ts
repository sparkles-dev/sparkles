import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ReframedModule } from '@sparkles/reframed';
import { AppComponent } from './app.component';
import { PlayComponent } from './play.component';

@NgModule({
  declarations: [AppComponent, PlayComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot([], { initialNavigation: 'enabled', useHash: true }),
    ReframedModule.forGuest([
      {
        path: 'play-it-now',
        component: PlayComponent
      }
    ], { urlScheme: 'sp://' })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
