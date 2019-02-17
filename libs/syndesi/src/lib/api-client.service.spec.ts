import { async, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiClient } from './api-client.service';

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
});
