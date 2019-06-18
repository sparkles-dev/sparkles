import { Component, ContentChildren, ViewChildren, Input, ViewChild, QueryList } from '@angular/core';
import { TestBed, fakeAsync } from '@angular/core/testing';
import { DynamicQueries, ContentChanges, ViewChanges, QueryListChange } from './dynamic-queries';

describe(`@DynamicQueries()`, () => {

  @Component({
    selector: 'sp-dynamic-child',
    template: `{{ foo }} works`
  })
  class ChildComponent {

    @Input()
    foo: any
  }

  @Component({
    selector: 'sp-content-parent',
    template: `<ng-content></ng-content>`
  })
  @DynamicQueries()
  class ContentParentComponent implements ContentChanges {

    @ContentChildren(ChildComponent)
    public dynamicContent$: QueryList<ChildComponent>;

    contentChange: QueryListChange;

    spOnContentChanges(change) {
      this.contentChange = change;
    }
  }

  @Component({
    template: `
      <sp-content-parent>
        <sp-dynamic-child *ngFor="let foo of foos"
                         [foo]="foo"></sp-dynamic-child>
      </sp-content-parent>`
  })
  class ContentParentTestingComponent {

    foos = [1, '2', 'three'];

    @ViewChild(ContentParentComponent, /* TODO: add static flag */ {})
    contentParent: ContentParentComponent;
  }

  @Component({
    template: `
      <sp-dynamic-child *ngFor="let foo of foos"
                       [foo]="foo"></sp-dynamic-child>`
  })
  @DynamicQueries()
  class ViewParentComponent implements ViewChanges {

    foos = [1, '2', 'three'];

    @ViewChildren(ChildComponent)
    public dynamicView$: QueryList<ChildComponent>;

    viewChange: QueryListChange;

    spOnViewChanges(change) {
      this.viewChange = change;
    }
  }

  // setup
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChildComponent,
        ViewParentComponent,
        ContentParentComponent,
        ContentParentTestingComponent
      ]
    });
  });

  it(`should notify when @ContentChildren() changes`, fakeAsync(() => {
    const testComponent = TestBed.createComponent(ContentParentTestingComponent);
    spyOn(testComponent.componentInstance.contentParent, 'spOnContentChanges').and.callThrough();
    testComponent.detectChanges();

    const contentChange = testComponent.componentInstance.contentParent.contentChange;
    expect(contentChange['dynamicContent$'].length).toEqual(3);
    expect(testComponent.componentInstance.contentParent.spOnContentChanges).toHaveBeenCalled();
  }));

  it(`should notify when @ContentChildren() are initialized`, fakeAsync(() => {
    const testComponent = TestBed.createComponent(ContentParentTestingComponent);
    spyOn(testComponent.componentInstance.contentParent, 'spOnContentChanges').and.callThrough();
    // Initial rendering
    testComponent.detectChanges();

    // Change projected content dynamically
    testComponent.componentInstance.foos = [1];
    testComponent.detectChanges();

    const contentChange = testComponent.componentInstance.contentParent.contentChange;
    expect(contentChange['dynamicContent$'].length).toEqual(1);

    expect(testComponent.componentInstance.contentParent.spOnContentChanges).toHaveBeenCalledTimes(2);
  }));

  it(`should notify when @ViewChildren() changes`, () => {
    const viewParent = TestBed.createComponent(ViewParentComponent);
    spyOn(viewParent.componentInstance, 'spOnViewChanges').and.callThrough();
    // Initial rendering
    viewParent.detectChanges();

    // Change view chilren dynamically
    viewParent.componentInstance.foos = ['four'];
    viewParent.detectChanges();

    const viewChange = viewParent.componentInstance.viewChange;
    expect(viewChange['dynamicView$'].length).toEqual(1);
    expect(viewParent.componentInstance.spOnViewChanges).toHaveBeenCalledTimes(2);
  });

  it(`should notify when @ViewChildren() are initialized`, () => {
    const viewParent = TestBed.createComponent(ViewParentComponent);
    spyOn(viewParent.componentInstance, 'spOnViewChanges').and.callThrough();
    viewParent.detectChanges();

    const viewChange = viewParent.componentInstance.viewChange['dynamicView$'];
    expect(viewChange.length).toEqual(3);
    expect(viewParent.componentInstance.spOnViewChanges).toHaveBeenCalled();
  });

});
