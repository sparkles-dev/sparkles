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
  selector: 'iframe[spAppLaunch]'
})
export class HostDirective implements OnInit, OnDestroy {
  @Input()
  public set spAppLaunch(value: string | ParsedUrl) {
    if (value) {
      this.renderer.removeStyle(this.iframeElement.nativeElement, 'display');

      if (typeof value === 'string') {
        this.parsedUrl = this.urlSerializer.deserialize(value);
      } else {
        this.parsedUrl = value;
      }

      this.iframeUrl = this.urlResolver.resolvePublicUrl(this.parsedUrl);
      this.launch();
    } else {
      this.renderer.setStyle(this.iframeElement.nativeElement, 'display', 'none');
    }
  }

  @Output() public spAppMessage: EventEmitter<Message> = new EventEmitter();

  @Output() public spAppLoads: EventEmitter<ParsedUrl> = new EventEmitter();

  @Output() public spAppLoaded: EventEmitter<ParsedUrl> = new EventEmitter();

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
      this.spAppLoads.next(this.parsedUrl);
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
        this.spAppMessage.next(value);
      });

      if (value.type === MessageTypes.CANCEL || value.type === MessageTypes.FINISH) {
        this.clearListener();

        this.renderer.setAttribute(this.iframeElement.nativeElement, 'src', 'about:blank');
        this.renderer.setStyle(this.iframeElement.nativeElement, 'display', 'none');
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
      this.spAppLoaded.next(this.parsedUrl);
    });
  }

  private clearListener() {
    if (this.unsubscribeFn) {
      this.unsubscribeFn();
    }
  }
}
