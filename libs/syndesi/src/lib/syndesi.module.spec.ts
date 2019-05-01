import { async, TestBed } from '@angular/core/testing';
import { SyndesiModule } from './syndesi.module';

describe('SyndesiModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [SyndesiModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(SyndesiModule).toBeDefined();
  });
});
