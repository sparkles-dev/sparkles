import { async, TestBed } from '@angular/core/testing';
import { ClientService } from './client.service';

describe('SyndesiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [ClientService]
    }).compileComponents();
  }));

  it('should create', () => {
    const service = TestBed.get(ClientService);
    expect(service).toBeTruthy();
  });
});
