import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ControlsService } from './controls.service';
import { FormControlDirective } from './form-control.directive';
import { FormComponent } from './form.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  declarations: [
    FormControlDirective,
    FormComponent
  ],
  exports: [
    FormComponent
  ],
  providers: [
    ControlsService
  ]
})
export class FormModule {}
