import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PackageDemosComponent } from './package-demos.component';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [PackageDemosComponent],
  exports: [PackageDemosComponent]
})
export class PackageDemosModule {}
