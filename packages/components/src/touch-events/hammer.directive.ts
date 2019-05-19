import { Directive, ElementRef, NgZone, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import 'hammerjs';

@Directive({
  selector: '[spHammer]'
})
export class HammerDirective implements OnInit, OnChanges {

  @Input()
  public spHammer: string;

  @Output()
  public spHammerInput: EventEmitter<HammerInput> = new EventEmitter();

  private hammer: HammerManager;

  constructor(
    private element: ElementRef,
    private ngZone: NgZone
  ) {}

  ngOnChanges(changes: SimpleChanges) {
    const change = changes['spHammer'];
    if (change.previousValue && change.previousValue !== change.currentValue) {
      this.unregister();
    }
    if (change.currentValue && change.previousValue !== change.currentValue) {
      this.register();
    }
  }

  ngOnInit() {
    if (!this.spHammer) {
      throw new Error(`You must bind a touch event with [spHammer]="eventName"!`);
    }
  }

  private register() {
    /* XXX: running hammer inside the NgZone would return in many superfluous change detection cycles
    this.hammer = new Hammer(this.element.nativeElement);
    this.hammer.on(this.spHammer, (evt: HammerInput) => {
      this.spHammerInput.emit(evt);
    });
    */

    this.ngZone.runOutsideAngular(() => {
      this.hammer = new Hammer(this.element.nativeElement);
      this.hammer.on(this.spHammer, (evt: HammerInput) => {
        this.ngZone.run(() => {
          this.spHammerInput.emit(evt);
        });
      });
    });
  }

  private unregister() {
    if (this.hammer) {
      // Gracefully clean up DOM Event listeners that were registed by hammer.js
      this.hammer.stop(true);
      this.hammer.destroy();
    }
  }

}
