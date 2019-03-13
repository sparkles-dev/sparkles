import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardComponent } from './card.component';
import { CardHeaderComponent } from './card-header.component';
import { CardImageComponent } from './card-image.component';

@NgModule({
  imports: [CommonModule],
  declarations: [CardComponent, CardHeaderComponent, CardImageComponent],
  exports: [CardComponent, CardHeaderComponent, CardImageComponent]
})
export class CardModule {}
