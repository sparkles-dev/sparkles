import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

// TODO: demo data + dynamic component pattern
@Component({
  selector: 'sp-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TabsComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
