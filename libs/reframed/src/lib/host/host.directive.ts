import {
  Directive,
  Input,
  Renderer2,
  ElementRef,
  EventEmitter,
  OnDestroy,
  Output,
  NgZone,
  OnInit
} from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageService } from '../messages/message.service';
import { Message, MessageTypes } from '../messages/message.interfaces';
import { UrlSerializer } from '../url/url-serializer.service';
import { UrlResolver } from '../url/url-resolver.service';
import { ParsedUrl } from '../reframed.interfaces';

@Directive({
  selector: '[sparklesAppLaunch]'
})
export class HostDirective implements OnInit, OnDestroy {
  @Input()
  public set uAppLaunch(value: string | ParsedUrl) {
    if (typeof value === 'string') {
      this.parsedUrl = this.urlSerializer.deserialize(value);
    } else {
      this.parsedUrl = value;
    }

    this.iframeUrl = this.urlResolver.resolvePublicUrl(this.parsedUrl);
    this.launch();
  }

  @Output() public uAppMessage: EventEmitter<Message> = new EventEmitter();

  @Output() public uAppLoads: EventEmitter<Message> = new EventEmitter();

  @Output() public uAppLoaded: EventEmitter<Message> = new EventEmitter();

  private iframeUrl: string;
  private parsedUrl: ParsedUrl;

  private unsubscribeFn: Function;
  private subscriptions: Subscription[] = [];

  constructor(
    private urlResolver: UrlResolver,
    private iframeElement: ElementRef,
    private renderer: Renderer2,
    private msgService: MessageService,
    private ngZone: NgZone,
    private urlSerializer: UrlSerializer
  ) {}

  private launch() {
    this.ngZone.runTask(() => {
      this.uAppLoads.next();
    });

    this.clearListener();

    this.unsubscribeFn = this.renderer.listen(this.iframeElement.nativeElement, 'load', () => {
      this.onIFrameLoad();
    });

    this.renderer.setAttribute(this.iframeElement.nativeElement, 'src', this.iframeUrl);
  }

  ngOnInit() {
    const sub = this.msgService.messages$.subscribe(value => {
      this.ngZone.runTask(() => {
        this.uAppMessage.next(value);
      });

      if (value.type === MessageTypes.CANCEL || value.type === MessageTypes.FINISH) {
        this.renderer.setAttribute(this.iframeElement.nativeElement, 'src', 'about:blank');
      }
    });
    this.subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    this.clearListener();

    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  /** @internal */
  onIFrameLoad() {
    this.msgService.launch(this.iframeElement, this.parsedUrl);

    this.ngZone.runTask(() => {
      this.uAppLoaded.next();
    });
  }

  private clearListener() {
    if (this.unsubscribeFn) {
      this.unsubscribeFn();
    }
  }
}
