import { CommonModule } from '@angular/common';
import {
  Component,
  ContentChildren,
  QueryList,
  TemplateRef,
  AfterContentInit,
  ViewChild
} from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { createTestComponent } from '@sparkles/testing';
import { TemplateDirective, templateByType } from './template.directive';

describe('<ng-template spTemplate>', () => {
  @Component({
    selector: 'sp-parent',
    template: `
      <div *ngFor="let t of templates?.toArray()">
        <ng-container *ngTemplateOutlet="t.template"></ng-container>
      </div>
    `
  })
  class ParentComponent implements AfterContentInit {
    @ContentChildren(TemplateDirective)
    templates: QueryList<TemplateDirective>;

    templateRefs: { [key: string]: TemplateRef<any> } = {};

    ngAfterContentInit() {
      this.templates.forEach(item => {
        this.templateRefs[item.getType()] = item.template;
      });
    }
  }

  @Component({ template: `` })
  class TestingComponent {
    @ViewChild(ParentComponent)
    public parent: ParentComponent;
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonModule],
      declarations: [TemplateDirective, ParentComponent, TestingComponent]
    });
  });

  it(`<ng-template spTemplate>`, () => {
    const foo = createTestComponent(
      TestingComponent,
      `
      <sp-parent>
        <ng-template spTemplate></ng-template>
      <sp-parent>
    `
    );
    foo.detectChanges();

    expect(foo).toBeTruthy();
    expect(foo.componentInstance.parent.templates.length).toEqual(1);
  });

  it(`<ng-template spTemplate="type"> (can be grouped by type)`, () => {
    const foo = createTestComponent(
      TestingComponent,
      `
      <sp-parent>
        <ng-template spTemplate="body"></ng-template>
        <ng-template spTemplate="footer"></ng-template>
      </sp-parent>
    `
    );
    foo.detectChanges();

    expect(foo).toBeTruthy();
    expect(foo.componentInstance.parent.templates.length).toEqual(2);
    expect(foo.componentInstance.parent.templateRefs['body']).toBeTruthy();
    expect(foo.componentInstance.parent.templateRefs['footer']).toBeTruthy();
  });

  it(`should throw when no <ng-template>`, () => {
    expect(() => {
      const foo = createTestComponent(
        TestingComponent,
        `<h1 spTemplate="body"></h1>`
      );
      foo.detectChanges();
    }).toThrow();
  });
});

describe(`templateByType()`, () => {
  @Component({
    selector: 'sp-parent',
    template: `
      <div *ngFor="let t of templates?.toArray()">
        <ng-container *ngTemplateOutlet="t.template"></ng-container>
      </div>
    `
  })
  class ParentComponent implements AfterContentInit {
    @ContentChildren(TemplateDirective)
    templates: QueryList<TemplateDirective>;

    templateRefs: { [key: string]: TemplateRef<any> } = {};

    ngAfterContentInit() {
      this.templates.forEach(item => {
        this.templateRefs[item.getType()] = item.template;
      });
    }
  }

  @Component({ template: `` })
  class TestingComponent {
    @ViewChild(ParentComponent)
    public parent: ParentComponent;
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonModule],
      declarations: [TemplateDirective, ParentComponent, TestingComponent]
    });
  });

  it(`should return TemplateRef for type`, () => {
    const foo = createTestComponent(
      TestingComponent,
      `
      <sp-parent>
        <ng-template spTemplate="foo"><h1>foo</h1></ng-template>
        <ng-template spTemplate="bar"><h2>bar</h2></ng-template>
      </sp-parent>
    `
    );
    foo.detectChanges();

    const queryList = foo.componentInstance.parent.templates;
    expect(queryList.length).toBeGreaterThan(0);
    expect(templateByType(queryList, 'foo')).toBeTruthy();
    expect(templateByType(queryList, 'bar')).toBeTruthy();
    expect(templateByType(queryList, 'foobar')).toBeFalsy();
  });
});
