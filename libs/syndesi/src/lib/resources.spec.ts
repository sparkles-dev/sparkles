import { Resource, Link, isLink, isLinks, link, links, embedded, embeddeds } from './resources';

describe(`Resource`, () => {

  it(`should extend existing interface types`, () => {
    interface A {
      b: number;
    }

    const test: Resource<A> = {
      _links: {
        self: { href: '/test' }
      },
      b: 123
    };

    expect(test.b).toEqual(123);
  });
});

describe(`isLink()`, () => {

  it(`should return true when property 'href' is string`, () => {
    const foo = { href: '/bar' };
    expect(isLink(foo)).toBeTruthy();
  });

  it(`should return false for undefined values`, () => {
    expect(isLink(undefined)).toBeFalsy();
    expect(isLink(null)).toBeFalsy();
  });

  it(`should return false for quirks`, () => {
    expect(isLink([])).toBeFalsy();
    expect(isLink({})).toBeFalsy();
  });

  it(`should be a type guard for Link`, () => {
    const a = {
      href: '/foo'
    };

    expect(isLink(a)).toBeTruthy();
    if (isLink(a)) {
      expect(a.href).toEqual('/foo');
    } else {
      fail('isLink() should return true for Link interface');
    }
  });
});

describe(`isLinks()`, () => {

  const foo = { href: '/foo' };
  const bar = { href: '/bar' };

  it(`should return true for array of Link`, () => {
    expect(isLinks([foo, bar])).toBeTruthy();
  });
});

describe(`link()`, () => {

  const test: Resource<any> = {
    _links: {
      self: { href: '/test' },
      foo: { href: '/bar' }
    }
  };

  it(`should return Link for rel`, () => {
    const l = link('self', test);
    expect(l.href).toEqual('/test');
  });
});

describe(`links()`, () => {

  const test: Resource<any> = {
    _links: {
      self: { href: '/test' },
      foo: [
        { href: '/bar' },
        { href: '/bar1' }
      ]
    }
  };

  it(`should return Link for rel`, () => {
    const l = links('foo', test);
    expect(l.length).toEqual(2);
  });
});

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
