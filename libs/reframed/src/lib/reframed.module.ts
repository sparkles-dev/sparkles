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
  EntryPoint,
  AppResolverOptions,
  provideUrlResolverOptions,
  DEFAULT_URL_RESOLVER_OPTIONS
} from './app-launcher.interfaces';

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

@NgModule({
  imports: []
})
export class ReframedModule {
  public static forHost(resolverOptions?: AppResolverOptions): ModuleWithProviders {
    const hostProviders: Provider[] = [
      ...providers,
      provideUrlResolverOptions(resolverOptions || DEFAULT_URL_RESOLVER_OPTIONS)
    ];

    return {
      ngModule: ReframedHostModule,
      providers: hostProviders
    };
  }

  public static forGuest(entries?: EntryPoint[]): ModuleWithProviders {
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
