import { async, TestBed } from '@angular/core/testing';
import { ReframedModule } from './reframed.module';

describe('ReframedModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ReframedModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ReframedModule).toBeDefined();
  });
});
