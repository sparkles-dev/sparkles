import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'sp-dom-outlet-demo',
  template: `
    <h1>DOM Outlet</h1>

    <button spButton="blue" (click)="onToggle()">
      <span *ngIf="attached === 'hidden'">Show DOM Outlet</span>
      <span *ngIf="attached === 'inplace'">Attach DOM Outlet</span>
      <span *ngIf="attached === 'attached'">Close DOM Outlet</span>
    </button>

    <button spButton="grey" (click)="onChangeTarget()">Switch Target Element</button>

    <sp-dom-outlet [spAttached]="attached" [spDomOutlet]="target">
      <h4>Put content in here</h4>
      <p>This piece of content is either shown inline, or attached to the body, or hidden.</p>
      <button *ngIf="attached === 'attached'" spButton="grey" (click)="onToggle()">Close and Hide DOM Outlet</button>
    </sp-dom-outlet>

    <div style="position: absolute; top: 0; bottom: 0; right: 0; width: 20%" #targetVar></div>
  `,
  host: {
    style: 'position: relative; display: block;'
  }
})
export class DomOutletDemoComponent {

  attached = 'inplace';

  target: string | ElementRef = 'body';

  @ViewChild('targetVar')
  targetElement: ElementRef;

  onToggle() {
    switch (this.attached) {
      case 'inplace':
        this.attached = 'attached';
        return;
      case 'attached':
        this.attached = 'hidden';
        return;
      case 'hidden':
        this.attached = 'inplace';
        return;
    }
  }

  onChangeTarget() {
    if (this.target === 'body') {
      this.target = this.targetElement;
    } else {
      this.target = 'body';
    }
  }

}
