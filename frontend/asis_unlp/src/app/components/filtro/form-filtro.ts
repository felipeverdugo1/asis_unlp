import { Component, inject, Output, EventEmitter, OnInit, Input } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { BarriosService } from "../../services/barrios.service";
import { CommonModule } from "@angular/common";
import { ReporteService } from "../../services/reporte.service";
import { ConstantPool } from "@angular/compiler";


@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-filtro-reporte',
  templateUrl: './form-filtro.html',
  styleUrls: ['../../../styles.css'],
})
export class FiltroReporteComponent implements OnInit {
  private fb = inject(FormBuilder);
  private barriosService = inject(BarriosService);
  private reporteService = inject(ReporteService);

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

    if (raw.edadMin != null && raw.edadMax != null) filtro.edad = [raw.edadMin, raw.edadMax];
    if (this.generoSeleccionado.length) filtro.genero = this.generoSeleccionado;
    if (raw.barrio != null) filtro.barrio = raw.barrio;
    if (raw.acceso_salud != undefined && raw.acceso_salud != null) filtro.acceso_salud = raw.acceso_salud;
    if (raw.acceso_agua != undefined && raw.acceso_agua != null) filtro.acceso_agua = raw.acceso_agua;
    if (this.materialSeleccionado.length) filtro.material_vivienda = this.materialSeleccionado;

    this.generarReporte.emit(filtro);
  }

  guardarFiltro() {
    console.log("TODO - Guardar filtro en la db.")
  }
}
