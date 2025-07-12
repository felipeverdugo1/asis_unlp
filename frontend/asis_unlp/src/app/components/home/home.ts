import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-home',
  imports: [CommonModule,RouterModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

}
