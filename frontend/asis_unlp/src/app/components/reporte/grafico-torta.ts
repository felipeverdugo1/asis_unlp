import { Component, Input, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, ChartData, ChartOptions } from 'chart.js';

// 👇 IMPORTANTE: registrar todo para que "pie" y "doughnut" funcionen
import { registerables } from 'chart.js';
Chart.register(...registerables);

@Component({
  selector: 'grafico-torta',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  template: `
    <div class="w-full max-w-md" style="height: 500px;">
      <h3 style="margin:0 0 8px 0">{{ title }}</h3>
      <canvas
        baseChart
        [type]="pieChartType"
        [data]="pieChartData()"
        [options]="pieChartOptions"
        (chartClick)="onChartClick($event)">
      </canvas>
    </div>
  `
})
export class GraficoTortaComponent {
  @Input() labels: string[] = ['A', 'B', 'C'];
  @Input() values: number[] = [30, 50, 20];
  @Input() title = 'Distribución';
  @Input() doughnut = true;

  pieChartType: 'pie' | 'doughnut' = 'pie';

  pieChartOptions: ChartOptions<'pie' | 'doughnut'> = {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 0
    },
    plugins: {
      legend: { position: 'top' },
      tooltip: { enabled: true },
      title: { display: false }
    }
  };

  private labelsSig = signal<string[]>(this.labels);
  private valuesSig = signal<number[]>(this.values);

  ngOnChanges() {
    this.labelsSig.set(this.labels ?? []);
    this.valuesSig.set(this.values ?? []);
    this.pieChartType = this.doughnut ? 'doughnut' : 'pie';
  }

  pieChartData = computed<ChartData<'pie' | 'doughnut', number[], string>>(() => ({
    labels: this.labelsSig(),
    datasets: [{ data: this.valuesSig(), borderWidth: 1 }]
  }));

  onChartClick(evt: unknown) {
    console.log(evt);
  }
}
