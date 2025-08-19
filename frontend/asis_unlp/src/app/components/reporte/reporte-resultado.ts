import { Component, Input, AfterViewInit, OnChanges, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import * as L from "leaflet";
import "leaflet.heat";

@Component({
  standalone: true,
  selector: 'reporte-resultado',
  imports : [CommonModule],
  template: `
    <div *ngIf="data" class="resultado-container">
      <div class="map-wrapper">
        <div id="map" #mapContainer></div>
      </div>
    </div>
  `,
  styles: [`
    .resultado-container {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .map-wrapper {
      position: relative;
      width: 100%;
      max-width: 900px;
      height: 500px;
      overflow: hidden;
      border: 2px solid #ccc;
      border-radius: 8px;
      margin: 2rem auto;
      background: #f9f9f9; /* 🔑 evita huecos feos cuando no hay tiles */
    }

    #map {
      width: 100%;
      height: 500px;
      min-height: 500px;
      display: block;
    }
  `]
})
export class ReporteResultadoComponent implements AfterViewInit, OnChanges {
  @Input() data: any[] = [];
  private map!: L.Map;
  private heatLayer: any;
  private router = inject(Router);


  ngAfterViewInit() {
    this.initMap();
    this.updateHeatmap();

    requestAnimationFrame(() => {
      this.map.invalidateSize(true);
    });
  }

  ngOnChanges() {
    if (this.map) {
      this.updateHeatmap();
    }
  }

  private initMap() {
    this.map = L.map('map', {
      preferCanvas: true,
      zoomControl: true
    }).setView([-34.9133, -57.9509], 15);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
      maxZoom: 19
    }).addTo(this.map);

    // Esperar a que Angular pinte bien el contenedor
    setTimeout(() => {
      this.map.invalidateSize(true);
    }, 500);
  }

  private updateHeatmap() {
    if (!this.data || !this.data.length) return;

    // Convertir datos a formato [lat, lng, intensity]
    const points = this.data.map(d => [d.lat, d.lng, d.cantidad]);

    if (this.heatLayer) {
      this.map.removeLayer(this.heatLayer);
    }

    // Crear el heatmap con parámetros ajustables
    this.heatLayer = (L as any).heatLayer(points, {
      radius: 25,
      blur: 15,
      maxZoom: 17,
      minOpacity: 0.5,
      gradient: {0.4: 'blue', 0.6: 'cyan', 0.7: 'lime', 0.8: 'yellow', 1.0: 'red'}
    }).addTo(this.map);

    // Ajustar la vista
    const bounds = L.latLngBounds(points.map(p => [p[0], p[1]]));
    this.map.fitBounds(bounds, {padding: [20, 20]});
  }

}
