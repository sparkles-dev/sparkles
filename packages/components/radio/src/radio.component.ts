import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo Service-Interface pattern
@Component({
  selector: 'sp-radio',
  templateUrl: './radio.component.html',
  styleUrls: ['./radio.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadioComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
