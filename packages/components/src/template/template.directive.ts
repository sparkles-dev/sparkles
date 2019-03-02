import { Directive, Input, TemplateRef, QueryList } from '@angular/core';

/**
 * The `spTemplate` directive needs to be used in combination with `<ng-template>`.
 *
 * Its purpose is to provide flexible templating features to components with embedded views
 * like the `sp-select`, `sp-list-view`, `sp-autocomplete`, and so on.
 *
 * **Please see individual components for concrete usage examples.**
 * This sections gives a generic description.
 *
 * ##### How To Use
 *
 * In a component with child views, e.g. `sp-select`, you want to render the items dynamically.
 * Embedd an `<ng-template spTemplate>` element in the component's body.
 * Use the [template input variables, prefixed with `let-*`](https://angular.io/guide/structural-directives#template-input-variable),
 * to render the child view's data.
 *
 * ```html
 * <sp-select>
 *   <ng-template spTemplate let-item>
 *     <span class="custom-dropdown-item">{{ item.label }}</span>
 *   </ng-template>
 * </sp-select>
 * ```
 *
 * ##### How It Works
 *
 * The parent component, e.g. `sp-select`, grabs a reference via `@ContentChild()` or
 * `@ContentChildren()` decorators.
 * Then, the template should be rendered with an [`*ngTemplateOutlet`](https://angular.io/api/common/NgTemplateOutlet)
 * or by calling the [`createEmbeddedView()`](https://angular.io/api/core/TemplateRef#createembeddedview).
 *
 * ```ts
 * import { TemplateDirective } from '@sparkles/components';
 *
 * @Component({
 *   template: `
 *     <ng-container *ngTemplateOutlet="singleItem.template; context: {$implicit: val}"></ng-container>
 *   `
 * })
 * export class SingleTemplateComponent {
 *
 *   @ContentChild(TemplateDirective)
 *   singleItem: TemplateDirective;
 * }
 * ```
 *
 * When you have **multiple templates**, they can be grouped by types like so:
 *
 * ```html
 * <sp-multi-template>
 *   <ng-template spTemplate="body">...</ng-template>
 *   <ng-template spTemplate="footer">...</ng-template>
 * </sp-multi-template>
 * ```
 *
 * References need to be grabbed with `@ContentChildren()` decorator using the
 * `ngAfterContentInit()` lifecycle hook:
 *
 * ```ts
 * import { TemplateDirective } from '@sparkles/components';
 *
 * @Component({
 *   template: `
 *     <ng-container *ngTemplateOutlet="bodyTemplate; context: {$implicit: val}"></ng-container>
 *     <ng-container *ngTemplateOutlet="footerTemplate; context: {$implicit: val}"></ng-container>
 *   `
 * })
 * export class MultiTemplateComponent {
 *
 *   @ContentChildren(TemplateDirective)
 *   templates: QueryList<TemplateDirective>;
 *
 *   bodyTemplate: TemplateRef<any>;
 *   footerTemplate: TemplateRef<any>;
 *
 *   ngAfterContentInit() {
 *     this.templates.forEach((item) => {
 *       switch (item.getType()) {
 *         case 'body':
 *           this.bodyTemplate = item.template;
 *           break;
 *         case 'footer':
 *           this.footerTemplate = item.template;
 *           break;
 *         default:
 *           throw new Error(`Unknown <ng-template> type embedded`);
 *       }
 *     });
 *   }
 * }
 * ```
 *
 * @stable
 */
@Directive({
  selector: '[spTemplate]'
})
export class TemplateDirective {

  @Input() spTemplate: string;

  constructor(
    public template: TemplateRef<any>
  ) {}

  getType(): string {
    return this.spTemplate;
  }
}

/**
 * Returns the `TemplateRef` from the `<ng-template spTemplate="type">` element.
 *
 * @param queryList List of `TemplateDirective` should be obtained with `@ContentChildren()` or `ViewChildren()`
 * @param type The type given to the `<ng-template spTemplate="type">` element.
 * @returns TemplateRef Template reference should be rendered with `<ng-container *ngTemplateOutlet>`
 */
export function templateByType(queryList: QueryList<TemplateDirective>, type: string): TemplateRef<any> {
  const spTemplate = queryList
    .find(t => t !== undefined && type === t.getType())

  return spTemplate ? spTemplate.template : undefined;
}
