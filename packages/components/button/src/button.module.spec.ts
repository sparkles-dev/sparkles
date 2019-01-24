import { TestBed } from '@angular/core/testing';
import { ButtonModule } from './button.module';
import { BUTTON_OPTIONS } from './button.options';

describe(`ButtonModule`, () => {

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it(`it should provide default ButtonOptions`, () => {
    const value = TestBed.get(BUTTON_OPTIONS);

    expect(value).toEqual({ defaultVariant: 'blue' });
  });

  describe(`.withOptions()`, () => {

    it(`should merge opts with defaults`, () => {
      TestBed.configureTestingModule({
        imports: [ ButtonModule.withOptions({ foo: 'bar' }) ]
      });

      const value = TestBed.get(BUTTON_OPTIONS);

      expect(value).toEqual({
        defaultVariant: 'blue',
        foo: 'bar'
      });
    });
  });
});
