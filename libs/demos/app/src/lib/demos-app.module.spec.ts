import { async, TestBed } from '@angular/core/testing';
import { DemosAppModule } from './demos-app.module';

describe('DemosAppModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [DemosAppModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(DemosAppModule).toBeDefined();
  });
});
