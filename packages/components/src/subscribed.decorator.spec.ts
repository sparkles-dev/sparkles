import { OnDestroy } from '@angular/core';
import { Subscription, of } from 'rxjs';
import { Subscribed } from './subscribed.decorator';

describe(`@Subscribed()`, () => {

  class Foo {

    @Subscribed()
    public sub: Subscription = of('1234').subscribe();
  }

  it(`should call .unsubscribe() on ngOnDestroy()`, () => {
    const foo = new Foo();
    const spy = jest.spyOn(foo.sub, 'unsubscribe');

    (foo as any).ngOnDestroy();

    expect(spy).toHaveBeenCalledTimes(1);
  });

  it(`should add ngOnDestroy() lifecycle hook`, () => {
    const foo = new Foo();

    expect((foo as any).ngOnDestroy).toBeTruthy();
    expect(typeof (foo as any).ngOnDestroy).toBe('function');
  });

  class Bar extends Foo implements OnDestroy {

    ngOnDestroy() {}
  }

  it(`should call original ngOnDestroy() lifecycle hook`, () => {
    const bar = new Bar();
    const spy = jest.spyOn(bar, 'ngOnDestroy');

    bar.ngOnDestroy();
    expect(spy).toHaveBeenCalledTimes(1);
  });
});
