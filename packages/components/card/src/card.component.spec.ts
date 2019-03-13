import { Component } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { createTestComponent, DomElementRef } from '@sparkles/testing';
import { CardHeaderComponent } from './card-header.component';
import { CardImageComponent } from './card-image.component';
import { CardComponent } from './card.component';

describe('<sp-card>', () => {

  @Component({})
  class FooComponent {}

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ CardComponent, CardHeaderComponent, CardImageComponent, FooComponent ]
    });
  });

  xit(`should add .has-image CSS class when sp-card-image is embedded`, () => {
    const fixture = createTestComponent(FooComponent, `
      <sp-card>
        <sp-card-image></sp-card-image>
      </sp-card>
    `);
    fixture.detectChanges();

    const element = DomElementRef.create(fixture.elementRef);
    expect(element.querySelector('sp-card').hasClass('has-image')).toBeTruthy();
  });

  xit(`should project spTitle to a <h2>`, () => {
    const fixture = createTestComponent(FooComponent, `
      <sp-card [spTitle]="'foo'">
      </sp-card>
    `);
    fixture.detectChanges();

    const element = DomElementRef.create(fixture.elementRef);
    const heading = element.querySelector('sp-card > h2');
    expect(heading).toBeTruthy();
    expect(heading.innerHTML).toEqual('foo');
  });

  xit(`should keep order of embedded <sp-card-header> and <sp-card-image>`, () => {
    const fixture = createTestComponent(FooComponent, `
      <sp-card>
        <sp-card-image src="//foo.svg"></sp-card-image>
        <sp-card-header>Hello World!</sp-card-header>
      </sp-card>
    `);
    fixture.detectChanges();

    const element = DomElementRef.create(fixture.elementRef).querySelector('sp-card');
    expect(element.innerHTML.indexOf(`<sp-card-header>Hello World!</sp-card-header>`)).toEqual(0);
    expect(element.innerHTML.indexOf(`<sp-card-image`)).toBeGreaterThan(0);
  });

  it(`should layout 'cover'`, () => {
    const fixture = createTestComponent(FooComponent, `
      <sp-card [spLayout]="'cover'">
        <h2>Hello World!</h2>
        <img src="//foo.svg">
      </sp-card>
    `);
    fixture.detectChanges();

    const element = DomElementRef.create(fixture.elementRef).querySelector('sp-card');
console.log(element.innerHTML);
    //expect(element.innerHTML.indexOf(`<img`)).toEqual(0);
    expect(element.querySelector('.cover-text').innerHTML).toContain('Hello World!');
  });
});
