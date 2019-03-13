import { Input, Directive, forwardRef, OnInit, OnDestroy, ViewContainerRef, ComponentRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';
import { ControlsService } from './controls.service';

@Directive({
  selector: '[spFormControl]',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: forwardRef(() => FormControlDirective)
    }
  ]
})
export class FormControlDirective implements OnInit, OnDestroy, ControlValueAccessor {

  @Input()
  spFormControl: string;

  private componentRef: ComponentRef<ControlValueAccessor>;

  constructor(
    private controls: ControlsService,
    private viewContainer: ViewContainerRef
  ) {}

  ngOnInit() {
    const factory = this.controls.resolve(this.spFormControl);
    this.componentRef = this.viewContainer.createComponent(factory);
    this.componentRef.changeDetectorRef.markForCheck();
  }

  ngOnDestroy() {
    if (this.componentRef) {
      this.componentRef.destroy();
    }
  }

  writeValue(obj: any) {
    this.componentRef.instance.writeValue(obj);
  }

  registerOnChange(fn: any) {
    this.componentRef.instance.registerOnChange(fn);
  }

  registerOnTouched(fn: any) {
    this.componentRef.instance.registerOnTouched(fn);
  }

  setDisabledState(isDisabled: boolean) {
    if (this.componentRef.instance.setDisabledState) {
      this.componentRef.instance.setDisabledState(isDisabled);
    }
  }
}
