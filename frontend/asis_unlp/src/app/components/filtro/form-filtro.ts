import { Component, inject, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { BarriosService } from "../../services/barrios.service";
import { CommonModule } from "@angular/common";


@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  selector: 'app-filtro-reporte',
  templateUrl: './form-filtro.html',
  styleUrls: ['../../../styles.css'],
})
export class FiltroReporteComponent {
  private fb = inject(FormBuilder);
  private barriosService = inject(BarriosService);

  @Output() generarReporte = new EventEmitter<any>();

  form: FormGroup = this.fb.group({
    edadMin: [null],
    edadMax: [null],
    barrio: [null],
    acceso_salud: [null],
    acceso_agua: [null],
  });

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
    console.log("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
    const raw = this.form.value;
    const filtro: any = {};

    if (raw.edadMin != null && raw.edadMax != null) filtro.edad = [raw.edadMin, raw.edadMax];
    if (this.generoSeleccionado.length) filtro.genero = this.generoSeleccionado;
    if (raw.barrio != null) filtro.barrio = raw.barrio;
    if (raw.acceso_salud) filtro.acceso_salud = true;
    if (raw.acceso_agua) filtro.acceso_agua = true;
    if (this.materialSeleccionado.length) filtro.material_vivienda = this.materialSeleccionado;

    this.generarReporte.emit(filtro);
  }
}
