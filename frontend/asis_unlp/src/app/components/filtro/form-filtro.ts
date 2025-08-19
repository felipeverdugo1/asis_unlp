import { Component, inject, Output, EventEmitter, OnInit, Input } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { BarriosService } from "../../services/barrios.service";
import { CommonModule, formatDate } from "@angular/common";
import { ReporteService } from "../../services/reporte.service";
import { ConstantPool } from "@angular/compiler";
import { FiltroService } from "../../services/filtro.service";
import { AuthService } from "../../services/auth.service";
import { Filtro } from "../../models/filtro.model";
import { FilenameInputDialogComponent } from "./filename-input-dialog.component";


@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FilenameInputDialogComponent],
  selector: 'app-filtro-reporte',
  templateUrl: './form-filtro.html',
  styleUrls: ['../../../styles.css'],
})
export class FiltroReporteComponent implements OnInit {
  private fb = inject(FormBuilder);
  private barriosService = inject(BarriosService);
  private reporteService = inject(ReporteService);
  private filtroService = inject(FiltroService);
  private auth = inject(AuthService);
  guardando: boolean = false;
  showSaveDialog: boolean = false;

  @Output() generarReporte = new EventEmitter<any>();
  @Input() filtroActual: any;

  ngOnInit() {
    if (this.reporteService.getFiltroActual()) {
      this.cargarFiltroExistente(this.reporteService.getFiltroActual());
    }  
  }

  private cargarFiltroExistente(filtro: any) {
    // Cargar valores simples
    console.log(filtro.acceso_salud, filtro.acceso_agua);
    console.log("Tipo de filtro:", typeof filtro.acceso_salud, typeof filtro.acceso_agua);
    this.form.patchValue({
      edadMin: filtro.edad?.[0] || null,
      edadMax: filtro.edad?.[1] || null,
      barrio: filtro.barrio || null,
      acceso_salud: (typeof filtro.acceso_salud !== 'undefined') ? filtro.acceso_salud : null,
      acceso_agua: (typeof filtro.acceso_agua !== 'undefined') ? filtro.acceso_agua : null
    });

    // Cargar arrays (géneros y materiales)
    if (filtro.genero) {
      this.generoSeleccionado = [...filtro.genero];
    }
    
    if (filtro.material_vivienda) {
      this.materialSeleccionado = [...filtro.material_vivienda];
    }
  }

  form: FormGroup = this.fb.group({
    edadMin: [null],
    edadMax: [null],
    barrio: [null],
    acceso_salud: [null],
    acceso_agua: [null]
  });
  
  resetForm() {
    this.form.reset();
    this.generoSeleccionado = [];
    this.materialSeleccionado = [];
    this.reporteService.setFiltroActual(null);
    console.log('Filtro reseteado');
  }


  generos: string[] = ['mujer cis', 'mujer trans-travesti', 'varón cis', 'varon trans-masculinidad trans', 'no binarie', 'otra identidad-ninguna de las anteriores'];
  generoSeleccionado: string[] = [];

  materiales: string[] = ['ladrillo', 'madera', 'chapa', 'mixto', 'otros'];
  materialSeleccionado: string[] = [];

  barrios = this.barriosService.getBarrios();

  toggleGenero(genero: string, e: any) {
    if (e.target.checked) this.generoSeleccionado.push(genero);
    else this.generoSeleccionado = this.generoSeleccionado.filter(g => g !== genero);
  }

  toggleMaterial(material: string, e: any) {
    if (e.target.checked) this.materialSeleccionado.push(material);
    else this.materialSeleccionado = this.materialSeleccionado.filter(m => m !== material);
  }

  onSubmit() {
    const raw = this.form.value;
    const filtro: any = {};

    // Generar arreglo de edades dentro del rango
    if (raw.edadMin != null && raw.edadMax != null) {
      const edades: number[] = [];
      for (let i = raw.edadMin; i <= raw.edadMax; i++) {
        edades.push(i);
      }
      filtro.edad = edades;
    }

    if (this.generoSeleccionado.length) filtro.genero = this.generoSeleccionado;
    if (raw.barrio != null) filtro.barrio = raw.barrio;

    // Mapeo true -> "si", false -> "no"
    if (raw.acceso_salud !== undefined && raw.acceso_salud !== null) {
      filtro.acceso_salud = raw.acceso_salud ? "si" : "no";
    }

    if (raw.acceso_agua !== undefined && raw.acceso_agua !== null) {
      filtro.acceso_agua = raw.acceso_agua ? "si" : "no";
    }

    if (this.materialSeleccionado.length) filtro.material_vivienda = this.materialSeleccionado;

    this.generarReporte.emit(filtro);
  }

  openSaveDialog() {
    this.showSaveDialog = true;
  }

  onSaveWithName(name: any) {
    if (!name || name.trim() === '') {
      alert('El nombre del filtro no puede estar vacío');
      return;
    }
    this.showSaveDialog = false;
    this.guardando = true;
    
    const filtroData = this.buildFiltroData(name);
    
    this.filtroService.createFiltro(filtroData).subscribe({
      next: () => {
        this.guardando = false;
        alert('Filtro guardado correctamente');
      },
      error: (err) => {
        this.guardando = false;
        console.error('Error guardando filtro:', err);
        alert('Error al guardar el filtro');
      }
    });
  }

  private buildFiltroData(customName: string): Filtro {
    const raw = this.form.value;

    const criterios: any = {};

    // Edad: de min a max en array
    if (raw.edadMin != null && raw.edadMax != null) {
      const edades: number[] = [];
      for (let i = raw.edadMin; i <= raw.edadMax; i++) {
        edades.push(i);
      }
      criterios.edad = edades;
    }

    if (this.generoSeleccionado?.length) {
      criterios.genero = this.generoSeleccionado;
    }

    if (raw.barrio) {
      criterios.barrio = raw.barrio;
    }

    if (raw.acceso_salud === true || raw.acceso_salud === false) {
      criterios.acceso_salud = raw.acceso_salud ? "si" : "no";
    }

    if (raw.acceso_agua === true || raw.acceso_agua === false) {
      criterios.acceso_agua = raw.acceso_agua ? "si" : "no";
    }

    if (this.materialSeleccionado?.length) {
      criterios.material_vivienda = this.materialSeleccionado;
    }

    const now = new Date();
    const fechaHora = formatDate(now, 'yyyy-MM-dd_HH-mm-ss', 'en-US');

    return {
      nombre: `${customName}_${fechaHora}`,
      criterios: JSON.stringify(criterios),
      propietario_id: this.auth.getUsuarioId() || 0
    };
  }
}
