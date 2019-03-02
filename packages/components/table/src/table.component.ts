import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo <ng-content> + ContentChildren pattern
@Component({
  selector: 'sp-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
