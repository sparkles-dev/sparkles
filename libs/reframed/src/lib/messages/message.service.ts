import { Injectable, ElementRef } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { fromEvent } from 'rxjs/observable/fromEvent';
import { map, filter } from 'rxjs/operators';
import { Message, MessageTypes } from './message.interfaces';

const MESSAGE_BUS$ = new ReplaySubject<Message>();

fromEvent(window, 'message')
  .pipe(
    map(event => {
      const msgEvent = event as MessageEvent;

      return deserializeMessage(msgEvent.data);
    }),
    filter(event => event !== undefined)
  )
  .subscribe(MESSAGE_BUS$);

function deserializeMessage(data: any): Message | undefined {
  try {
    const msg: Message = JSON.parse(data);

    return msg;
  } catch (e) {
    return undefined;
  }
}

function serializeMessage(msg: Message): any {
  return JSON.stringify(msg);
}

@Injectable()
export class MessageService {

  public get messages$(): Observable<Message> {
    return MESSAGE_BUS$.asObservable();
  }

  public notify(target: ElementRef, type: string, payload: any): void {
    const msg = serializeMessage({ type, payload });

    // TODO: safe guard target origin
    const targetOrigin = '*';
    if (target.nativeElement instanceof HTMLIFrameElement) {
      target.nativeElement.contentWindow.postMessage(msg, targetOrigin);
    } else if (typeof target.nativeElement.postMessage === 'function') {
      target.nativeElement.postMessage(msg, targetOrigin);
    }
  }

  public launch(target: ElementRef, payload: any) {
    this.notify(target, MessageTypes.LAUNCH, payload);
  }

  public finish(target: ElementRef, payload: any) {
    this.notify(target, MessageTypes.FINISH, payload);
  }

  public cancel(target: ElementRef, payload: any) {
    this.notify(target, MessageTypes.CANCEL, payload);
  }
}
