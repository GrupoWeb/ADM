import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserTramites } from '../shared/user-tramites';
import { MaterializeAction } from 'angular2-materialize';
import { ExcelService } from '../shared/excel.service';
declare const $: any;


declare var Materialize:any;

@Component({
  selector: 'app-reporte-tramite',
  templateUrl: './reporte-tramite.component.html',
  styleUrls: ['./reporte-tramite.component.css']
})
export class ReporteTramiteComponent implements OnInit {
  httpUser: Subscription;
  searchUserForm: FormGroup;
  activate_loading: boolean = false;
  tramites = []
  pageSize: number = 10;
  currentPage: number = 1;
  total: number;
  btn_export: boolean

  constructor(private userExternal: UserTramites, private excelService: ExcelService) { }

  ngOnInit() {
    this.initUserForm();
    this.btn_export = false
  }


  onSearchUser(){
    this.activate_loading = true;
    const query =  this.searchUserForm.value.query
    if (!query) {
      Materialize.toast('Debe ingresar un criterio de búsqueda.', 4000);
      this.activate_loading = false;
      this.btn_export = false
      this.tramites = []
      return false;
    }


    this.httpUser = this.userExternal.getTramites(query).subscribe(
      res => {
        if(res == null){
          this.activate_loading = false;
          Materialize.toast('Código no encontrado', 4000);
        }else{
          this.tramites = res
          this.btn_export = true
          this.activate_loading = false;

        }
      }
    ),(error)=>{
      this.activate_loading = false;
    }
  }

  exportData(){
    const temp = this.tramites.map(el => ({
      "Garantía": el.idGarantia,
      "Trámite": el.idTramite,
      "Precio": el.precio,
      "Fecha": el.fecha,
      "Descripción": el.descripcion,
      "Año": el.anio
    }));
    this.excelService.export(temp, 'tramites');
  }


  private initUserForm() {
    this.searchUserForm = new FormGroup({
      'query': new FormControl(null, Validators.required)
    });

  }

}
