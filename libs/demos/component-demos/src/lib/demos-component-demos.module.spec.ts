import { async, TestBed } from '@angular/core/testing';
import { DemosComponentDemosModule } from './demos-component-demos.module';

describe('DemosComponentDemosModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [DemosComponentDemosModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(DemosComponentDemosModule).toBeDefined();
  });
});
