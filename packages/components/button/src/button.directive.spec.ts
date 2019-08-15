import { TestBed, async } from '@angular/core/testing';
import { Component, ViewChild } from '@angular/core';
import { DomElementRef, createTestComponent } from '@sparkles/testing';
import { ButtonModule } from './button.module';
import { ButtonDirective } from './button.directive';

describe(`[spButton] ButtonDirective`, () => {

  @Component({
    template: `<button spButton>foo</button>`
  })
  class TestComponent {

    @ViewChild(ButtonDirective, { static: false })
    public button: ButtonDirective;
  }

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ ButtonModule ],
      declarations: [ TestComponent ]
    });
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

  describe(`spButton`, () => {

    it(`should render .btn-blue`, () => {
      const fixture = createTestComponent(TestComponent, `<button [spButton]="'blue'">foobar</button>`);
      fixture.detectChanges();

      expect(DomElementRef.create(fixture.elementRef).querySelector('.btn.btn-blue')).toBeTruthy();
      fixture.destroy();
    });

    it(`should render .btn-grey`, () => {
      const fixture = createTestComponent(TestComponent, `<button spButton="grey">foo</button>`);
      fixture.detectChanges();

      expect(DomElementRef.create(fixture.elementRef).querySelector('.btn.btn-grey')).toBeTruthy();
      fixture.destroy();
    });
  });

});
