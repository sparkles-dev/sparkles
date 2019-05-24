import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MapModule } from './map/map.module';
import { AppComponent } from './app.component';

@NgModule({
  imports: [BrowserModule, MapModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule {}
