import { Directive, HostListener, ElementRef, Input } from '@angular/core';
import { MessageService } from '../messages/message.service';

@Directive({
    selector: '[spAppLaunchFinish]'
})
export class FinishDirective {

  @Input()
  public spAppLaunchFinish: any = '';

  constructor(private messages: MessageService) {}

  @HostListener('click')
  public onClick() {
    this.messages.finish(new ElementRef(window.parent), this.spAppLaunchFinish);
  }
}
