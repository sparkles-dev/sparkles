import { ElementRef } from '@angular/core';

/**
 * Utility class to query a native DOM element.
 *
 * ##### How To Use
 *
 * Grab a reference to an Angular `ElementRef` and create a `DomElementRef` with:
 *
 * ```ts
 * const elementRef: ElementRef = ...; // from somewhere else, e.g. inject in constructor
 * new DomElementRef(elementRef);
 * ```
 *
 * You can also create a new instance from a `ElementRef` or a native DOM element:
 *
 * ```ts
 * const elementRef = DomElementRef.create(elementRefOrNativeElement);
 * ```
 *
 * @stable
 */
export class DomElementRef {
  public readonly nativeElement: any;

  /**
   * Creates a new `DomElementRef`.
   *
   * @param element Either an Angular `ElementRef` or a native DOM element.
   * @returns A `DomElementRef`
   */
  static create(element: ElementRef | any) {
    if (element instanceof ElementRef) {
      return new DomElementRef(element);
    } else {
      return new DomElementRef(new ElementRef(element));
    }
  }

  constructor(public readonly elementRef: ElementRef) {
    if (!elementRef || !elementRef.nativeElement) {
      throw new Error(`DomElementRef: Cannot wrap an undefined nativeElement!`);
    }

    this.nativeElement = elementRef.nativeElement;
  }

  get innerHTML(): string {
    return this.elementRef.nativeElement.innerHTML;
  }

  get innerText(): string {
    return this.elementRef.nativeElement.innerText;
  }

  get computedStyle(): CSSStyleDeclaration {
    return window.getComputedStyle(this.elementRef.nativeElement);
  }

  /**
   * Queries this `ElementRef` for a child element that matches `selector`.
   *
   * @param selector CSS selector string
   * @return An instance of `DomElementRef`
   * @throws If the selector does not match any element
   */
  querySelector(selector: string): DomElementRef {
    const result = this.elementRef.nativeElement.querySelector(selector);

    if (result) {
      return new DomElementRef(new ElementRef(result));
    } else {
      throw new Error(`DomElementRef: Selector ${selector} did not match any element`);
    }
  }

  /**
   * Queries for all child elements that match the given `selector`.
   *
   * @param selector CSS selector string
   * @return An array of `DomElementRef`
   */
  querySelectorAll(selector: string): DomElementRef[] {
    const result: NodeList = this.elementRef.nativeElement.querySelectorAll(selector);

    return Array.from(result).map(node => new DomElementRef(new ElementRef(node)));
  }

  /**
   * Removes the element from the DOM.
   */
  removeElement() {
    if (this.nativeElement.parentNode) {
      this.nativeElement.parentNode.removeChild(this.nativeElement);
    }
  }

  /**
   * Returns the value of a DOM attribute.
   *
   * @param name DOM attribute name
   */
  attribute(name: string): any {
    return this.elementRef.nativeElement.getAttribute(name);
  }

  /**
   * Returns `true`, if this element has the CSS class `name`.
   *
   * @param CSS Class name, e.g. 'foobar'
   */
  hasClass(name: string): boolean {
    return this.elementRef.nativeElement.classList.contains(name);
  }

}
