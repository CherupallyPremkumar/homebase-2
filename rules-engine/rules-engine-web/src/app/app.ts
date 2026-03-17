import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AppNavComponent } from './components/app-nav/app-nav.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, AppNavComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('policy-frontend');
}
