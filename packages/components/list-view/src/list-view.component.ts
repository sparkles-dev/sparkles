import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo cdkVirtualScroll pattern
@Component({
  selector: 'sp-list-view',
  templateUrl: './list-view.component.html',
  styleUrls: ['./list-view.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListViewComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
