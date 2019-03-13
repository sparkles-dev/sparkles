import { Component, forwardRef, NgModule } from '@angular/core';
import { async, TestBed, fakeAsync } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { NG_VALUE_ACCESSOR, ControlValueAccessor, FormsModule } from '@angular/forms';
import { provideControl } from './controls.service';
import { FormModule } from './form.module';
import { Form } from './form.interface';

@Component({
  selector: 'sp-controls-foo',
  template: `{{ value | json }}`,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => FooControlComponent)
    }
  ]
})
export class FooControlComponent implements ControlValueAccessor {

  value: any;

  writeValue(obj: any) {
    this.value = obj;
  }

  registerOnChange(fn: any) {
  }

  registerOnTouched(fn: any) {
  }

  setDisabledState(isDisabled: boolean) {
  }
}

@Component({
  selector: 'sp-controls-bar',
  template: `<input type="text"
    [(ngModel)]="value"
    (ngModelChange)="onChange($event)"
    (blur)="onTouched()"
    [class.disabled]="disabled">
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => BarControlComponent)
    }
  ]
})
export class BarControlComponent implements ControlValueAccessor {

  value: any;
  disabled = false;

  onChange = () => { };
  onTouched = () => { };

  writeValue(obj: any) {
    this.value = obj;
  }

  registerOnChange(fn: any) {
    this.onChange = fn;
  }

  registerOnTouched(fn: any) {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean) {
    this.disabled = isDisabled;
  }
}

@NgModule({
  imports: [
    CommonModule,
    FormsModule
  ],
  declarations: [
    FooControlComponent,
    BarControlComponent
  ],
  exports: [
    FooControlComponent,
    BarControlComponent
  ],
  entryComponents: [
    FooControlComponent,
    BarControlComponent
  ],
  providers: [
    provideControl(FooControlComponent),
    provideControl(BarControlComponent)
  ]
})
export class MyControlsModule {}

@Component({
  selector: 'sp-my-form',
  template: `
    <sp-form [form]="foo" (submit)="onSubmit($event)"></sp-form>
    <pre *ngIf="submitData">{{ submitData | json }}</pre>
  `
})
export class FormComponent {

  foo: Form = {
    controls: [
      {
        name: 'age',
        selector: 'sp-controls-foo',
        value: 12
      },
      {
        name: 'name',
        selector: 'sp-controls-bar',
        value: 'whoknows'
      }
    ]
  };
  submitData: any;

  onSubmit(value: any) {
    this.submitData = value;
  }

}

describe('FormModule', () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        FormModule,
        MyControlsModule
      ],
      declarations: [
        FormComponent
      ]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(FormModule).toBeTruthy();
  });

  it(`should render a form with custom controls`, fakeAsync(async function() {
    const component = TestBed.createComponent(FormComponent);
    component.detectChanges();

    // https://github.com/angular/angular/issues/22606#issuecomment-377390233
    await component.whenStable();

    const inputEl = component.nativeElement.querySelector('input[type="text"]');
    expect(inputEl).toBeTruthy();
    expect(inputEl.value).toEqual('whoknows');
  }));
});
