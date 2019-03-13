import { Component, ChangeDetectionStrategy, ElementRef } from '@angular/core';

@Component({
  selector: 'sp-card-image, img[spCardImage]',
  template: `
    <ng-container *ngIf="!isNativeImage">
      <img [attr.src]="src">
    </ng-container>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardImageComponent {

  /** @internal */
  public isNativeImage = false;

  /** @internal */
  public src: string;

  constructor(
    public elementRef: ElementRef
  ) {
    this.src = elementRef.nativeElement.getAttribute('src');
    this.isNativeImage = elementRef.nativeElement.tagName === 'img';
  }

}
