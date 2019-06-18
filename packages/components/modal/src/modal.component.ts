import {
  Component,
  OnInit,
  OnChanges,
  OnDestroy,
  ChangeDetectionStrategy,
  ViewChild,
  ElementRef,
  ComponentFactoryResolver,
  ApplicationRef,
  Injector,
  Input
} from '@angular/core';
import { CdkPortal, PortalOutlet, DomPortalOutlet } from '@angular/cdk/portal';
import { Overlay } from '@angular/cdk/overlay';

// TODO: demo portal outlet pattern
@Component({
  selector: 'sp-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ModalComponent implements OnChanges, OnDestroy {
  @Input()
  public set spVisible(value: boolean) {
    this.isFullScreen = value;
  }

  @Input()
  public set spAnchor(value: 'overlay' | ElementRef) {
    this.anchor = value;
  }

  /** @internal */
  public isFullScreen = false;

  /** @internal */
  public anchor: 'overlay' | ElementRef = 'overlay';

  /** @internal */
  @ViewChild(CdkPortal, { static: false })
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
    if (changes['spVisible']) {
      this.toggle();
    }
  }

  ngOnDestroy() {
    if (this.isFullScreen && this.outlet) {
      this.outlet.detach();
    }
  }

  toggle() {
    if (this.outlet) {
      this.outlet.detach();
    }

    if (this.isFullScreen) {
      if (this.anchor === 'overlay') {
        this.outlet = this.overlay.create();
      } else {
        this.outlet = new DomPortalOutlet(
          this.anchor.nativeElement,
          this.cfr,
          this.appRef,
          this.injector
        );
      }
    } else {
      this.outlet = new DomPortalOutlet(
        this.element.nativeElement,
        this.cfr,
        this.appRef,
        this.injector
      );
    }

    this.outlet.attach(this.portal);
  }
}
