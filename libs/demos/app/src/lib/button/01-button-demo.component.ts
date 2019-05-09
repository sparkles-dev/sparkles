import { Component } from "@angular/core";

@Component({
  selector: 'sp-01-button-demo',
  template: `
    <sp-button (click)="onClick()" [attr.role]="'button'">Hello Button!</sp-button>
    <p *ngIf="message">{{ message }}</p>
  `
})
export class ButtonDemo01Component {

  message: string;

  onClick() {
    this.message = 'Button was clicked!';
  }
}
