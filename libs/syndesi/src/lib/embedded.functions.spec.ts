import { Resource } from './resource.interfaces';
import { embedded, embeddeds } from './embedded.functions';

describe(`embedded()`, () => {

  const test: Resource<any> = {
    _links: {
      self: { href: '/test' }
    },
    _embedded: {
      foo: {
        _links: { self: { href: '/foo'} },
        value: 'first'
      },
      bar: {
        _links: { self: { href: '/bar'} },
        value: 'second'
      },
    }
  };

  it(`should return Resource for rel`, () => {
    const r = embedded<any>('foo', test);
    expect(r).toBeTruthy();
    expect(r.value).toEqual('first');

    const r2 = embedded<any>('bar', test);
    expect(r2).toBeTruthy();
    expect(r2.value).toEqual('second');
  });
});

describe(`embeddeds()`, () => {

  const test: Resource<any> = {
    _links: {
      self: { href: '/test' }
    },
    _embedded: {
      foo: [
        {
          _links: { self: { href: '/foo'} },
          value: 'first'
        },
        {
          _links: { self: { href: '/bar'} },
          value: 'second'
        }
      ]
    }
  };

  it(`should return Resource[] for rel`, () => {
    const r = embeddeds<any>('foo', test);
    expect(r.length).toEqual(2);
  });
});
