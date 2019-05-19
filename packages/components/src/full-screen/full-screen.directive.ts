import { Directive, OnChanges, Input, Inject, Optional, ElementRef, SimpleChanges, EventEmitter, OnInit, Output } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Directive({
  selector: '[spFullScreen]'
})
export class FullScreenDirective implements OnChanges, OnInit {

  @Input()
  spFullScreen = false;

  @Output()
  spFullScreenChange: EventEmitter<boolean> = new EventEmitter();

  private emitChangeOnNextEvent = false;

  private document: Document;

  constructor(
    private element: ElementRef,
    @Inject(DOCUMENT) @Optional() _document: any
  ) {
    this.document = _document;
  }

  ngOnInit() {
    if (this.document) {
      this.document.addEventListener('fullscreenchange', (evt) => {
        const fullscreenElement = (this.document as any).fullscreenElement;

        if (fullscreenElement === this.element.nativeElement) {
          this.emitChangeOnNextEvent = true;
        } else if (this.emitChangeOnNextEvent && !fullscreenElement) {
          this.spFullScreen = false;
          this.spFullScreenChange.emit(false);
        }
      });
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    const ch = changes['spFullScreen'];
    if (ch && ch.firstChange && ch.currentValue !== true) {
      // Do nothing on initial update to [spFullScreen]="false"
      return;
    }

    if (ch) {
      this.toggle();
    }
  }

  toggle() {
    if (this.spFullScreen !== true) {
      this.hide();
    } else {
      this.show();
    }
  }

  show() {
    if (this.element.nativeElement && typeof this.element.nativeElement.requestFullscreen === 'function') {
      return this.element.nativeElement.requestFullscreen();
    } else {
      return Promise.resolve();
    }
  }

  hide() {
    if (this.document) {
      const fullscreenElement = (this.document as any).fullscreenElement;
      if (fullscreenElement === this.element.nativeElement) {
        return this.document.exitFullscreen();
      } else {
        return Promise.resolve();
      }
    } else {
      return Promise.resolve();
    }
  }

}
