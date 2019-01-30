import { Directive, HostListener, ElementRef } from '@angular/core';
import { MessageService } from '../messages/message.service';

@Directive({
    selector: '[uAppLaunchCancel]'
})
export class CancelDirective {
    constructor(private messages: MessageService) {}

    @HostListener('click')
    public onClick() {
        this.messages.cancel(new ElementRef(window.parent), '');
    }
}
