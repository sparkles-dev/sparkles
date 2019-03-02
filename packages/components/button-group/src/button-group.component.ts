import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo ContentChildren pattern
@Component({
  selector: 'sp-button-group',
  templateUrl: './button-group.component.html',
  styleUrls: ['./button-group.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ButtonGroupComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
