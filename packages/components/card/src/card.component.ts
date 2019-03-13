import { Component, ChangeDetectionStrategy, Input } from '@angular/core';

@Component({
  selector: 'sp-card',
  template: `
    <ng-container *ngIf="spLayout === 'default'">
      <ng-content select="h2"></ng-content>
      <ng-content select="img"></ng-content>
    </ng-container>
    <ng-container *ngIf="spLayout === 'cover'">
      <div class="cover-image">
        <ng-content select="img"></ng-content>
        <div class="cover-text">
          <ng-content select="h2"></ng-content>
        </div>
        <ng-content></ng-content>
      </div>
    </ng-container>
  `,
  styles: [`
    .cover-image {
      position: relative;
    }

    .cover-text {
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: rgba(0, 0, 0, .3);
      color: #fff;
    }
  `],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardComponent {

  @Input()
  public spLayout: 'default' | 'cover' = 'default';

}
