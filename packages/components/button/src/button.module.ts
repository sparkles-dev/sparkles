import { NgModule, ModuleWithProviders, Optional, SkipSelf, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonDirective } from './button.directive';
import { ButtonOptions, BUTTON_OPTIONS, BUTTON_DEFAULTS } from './button.options';

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
    const value = Object.assign({}, opts, BUTTON_DEFAULTS);

    return {
      ngModule: ButtonModuleWithOptions,
      providers: [
        {
          provide: BUTTON_OPTIONS,
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
