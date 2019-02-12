import { NgModule, ModuleWithProviders, Provider } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, provideRoutes } from '@angular/router';
import { CancelDirective } from './guest/cancel.directive';
import { FinishDirective } from './guest/finish.directive';
import { GuestComponent } from './guest/guest.component';
import { HostDirective } from './host/host.directive';
import { MessageService } from './messages/message.service';
import { UrlSerializer } from './url/url-serializer.service';
import { UrlResolver } from './url/url-resolver.service';
import {
  provideEntries,
  Entry,
  ReframedOptions,
  provideReframedOptions,
  DEFAULT_REFRAMED_OPTIONS
} from './reframed.interfaces';

const providers = [UrlSerializer, MessageService, UrlResolver];

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [GuestComponent, CancelDirective, FinishDirective],
  exports: [GuestComponent, CancelDirective, FinishDirective]
})
export class ReframedGuestModule {}

@NgModule({
  imports: [CommonModule],
  declarations: [HostDirective],
  exports: [HostDirective]
})
export class ReframedHostModule {}

/**
 * A feature module for providing cross-iframe screen workflows and bidirectional communication
 * between host app and guest app.
 *
 * @stable
 */
@NgModule({
  imports: []
})
export class ReframedModule {

  // TODO: public static forDancer(): combines forHost() + forGuest()

  public static forHost(resolverOptions?: ReframedOptions): ModuleWithProviders {
    const hostProviders: Provider[] = [
      ...providers,
      provideReframedOptions(resolverOptions || DEFAULT_REFRAMED_OPTIONS)
    ];

    return {
      ngModule: ReframedHostModule,
      providers: hostProviders
    };
  }

  public static forGuest(entries?: Entry[]): ModuleWithProviders {
    const guestProviders = [
      ...providers,
      provideEntries(entries),
      provideRoutes([
        {
          path: 'external',
          children: [
            {
              path: '**',
              component: GuestComponent
            }
          ]
        }
      ])
    ];

    return {
      ngModule: ReframedGuestModule,
      providers: guestProviders
    };
  }
}
