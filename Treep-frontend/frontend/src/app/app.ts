import { Component, signal, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface StatusResponse {
  status: string;
  message: string;
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');
  protected readonly backendStatus = signal<StatusResponse | null>(null);
  private http = inject(HttpClient);

  ngOnInit() {
    this.http.get<StatusResponse>('http://localhost:8080/api/status')
      .subscribe({
        next: (response) => this.backendStatus.set(response),
        error: (err) => console.error('Error fetching status', err)
      });
  }
}
