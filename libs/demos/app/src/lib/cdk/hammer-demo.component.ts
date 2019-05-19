import { Component, DoCheck } from '@angular/core';

@Component({
  selector: 'sp-hammer-demo',
  template: `
    <div style="width: 100%; height: 100%; background-color: orange; position: absolute; display: flex;"
      [spHammer]="'pan'"
      (spHammerInput)="onHammerInput($event)">
      <div style="width: 50%">
        <pre>{{ debug.length }}</pre>
      </div>
      <div style="width: 50%">
        <pre>{{ checks }}</pre>
      </div>
    </div>
  `
})
export class HammerDemoComponent implements DoCheck {

  checks = 0;

  debug = [];

  onHammerInput(evt: HammerInput) {
    console.log(evt);
    this.debug.push(evt.pointerType);
  }

  ngDoCheck() {
    this.checks += 1;
  }

}
