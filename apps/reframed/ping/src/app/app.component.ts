import { Component } from '@angular/core';

@Component({
  selector: 'sparkles-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'reframed-ping';

  onAppMessage(evt: any) {
    console.log("onAppMessage(): ", evt);
  }

  onAppLoads(url: any) {
    console.log("onAppLoads(): ", url);
  }

  onAppLoaded(url: any) {
    console.log("onAppLoaded(): ", url);
  }
}
