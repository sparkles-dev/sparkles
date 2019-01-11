import { async, TestBed } from '@angular/core/testing';
import { Debug, provideDebug } from './debug';

describe('Debug', () => {
  let debug: Debug;
  beforeEach(async(() => {
    debug = new Debug();

    TestBed.configureTestingModule({}).compileComponents();
  }));

  it(`should be injectable by default`, () => {
    expect(TestBed.get(Debug)).toBeTruthy();
  });

  it(`should generate a stack trace`, () => {
    const trace = debug.stackTrace();
    expect(trace).toContain('src/lib/debug.spec.ts');
  });

  it(`should return isDevelop=true for non-production environment`, () => {
    debug.environment = { production: false };
    expect(debug.isDevelop).toBeTruthy();
  });

});

describe(`provideDebug()`, () => {

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [ provideDebug({ production: true, foo: 'bar' })]
    }).compileComponents();
  }));

  it(`should set up an injectable for Debug`, () => {
    const debug = TestBed.get(Debug);
    expect(debug).toBeTruthy();
    expect(debug.environment.foo).toEqual('bar');
  });

  it(`should store Debug as global variable 'window.sparkles.debug'`, () => {
    const debug = (window as any).sparkles.debug;
    expect(debug).toBeTruthy();
    expect(debug.environment.foo).toEqual('bar');
  });

});
