import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDirective } from './button.directive';
import { ButtonOpts, ButtonOptions } from './button.options';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    ButtonDirective
  ],
  exports: [
    ButtonDirective
  ]
})
export class ButtonModule {

  public static withOptions(opts?: ButtonOpts): ModuleWithProviders {
    const value = new ButtonOptions();
    Object.assign(value, opts);;

    return {
      ngModule: ButtonModule,
      providers: [
        {
          provide: ButtonOptions,
          useValue: value
        }
      ]
    };
  }
}
