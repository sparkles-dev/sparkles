import { async, TestBed } from '@angular/core/testing';
import { ArWebrtcMediaModule } from './ar-webrtc-media.module';

describe('ArWebrtcMediaModule', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ArWebrtcMediaModule]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ArWebrtcMediaModule).toBeDefined();
  });
});
