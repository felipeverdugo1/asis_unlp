import { Component, inject, Output, OnInit, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EncuestadorService } from '../../services/encuestador.service';
import { ZonaService } from '../../services/zonas.service';
import { BarriosService } from '../../services/barrios.service';
import { CommonModule } from '@angular/common';
import { JornadaService } from '../../services/jornada.service';
import { CampaniaService } from '../../services/campanias.service';

@Component({
  selector: 'app-carga-csv',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './carga-csv.html',
  styleUrls: ['../../../styles.css']
})
export class CargaCsvComponent implements OnInit {
  private fb = inject(FormBuilder);
  private encuestadorService = inject(EncuestadorService);
  private zonaService = inject(ZonaService);
  private barrioService = inject(BarriosService);
  private jornadaService = inject(JornadaService);
  private campaniaService = inject(CampaniaService);
  encuestadores: any[] = [];
  zonas: any[] = [];
  barrios: any[] = [];
  jornadas: any[] = [];
  campanias: any[] = [];
  
  @Output() datosCargados = new EventEmitter<any>();

  formulario: FormGroup = this.fb.group({
    encuestador_id: ['', Validators.required],
    zona_id: [{ value: '', disabled: true }, Validators.required],
    jornada_id: [{ value: '', disabled: true }, Validators.required],
    barrio_id: ['', Validators.required],
    campania_id: [{ value: '', disabled: true }, Validators.required],
    generalCsv: [null, Validators.required],
    branchesCsv: [null, Validators.required]
  });

  ngOnInit(): void {
    this.cargarEncuestadores();
    this.cargarBarrios();

    // Escuchar cambios en barrio_id para cargar zonas
    this.formulario.get('barrio_id')?.valueChanges.subscribe(barrioId => {
      if (barrioId) {
        this.cargarZonasPorBarrio(barrioId);
        this.cargarCampanias(barrioId);
      } else {
        this.zonas = [];
        this.formulario.get('zona_id')?.reset();
      }
    });

    // Escuchar cambios en campania_id para cargar jornadas
    this.formulario.get('campania_id')?.valueChanges.subscribe(campaniaId => {
      if (campaniaId) {
        this.cargarJornadasPorCampania(campaniaId);
      } else {
        this.jornadas = [];
        this.formulario.get('jornada_id')?.reset();
      }
    });
  }

  cargarEncuestadores() {
    this.encuestadorService.getEncuestadores().subscribe(data => {
      this.encuestadores = data;
    });
  }

  cargarBarrios() {
    this.barrioService.getBarrios().subscribe((data: any[]) => {
      this.barrios = data;
    });
  }

  cargarCampanias(barrioId: number) {
    this.formulario.get('campania_id')?.enable();
    this.campaniaService.getCampaniasByBarrio(barrioId).subscribe((data: any[]) => {
        this.campanias = data;
        if (!this.campanias.some(j => j.id === this.formulario.get('campania_id')?.value)) {
            this.formulario.get('campania_id')?.setValue('');
        }
    })
  }

  cargarJornadasPorCampania(campaniaId: number) {
    this.formulario.get('jornada_id')?.enable();
    this.jornadaService.getJornadasPorCampania(campaniaId).subscribe((data: any[]) => {
        this.jornadas = data;
        if (!this.jornadas.some(j => j.id === this.formulario.get('jornada_id')?.value)) {
            this.formulario.get('jornada_id')?.setValue('');
        }
    })
  }

  cargarZonasPorBarrio(barrioId: number) {
    this.formulario.get('zona_id')?.enable();
    this.zonaService.getZonasPorBarrio(barrioId).subscribe((data: any[]) => {
      this.zonas = data;
      // Resetear zona_id si las zonas cambiaron
      if (!this.zonas.some(z => z.id === this.formulario.get('zona_id')?.value)) {
        this.formulario.get('zona_id')?.setValue('');
      }
    });
  }

  onFileChange(event: any, field: string) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      if (!file.name.endsWith('.csv')) {
        alert('Por favor suba un archivo CSV');
        return;
      }
      this.formulario.get(field)?.setValue(file);
    }
  }

  onSubmit() {
    if (this.formulario.valid) {
      const formData = new FormData();
      formData.append('encuestador_id', this.formulario.get('encuestador_id')?.value);
      formData.append('zona_id', this.formulario.get('zona_id')?.value);
      formData.append('jornada_id', this.formulario.get('jornada_id')?.value);
      formData.append('generalCsv', this.formulario.get('generalCsv')?.value);
      formData.append('branchesCsv', this.formulario.get('branchesCsv')?.value);

      this.datosCargados.emit(formData);
    }
  }
}