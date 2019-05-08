import { Component, Input, ViewChild, ElementRef, ComponentFactoryResolver, ApplicationRef, Injector, OnChanges } from '@angular/core';
import { CdkPortal, PortalOutlet, DomPortalOutlet } from '@angular/cdk/portal';
import { Overlay } from '@angular/cdk/overlay';

/**
 * `<sp-dom-outlet>` is an utility to attach arbitrary content to an overlay panel covering the viewport of the browser screen.
 * Actually, the content will be attached to the `<body>` element of the document.
 * Optionally, the content can also be attached to any other element on the screen.
 *
 * ##### How To Use
 *
 * Embed arbitrary content to the component and toggle the display mode through an input-bound property:
 *
 * ```html
 * <sp-dom-outlet [spAttached]="'inplace'|'attached'|'hidden'">
 *   <h1>put your custom content here</h1>
 * </sp-dom-outlet>
 * ```
 *
 * ##### How It Works
 *
 * Internally, it uses [Angular CDK Portals](https://material.angular.io/cdk/portal/api) to capture the projected content and attaches it to a `PortalOutlet`.
 *
 * Depending on the `spAttached` property:
 *
 *  - when `attached`, the portal is positioned in an [Angular CDK Overlay](https://material.angular.io/cdk/overlay/overview),
 *  - when `inplace`, the portal is beamed to the host element (appended as child to `<sp-dom-outlet>` in the DOM hierarchy),
 *  - when `hidden`, the portal is not shown, thus the content is hidden.
 *
 * @experimental
 */
@Component({
  selector: 'sp-dom-outlet, [spDomOutlet]',
  template: `
    <ng-template cdkPortal>
      <div [class.fullscreen-on-body]="spAttached === 'attached'">
        <ng-content></ng-content>
      </div>
    </ng-template>
  `,
  styles: [`
    .fullscreen-on-body {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      z-index: 1050;
      background-color: white;
    }
  `]
})
export class DomOutletComponent implements OnChanges {

  @Input()
  public spDomOutlet: ElementRef | 'body' | Element = 'body';

  @Input()
  public spAttached: 'attached' | 'inplace' | 'hidden' = 'inplace';

  /** @internal */
  @ViewChild(CdkPortal)
  public portal: CdkPortal;

  private outlet: PortalOutlet;

  constructor(
    private element: ElementRef,
    private overlay: Overlay,
    private cfr: ComponentFactoryResolver,
    private appRef: ApplicationRef,
    private injector: Injector
  ) {}

  ngOnChanges(changes) {
    if (changes['spAttached']) {
      this.toggle();
    }
  }

  toggle() {
    if (this.outlet) {
      this.outlet.detach();
    }

    if (this.spAttached === 'hidden') {
      // Do nothing to hide the content
      return;
    }

    if (this.spAttached === 'attached') {
      if (this.spDomOutlet === 'body') {
        this.outlet = this.overlay.create();
      } else {
        const nativeElement = this.spDomOutlet instanceof ElementRef ? this.spDomOutlet.nativeElement : this.spDomOutlet;
        this.outlet = new DomPortalOutlet(nativeElement, this.cfr, this.appRef, this.injector);
      }
    } else if (this.spAttached === 'inplace') {
      this.outlet = new DomPortalOutlet(this.element.nativeElement, this.cfr, this.appRef, this.injector);
    }

    this.outlet.attach(this.portal);
  }

}
