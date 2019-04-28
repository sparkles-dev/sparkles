import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo ContentChildren pattern
@Component({
  selector: 'sp-button-toolbar',
  templateUrl: './button-toolbar.component.html',
  styleUrls: ['./button-toolbar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ButtonToolbarComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
