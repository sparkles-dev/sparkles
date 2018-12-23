import { InjectionToken, Injectable, Inject, ComponentFactoryResolver, ComponentFactory, Type, ForwardRefFn } from '@angular/core';
import { ControlValueAccessor } from '@angular/forms';

export interface ControlValueAccessorType<T> extends Type<T & ControlValueAccessor> {
}

export const CONTROLS = new InjectionToken<ControlValueAccessorType<any>>('foo');

export function provideControl<T>(type: ControlValueAccessorType<T>) {
  return {
    provide: CONTROLS,
    multi: true,
    useValue: type
  };
}

@Injectable()
export class ControlsService {

  private factories: ComponentFactory<ControlValueAccessor>[];

  constructor(
    @Inject(CONTROLS) private controls: ControlValueAccessorType<any>[],
    private cfr: ComponentFactoryResolver
  ) {}

  resolve<T>(selector: string): ComponentFactory<T & ControlValueAccessor> {
    if (!this.factories) {
      this.factories = this.controls.map(t => this.cfr.resolveComponentFactory(t));
    }

    const factory = this.factories.find(f => f.selector === selector);
    return (factory as any) as ComponentFactory<T & ControlValueAccessor>;
  }

}
