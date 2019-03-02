import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// demo <ng-content select> and semantic layout pattern
@Component({
  selector: 'sp-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
