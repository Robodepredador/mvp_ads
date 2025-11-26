import { Component } from '@angular/core';
import { NgIf, AsyncPipe } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoaderService } from './loader.service';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [NgIf, AsyncPipe, MatProgressSpinnerModule],
  template: `
    <div class="loader-backdrop" *ngIf="loader.loading$ | async">
      <mat-progress-spinner mode="indeterminate" diameter="60"></mat-progress-spinner>
    </div>
  `
})
export class LoaderComponent {
  constructor(public loader: LoaderService) {}
}
