import { async, TestBed } from '@angular/core/testing';
import { PackageDemosModule } from './package-demos.module';

describe('PackageDemosModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [PackageDemosModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(PackageDemosModule).toBeDefined();
  });
});
