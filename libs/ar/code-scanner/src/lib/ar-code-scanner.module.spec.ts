import { async, TestBed } from '@angular/core/testing';
import { ArCodeScannerModule } from './ar-code-scanner.module';

describe('ArCodeScannerModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ArCodeScannerModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ArCodeScannerModule).toBeDefined();
  });
});
