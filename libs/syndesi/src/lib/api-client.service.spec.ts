import { async, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiClient } from './api-client.service';
import { Resource, ResourceCollection } from './resource.interfaces';

describe('ApiClient', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ HttpClientTestingModule ],
      providers: [ ApiClient ]
    }).compileComponents();
  }));

  it('should create', () => {
    const service = TestBed.get(ApiClient);
    expect(service).toBeTruthy();
    const backend = TestBed.get(HttpTestingController);
    expect(backend).toBeTruthy();
  });

  describe(`call()`, () => {
    interface Foo {
      what: string;
    }

    const foo: Resource<Foo> = {
      _links: {
        self: { href: '/foo' }
      },
      what: 'foo!'
    };

    interface FooCollectionResource extends ResourceCollection<Foo, any> {}

    it(`should return an Observable`, () => {
      const api: ApiClient = TestBed.get(ApiClient);
      const obs = api.call(foo);
      expect(obs).toBeTruthy();
    });

    xit(`should...`, () => {
      const api: ApiClient = TestBed.get(ApiClient);

      api.call(foo).get('next').send<FooCollectionResource>().subscribe(next => {
        const bar = next.resource;

        expect(bar).toBeTruthy();
      });
    });
  });
});
