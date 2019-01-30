import { Directive, HostListener, ElementRef } from '@angular/core';
import { MessageService } from '../messages/message.service';

@Directive({
    selector: '[uAppLaunchFinish]'
})
export class FinishDirective {
    constructor(private messages: MessageService) {}

    @HostListener('click')
    public onClick() {
        this.messages.finish(new ElementRef(window.parent), '');
    }
}
