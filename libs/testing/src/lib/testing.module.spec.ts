import { async, TestBed } from '@angular/core/testing';
import { TestingModule } from './testing.module';

describe('TestingModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestingModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(TestingModule).toBeDefined();
  });
});
