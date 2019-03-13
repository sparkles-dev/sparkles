import { Component, Input, Output, EventEmitter } from  '@angular/core';
import { FormGroup, FormControl } from  '@angular/forms';
import { Form } from './form.interface';

@Component({
  selector: 'sp-form',
  template: `
    <form [formGroup]="formGroup" (ngSubmit)="onSubmit()">
      <ng-container
        *ngFor="let control of formData.controls"
        [spFormControl]="control.selector"
        [formControlName]="control.name">
      </ng-container>
    </form>
  `
})
export class FormComponent {

  formGroup: FormGroup;
  formData: Form;
  submitData: any;

  @Input()
  public set form(value: Form) {
    const controls = value.controls.reduce((ctrls, ctrl) => {
      ctrls[ctrl.name] = new FormControl(ctrl.value);

      return ctrls;
    }, {});

    this.formData = value;
    this.formGroup = new FormGroup(controls);
  }

  @Output()
  public submit: EventEmitter<any> = new EventEmitter();

  onSubmit() {
    this.submit.next(this.formGroup.value);
  }

}
