import { Component } from '@angular/core';
import { AppLaunch } from '@sparkles/reframed';

@Component({
  selector: 'sp-play',
  template: `<span [spAppLaunchFinish]="'fertig'">finish now</span>`
})
export class PlayComponent implements AppLaunch {

  onAppLaunch(url) {
    console.log("launched with...", url);
  }
}
