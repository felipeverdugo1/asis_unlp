import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/home/header.component';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [RouterOutlet,CommonModule, HeaderComponent],
  templateUrl: './app.html',
  styles: []
})
export class App {
  protected title = 'asis_unlp';
}
