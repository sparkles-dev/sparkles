import { Resource } from './resource.interfaces';

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

  it(`should allow to declare custom interface types`, () => {
    interface A {
      id: number;
    }

    interface AResource extends Resource<A> {}

    const test: AResource = {
      id: 123,
      _links: {
        self: { href: '/user/123' }
      }
    };

    expect(test.id).toEqual(123);
    expect(test._links.self.href).toEqual('/user/123');
  });
});
