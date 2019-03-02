import { TestBed, async } from '@angular/core/testing';
import { ButtonModule } from './button.module';
import { Component } from '@angular/core';

describe(`ButtonDirective`, () => {

  @Component({
    template: `<button spButton>foo</button>`
  })
  class TestComponent {}

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ ButtonModule ],
      declarations: [ TestComponent ]
    }).compileComponents();
  }));

  it('should create', () => {
    expect(ButtonModule).toBeDefined();
    const testComponent = TestBed.createComponent(TestComponent);
    expect(testComponent).toBeDefined();
    testComponent.destroy();
  });

  it(`should render <button class="btn">`, () => {
    const testComponent = TestBed.createComponent(TestComponent);
    testComponent.detectChanges();

    expect(testComponent).toBeDefined();
    expect(testComponent.elementRef.nativeElement.querySelector('.btn')).toBeTruthy();

    testComponent.destroy();
  });

});
