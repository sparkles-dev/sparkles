import { NgModule, ModuleWithProviders, Optional, SkipSelf, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDirective } from './button.directive';
import { ButtonOptions, ButtonOptionsToken } from './button.options';

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

  public static withOptions(opts?: ButtonOptions): ModuleWithProviders {
    const value = new ButtonOptionsToken();
    Object.assign(value, opts);;

    return {
      ngModule: ButtonModuleWithOptions,
      providers: [
        {
          provide: ButtonOptionsToken,
          useValue: value
        }
      ]
    };
  }
}

/** @internal */
@NgModule({
  imports: [
    ButtonModule
  ],
  exports: [
    ButtonModule
  ]
})
export class ButtonModuleWithOptions {

  constructor (@Optional() @SkipSelf() parentModule: ButtonModuleWithOptions) {
    if (parentModule) {
      throw new Error('ButtonModule.withOptions() is already loaded. Import it in the AppModule only!');
    }
  }
}
