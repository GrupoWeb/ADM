import { Component, OnInit, ElementRef, ViewChild, EventEmitter, Input, ChangeDetectorRef,NgZone } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { DeudoresService } from '../shared/deudores.service';
import { LoadingService } from '../shared/loading.service';
import { MaterializeAction } from 'angular2-materialize';
import { NgForm } from '@angular/forms';
import * as moment from 'moment';
import { ExcelService } from '../shared/excel.service';
import { GarantiasService } from '../shared/garantias.service';
import { Acreedor } from '../shared/acreedores.model'
import { Deudor } from '../shared/deudores.model'
import { Garantias } from '../shared/garantias.model'
import { Usuario } from '../shared/usuarios.model';
import { AcreedoresService } from '../shared/acreedores.service';
import { UsuariosService } from '../shared/usuarios.service';
import { concat, finalize } from 'rxjs/operators';
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from 'ngx-daterangepicker';
import { PaginationInstance } from 'ngx-pagination';
import { garantia } from '../shared/garantias/garantia.model';
import { DecimalPipe } from '@angular/common';
@Component({
  selector: 'app-report-deudores',
  templateUrl: './report-deudores.component.html',
  styleUrls: ['./report-deudores.component.css']
})
export class ReportDeudoresComponent implements OnInit {
  public loading = false;
  index = 1;
  deudores: Deudor[];
  garantias: Garantias[];
  garantias_select: Garantias[];
  garantias_acre: Garantias[];
  garantias_usu: Garantias[];
  acreedores_b: Acreedor[];
  acreedores_filtro: any[] = [];
  filAcreedores: any[] = [];
  usuarios: Usuario[];
  exportDeudor: Deudor[];
  exportGarantia: Garantias[];
  exportAcreedor: Acreedor[];
  exportUsuario: Usuario[];
  acreedor_budqueda: String;
  usuario_busqueda: String;
  total: number;
  total2: number;
  total_acr: number;
  total_usr: number;
  pageSize: number = 10;
  currentPage: number = 1;
  currentPage2: number = 1;
  currentPage_acr: number = 1;
  currentPage_usr: number = 1;
  pageSize2: number = 10;
  pageSize_acr: number = 10;
  pageSize_usr: number = 10;
  currentPageG: number = 1;
  httpSubscription: Subscription;
  httpSubscription2: Subscription;
  httpSubscription_s: Subscription;
  exportHttpSubscription: Subscription;
  sumHttpSubscription: Subscription;
  filtro: Deudor;
  filtrog: Garantias;
  filtroa: Acreedor;
  filtrod: Deudor;
  filtrou: Usuario;
  localSubscription: Subscription;
  modRejectActions = new EventEmitter<string | MaterializeAction>();
  modDetallesActions = new EventEmitter<string | MaterializeAction>();
  modGarantiasActions = new EventEmitter<string | MaterializeAction>();
  modDetallesGActions = new EventEmitter<string | MaterializeAction>();
  modDetallesAcreActions = new EventEmitter<string | MaterializeAction>();
  modDetallesUsuActions = new EventEmitter<string | MaterializeAction>();
  modalDeposit: Deudor;
  modalIndex: number;
  fechaInicio: string;
  fechaFin: string;
  @ViewChild('f') rejectForm: NgForm;
  identi_garantia = '';
  nit_garantia = '';
  nomrec_garantia = '';

  identificador_Macreedor = '';
  nit_gMacreedor = '';
  nomrec_Macreedor = '';

  identificador_Musuario = '';
  nit_Musuario = '';
  nomrec_Musuario = '';

  nume_garantia = '';
  textob = '';
  m_identificacion = '';
  m_nit = '';
  m_nombre = '';
  m_tipo = '';
  m_nacionalidad = '';
  m_correo = '';
  m_domicilio = '';
  mg_numero = '';
  mg_movs = '';
  mg_inscr = '';
  mg_cert = '';
  mg_mod = '';
  mg_canc = '';
  mg_ejec = '';
  mg_prorr = '';
  mg_finscr = '';
  mg_factu = '';
  mg_operacion = '';
  mg_vigencia = '';
  mg_bienes = '';
  mg_deudor = '';
  mg_acreedor = '';
  mg_usuario = '';
  mg_estado = '';
  mg_solicitante = '';
  mg_soriginal = '';
  mg_detalle = '';
  totald_deudores = '';
  totald_modGarantia='';
  totald_modAcreedor = '';
  totald_modUsuarios = '';
  options: NgxDateRangePickerOptions;
  @Input() dateRange: NgxDateRangePickerComponent;
  @ViewChild('filAcreedorInput') filAcreedorInput: ElementRef;
  rangoFechas: string | any;
  manualDate: string;

  constructor(private deudorService: DeudoresService,
    private loadingService: LoadingService,
    private excelService: ExcelService,
    private garantiaService: GarantiasService,
    private acreedorService: AcreedoresService,
    private usuariosService: UsuariosService,
    private cdr: ChangeDetectorRef,
    private ngZone: NgZone
  ) {

  }

  ngOnInit() {
    this.acreedor_budqueda = "";
    const today = moment().toDate();
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };


    this.options = {
      theme: 'default',
      range: 'tm', // The alias of item menu
      labels: ['Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab', 'Dom'],
      menu: [
        { alias: 'td', text: 'Hoy', operation: '0d' },
        { alias: 'tm', text: 'Este mes', operation: '0m' },
        { alias: 'lm', text: 'Mes pasado', operation: '-1m' },
        { alias: 'tw', text: 'Esta semana', operation: '0w' },
        { alias: 'lw', text: 'Semana pasada', operation: '-1w' },
        { alias: 'ty', text: 'Este año', operation: '0y' },
        { alias: 'ly', text: 'Año pasado', operation: '-1y' },
      ],
      dateFormat: 'DD/MM/YYYY',
      outputFormat: 'YYYY-MM-DD',
      outputType: "object",
      startOfWeek: 0,
      locale: 'es',
    };
    //this.fechaInicio = moment().format('YYYY-MM-') + '01';
    //this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.filtro = new Deudor;
    this.filtrog = new Garantias;
    this.filtroa = new Acreedor;
    this.filtrou = new Usuario;
    this.deudorService.deudores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData(null, this.currentPage, this.pageSize, this.filtro).subscribe(
      res => {
        this.deudores = res.data;
        this.totald_deudores = String(res.total);
        this.total = res.total;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.localSubscription = this.deudorService.depositsChanged.subscribe(
      (deposits: Deudor[]) => {
        this.deudores = deposits;

        this.closeModalReject();
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChange(page: number) {
    this.deudorService.deudores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData(null, page, this.pageSize, this.filtro).subscribe(
      res => {
        this.deudores = res.data;
        this.total = res.total;
        this.totald_deudores= String(res.total);
        this.currentPage = page;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChangeG(page2: number) {
    this.modalIndex = 1;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, page2, this.pageSize2, this.filtrog).subscribe(
      res => {
        this.garantias = res.data;
        this.garantias.forEach(garantia => {
          const fecha = moment.utc(garantia.fInscr, "YYYY-MM-DDTHH:mm:ss[Z]");
          const fechaString = fecha.local().format('DD/MM/YYYY');
          const partesFecha = fechaString.split('/');
          const dia = parseInt(partesFecha[0], 10);
          const mes = parseInt(partesFecha[1], 10) - 1; // Los meses en JavaScript son base 0, por lo que restamos 1
          const anio = parseInt(partesFecha[2], 10);
          const fechaFormateada = new Date(anio, mes, dia);
          const date = new Date(fechaFormateada);
          garantia.fInscr = isValidDate(date) ? date : null;
        });
        this.total2 = res.total;
        this.totald_modGarantia = String(res.total);
        this.currentPage2 = page2;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    function padLeft(valor: number): string {
      return valor.toString().padStart(2, '0');
    }
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
    //this.modGarantiasActions.emit({action:"modal", params:['open']});
  }
  onPageChangeAcr(pageac: number) {
    console.log("onAcreedores");
    this.modalIndex = 1;
    //this.filtrog = new Garantias;
    //this.filtrog.idDeudor = String(deudor.idPersona);

    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("this.acreedor_budqueda: " + this.acreedor_budqueda);
          this.modalIndex = 1;
          const ultimoCaracter = this.acreedor_budqueda.slice(-1);
          if(ultimoCaracter==","){this.acreedor_budqueda = this.acreedor_budqueda.slice(0, -1);}
          this.filtroa.idAcreedor_b = String(this.acreedor_budqueda);
          this.acreedorService.acreedores = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.httpSubscription = this.acreedorService.fetchData_b(null, pageac, this.pageSize_acr, this.filtroa).subscribe(
            res => {
              this.totald_modAcreedor= String(res.total);
              this.acreedores_b = res.data;
              this.total_acr = res.total;
              this.currentPage_acr = pageac;
              //this.deudorService.addDeposits(this.deudores);
            },
            err => console.error(err),
            () => {
              this.loading = false;
              this.loadingService.changeLoading(this.loading);
            }
          );
        })
      )
      .subscribe(
        res => {
          this.acreedor_budqueda = '';
          this.garantias_acre = res.data;
          this.garantias_acre.forEach(garantiaAcr => {
            if (res.data == 1) {
              this.acreedor_budqueda = garantiaAcr.idAcreedor;
            } else {
              this.acreedor_budqueda = garantiaAcr.idAcreedor + "," + this.acreedor_budqueda;
            }

          });
        }
      );


  }
  onPageChangeUsr(pageusr: number) {
    console.log("onUsuarios");
    this.modalIndex = 1;
    /*this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);*/
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("this.usuario_busqueda: " + this.usuario_busqueda);
          this.modalIndex = 1;
          const ultimoCaracter = this.usuario_busqueda.slice(-1);
          if(ultimoCaracter==","){this.usuario_busqueda = this.usuario_busqueda.slice(0, -1);}
          this.filtrou.idUsuario_b = String(this.usuario_busqueda);
          this.usuariosService.usuarios = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.httpSubscription = this.usuariosService.fetchData_b(null, pageusr, this.pageSize_usr, this.filtrou).subscribe(
            res => {
              this.totald_modUsuarios = String(res.total);
              this.usuarios = res.data;
              this.total_usr = res.total;
              this.currentPage_usr = pageusr;
              //this.deudorService.addDeposits(this.deudores); 
            },
            err => console.error(err),
            () => {
              this.loading = false;
              this.loadingService.changeLoading(this.loading);
            }
          );
        })
      )
      .subscribe(
        res => {
          this.usuario_busqueda = '';
          this.garantias_usu = res.data;
          this.garantias_usu.forEach(garantiaUser => {
            if (res.data == 1) {
              this.usuario_busqueda = garantiaUser.idUsuario;
            } else {
              this.usuario_busqueda = garantiaUser.idUsuario + "," + this.usuario_busqueda;
            }

          });
        }
      );

  }

  refreshData() {
    this.filtro.textobusqueda = this.textob;
    this.deudorService.deudores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData(null, 1, this.pageSize, this.filtro).subscribe(
      res => {
        this.deudores = res.data;
        this.totald_deudores = String(res.total);
        this.total = res.total;
        this.currentPage = 1;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  exportData() {
    this.exportDeudor = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData(null, null, null, this.filtro).subscribe(

      res => {
        this.exportDeudor = res.data;
        const temp = this.exportDeudor.map(el => ({
          "Fecha": moment(new Date(), 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Identificación": el.idPersona,
          "Nit": el.rfc,
          "Nombre Completo": el.nombrePersona,
          "Tipo de Persona": el.perJuridica,
          "Nacionalidad": el.nomNacionalidad,
          "Correo electronico": el.email == 'N/A' ? '' : el.email,
          "Domicilio": el.direccion,
          "Garantias": el.cGarantias,
          "Vigentes": el.cVigentes,
          "No Vigentes": el.cnVigentes,
          "Canceladas": el.cCanceladas
        }));
        this.excelService.export(temp, 'deudores');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportDataG() {
    this.exportGarantia = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog).subscribe(

      res => {
        this.exportGarantia = res.data;
        const temp = this.exportGarantia.map(el => ({
          "Fecha": moment(new Date(), 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "idGarantia": el.idGarantia,
          "movs": el.movs,
          "inscr": el.inscr,
          "cert": el.cert,
          "modif": el.modif,
          "cancelado": el.cancelado,
          "ejec": el.ejec,
          "fInscr": el.fInscr,
          "idTramite": el.idTramite,
          "dTramite": el.dTramite,
          "vigencia": el.vigencia,
          //          "dGarantia":el.dGarantia, da error porque es muy larga para la celda length must not exceed 32767 characters
          "gStatus": el.gStatus,
          "solicitante": el.solicitante,
          "sOriginal": el.sOriginal,
          "idDeudor": el.idDeudor,
          "nDeudor": el.nDeudor,
          "idAcreedor": el.idAcreedor,
          "nAcreedor": el.nAcreedor,
          "idUsuario": el.idUsuario,
          "nUsuario": el.nUsuario,

        }));
        this.excelService.export(temp, 'garantias');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportDataAcre() {
    this.exportAcreedor = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData(null, null, null, this.filtroa).subscribe(

      res => {
        this.exportAcreedor = res.data;
        const temp = this.exportAcreedor.map(el => ({
          "Fecha": moment(new Date(), 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Identificación": el.idPersona,
          "Nit": el.rfc,
          "Nombre Completo": el.nombrePersona,
          "Tipo de Persona": el.perJuridica,
          "Nacionalidad": el.nomNacionalidad,
          "Correo electronico": el.email == 'N/A' ? '' : el.email,
          "Domicilio": el.direccion,
          "Garantias": el.cGarantias,
          "Vigentes": el.cVigentes,
          "No Vigentes": el.cnVigentes,
          "Canceladas": el.cCanceladas
        }));
        this.excelService.export(temp, 'acreedores');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportDataUser() {
    this.exportUsuario = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usuariosService.fetchData(null, null, null, this.filtrou).subscribe(

      res => {
        this.exportUsuario = res.data;
        const temp = this.exportUsuario.map(el => ({
          "Fecha": moment(new Date(), 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Identificación": el.idPersona,
          "Nit": el.rfc,
          "Nombre Completo": el.nombrePersona,
          "Tipo de Persona": el.perJuridica,
          "Nacionalidad": el.nomNacionalidad,
          "Correo electronico": el.email == 'N/A' ? '' : el.email,
          "Domicilio": el.direccion,
          "Garantias": el.cGarantias,
          "Vigentes": el.cVigentes,
          "No Vigentes": el.cnVigentes,
          "Canceladas": el.cCanceladas
        }));
        this.excelService.export(temp, 'usuarios');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onRevertClicked(deudor: Deudor, i: number) {
    this.modalDeposit = deudor;
    this.modalIndex = i;

    this.modRejectActions.emit({ action: "modal", params: ['open'] });
  }
  onDetalles(deudor: Deudor, i: number) {
    this.modalDeposit = deudor;
    this.modalIndex = 1;
    this.m_identificacion = String(deudor.idPersona);
    //this.m_nit = String(deudor.rfc);
    this.m_nit = deudor.rfc !== null && deudor.rfc !== undefined ? String(deudor.rfc) : '';   
    //this.m_nombre = String(deudor.nombrePersona);
    this.m_nombre = deudor.nombrePersona !== null && deudor.nombrePersona !== undefined ? String(deudor.nombrePersona) : '';   
    this.m_tipo = String(deudor.perJuridica);
//    this.m_nacionalidad = String(deudor.nomNacionalidad);
    this.m_nacionalidad = deudor.nomNacionalidad !== null && deudor.nomNacionalidad !== undefined ? String(deudor.nomNacionalidad) : '';   
    //this.m_correo = String(deudor.email);
    this.m_correo = deudor.email !== null && deudor.email !== undefined ? String(deudor.email) : '';   
    //this.m_domicilio = String(deudor.direccion);
    this.m_domicilio = deudor.direccion !== null && deudor.direccion !== undefined ? String(deudor.direccion) : '';   
    
    this.modDetallesActions.emit({ action: "modal", params: ['open'] });
  }
  llenar_select(){
    /* Trae todos los acreedores de este deudor sin paginar*/
    console.log("Aqui va a cargar el select");
    this.httpSubscription_s = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("Ya se cargo");
          this.cdr.detectChanges(); 
          this.filAcreedores=this.acreedores_filtro;
        })
      )
      .subscribe(
        res => {
          this.acreedores_filtro = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.garantias_select = res.data;
          console.log(res.data);
          this.garantias_select.forEach(garantia_s => {

            this.index = this.index + 1;
            var existe = this.acreedores_filtro.some((acreedor: any) => acreedor.label === garantia_s.nAcreedor);

            if (!existe) {
              this.acreedores_filtro.push({
                value: garantia_s.idAcreedor,
                label: garantia_s.nAcreedor
              });
            }
          });
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
  
  }
  onGarantias(deudor: Deudor, i: number) {

    this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);
    this.modalIndex = 1;
    this.garantiaService.garantias = [];
    this.loading = true;
    this.acreedores_filtro = [];
    this.loadingService.changeLoading(this.loading);
    this.llenar_select();
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
      .pipe(
        finalize(() => {
          
        }
      )
      )
      .subscribe(
        res => {
          this.garantias = res.data;
          this.garantias.forEach(garantia => {
            console.log(garantia);
            const fecha = moment.utc(garantia.fInscr, "YYYY-MM-DDTHH:mm:ss[Z]");
            const fechaString = fecha.local().format('DD/MM/YYYY');
            const partesFecha = fechaString.split('/');
            const dia = parseInt(partesFecha[0], 10);
            const mes = parseInt(partesFecha[1], 10) - 1; // Los meses en JavaScript son base 0, por lo que restamos 1
            const anio = parseInt(partesFecha[2], 10);
            const fechaFormateada = new Date(anio, mes, dia);
            const date = new Date(fechaFormateada);
            garantia.fInscr = isValidDate(date) ? date : null;
          });
          this.total2 = res.total;
          this.totald_modGarantia = String(res.total);
          this.currentPage2 = 1;
          this.identi_garantia = String(deudor.idPersona);
          //this.nit_garantia = deudor.rfc;
          this.nit_garantia = deudor.rfc !== null && deudor.rfc !== undefined ? String(deudor.rfc) : '';   
          this.nomrec_garantia = deudor.nombrePersona;
          this.nomrec_garantia = deudor.nombrePersona !== null && deudor.nombrePersona !== undefined ? String(deudor.nombrePersona) : '';
          //this.deudorService.addDeposits(this.deudores);
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
    function padLeft(valor: number): string {
      return valor.toString().padStart(2, '0');
    }
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
    this.modGarantiasActions.emit({ action: "modal", params: ['open'] });
  }
  onAcreedores_b(deudor: Deudor, i: number) {
    console.log("onAcreedores");
    this.identificador_Macreedor = String(deudor.idPersona);
    //this.nit_gMacreedor = String(deudor.rfc);
    this.nit_gMacreedor = deudor.rfc !== null && deudor.rfc !== undefined ? String(deudor.rfc) : '';   
    this.nomrec_Macreedor = String(deudor.nombrePersona);
    this.nomrec_Macreedor = deudor.nombrePersona !== null && deudor.nombrePersona !== undefined ? String(deudor.nombrePersona) : '';   
    this.modalIndex = 1;
    this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("this.acreedor_budqueda: " + this.acreedor_budqueda);
          this.modalIndex = 1;
          const ultimoCaracter = this.acreedor_budqueda.slice(-1);
          if(ultimoCaracter==","){this.acreedor_budqueda = this.acreedor_budqueda.slice(0, -1);}
          this.filtroa.idAcreedor_b = String(this.acreedor_budqueda);
          this.acreedorService.acreedores = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.httpSubscription = this.acreedorService.fetchData_b(null, 1, this.pageSize_acr, this.filtroa).subscribe(
            res => {
              this.totald_modAcreedor= String(res.total);
              this.acreedores_b = res.data;
              this.total_acr = res.total;
              this.currentPage_acr = 1;
              //this.deudorService.addDeposits(this.deudores);
            },
            err => console.error(err),
            () => {
              this.loading = false;
              this.loadingService.changeLoading(this.loading);
            }
          );
        })
      )
      .subscribe(
        res => {
          this.acreedor_budqueda = '';
          this.garantias_acre = res.data;
          this.garantias_acre.forEach(garantiaAcr => {
            if (res.data == 1) {
              this.acreedor_budqueda = garantiaAcr.idAcreedor;
            } else {
              this.acreedor_budqueda = garantiaAcr.idAcreedor + "," + this.acreedor_budqueda;
            }

          });
        }
      );



    this.modDetallesAcreActions.emit({ action: "modal", params: ['open'] });
  }
  onDetallesG(garantia: Garantias, i: number) {
    this.modalIndex = 2;
    this.closeModalGarantia();
    this.mg_numero = String(garantia.idGarantia);
    this.mg_movs = String(garantia.movs);
    this.mg_inscr = String(garantia.inscr);
    this.mg_cert = String(garantia.cert);
    this.mg_mod = String(garantia.modif);
    this.mg_canc = String(garantia.cancelado);
    this.mg_ejec = String(garantia.ejec);
    this.mg_prorr = garantia.prorr !== null && garantia.prorr !== undefined ? String(garantia.prorr) : '';
    this.mg_finscr = String(garantia.fInscr);
    //this.mg_factu = String("");
    this.mg_operacion = String(garantia.dTramite);
    this.mg_vigencia = String(garantia.vigencia);
    //this.mg_bienes = String("");
    this.mg_deudor = String(garantia.nDeudor);
    this.mg_acreedor = String(garantia.nAcreedor);
    this.mg_usuario = String(garantia.nUsuario);
    this.mg_estado = String(garantia.gStatus);
    //this.mg_solicitante = String(garantia.solicitante);
    //this.mg_soriginal = String(garantia.sOriginal);
    this.mg_soriginal = garantia.sOriginal !== null && garantia.sOriginal !== undefined ? String(garantia.sOriginal) : '';   
    //this.mg_detalle = String(garantia.dGarantia);
    this.mg_detalle = garantia.dGarantia !== null && garantia.dGarantia !== undefined ? String(garantia.dGarantia) : '';   


    this.modDetallesGActions.emit({ action: "modal", params: ['open'] });
  }
  onUsuarios(deudor: Deudor, i: number) {
    console.log("onUsuarios");
    this.identificador_Musuario = String(deudor.idPersona);
    //this.nit_Musuario = String(deudor.rfc);
    this.nit_Musuario = deudor.rfc !== null && deudor.rfc !== undefined ? String(deudor.rfc) : '';   
    //this.nomrec_Musuario = String(deudor.nombrePersona);
    this.nomrec_Musuario = deudor.nombrePersona !== null && deudor.nombrePersona !== undefined ? String(deudor.nombrePersona) : '';   

    this.modalIndex = 1;
    this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("this.usuario_busqueda: " + this.usuario_busqueda);
          const ultimoCaracter = this.usuario_busqueda.slice(-1);
          if(ultimoCaracter==","){this.usuario_busqueda = this.usuario_busqueda.slice(0, -1);}
          this.modalIndex = 1;
          this.filtrou.idUsuario_b = String(this.usuario_busqueda);
          this.usuariosService.usuarios = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.httpSubscription = this.usuariosService.fetchData_b(null, 1, this.pageSize_usr, this.filtrou).subscribe(
            res => {
              this.totald_modUsuarios = String(res.total);
              this.usuarios = res.data;
              this.total_usr = res.total;
              this.currentPage_usr = 1;
              //this.deudorService.addDeposits(this.deudores);
            },
            err => console.error(err),
            () => {
              this.loading = false;
              this.loadingService.changeLoading(this.loading);
            }
          );
        })
      )
      .subscribe(
        res => {
          this.usuario_busqueda = '';
          this.garantias_usu = res.data;
          this.garantias_usu.forEach(garantiaUser => {
            if (res.data == 1) {
              this.usuario_busqueda = garantiaUser.idUsuario;
            } else {
              this.usuario_busqueda = garantiaUser.idUsuario + "," + this.usuario_busqueda;
            }

          });
        }
      );

    this.modDetallesUsuActions.emit({ action: "modal", params: ['open'] });
  }
  closeModalReject() {
    this.modRejectActions.emit({ action: "modal", params: ['close'] });
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalDetalles() {
    this.modDetallesActions.emit({ action: "modal", params: ['close'] });
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalAcreedores() {
    this.modDetallesAcreActions.emit({ action: "modal", params: ['close'] });
    this.modalIndex = -1;

  }
  closeModalDetallesG() {
    this.modDetallesGActions.emit({ action: "modal", params: ['close'] });
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalGarantia() {
    this.modGarantiasActions.emit({ action: "modal", params: ['close'] });
    this.modalIndex = -1;

  }
  closeModalUsuarios() {
    this.modDetallesUsuActions.emit({ action: "modal", params: ['close'] });
    this.modalIndex = -1;

  }
  onRangeChanged(event) {

    if (this.fechaInicio !== event.from || this.fechaFin !== event.to) {
      this.filtrog.fechaInicio = event.from
      this.filtrog.fechaFin = event.to
      this.fechaInicio = event.from;
      this.fechaFin = event.to;
      //this.refreshData();
    }
  }
  filAcreedorChanged(filAcreedor: String) {
    console.log(filAcreedor);
    this.filtrog.idAcreedor = filAcreedor;
    //this.BuscarData();
  }
  BuscarData() {

    //this.filtrog = new Garantias;
    this.filtrog.idGarantia = parseInt(this.nume_garantia);
    this.filtrog.idDeudor = this.identi_garantia;
    this.filtrog.fechaInicio = this.fechaInicio;
    this.filtrog.fechaFin = this.fechaFin;

    this.httpSubscription = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog).subscribe(
      res => {
        this.garantias = res.data;
        this.garantias.forEach(garantia => {
          const fecha = moment.utc(garantia.fInscr, "YYYY-MM-DDTHH:mm:ss[Z]");
          const fechaString = fecha.local().format('DD/MM/YYYY');
          const partesFecha = fechaString.split('/');
          const dia = parseInt(partesFecha[0], 10);
          const mes = parseInt(partesFecha[1], 10) - 1; // Los meses en JavaScript son base 0, por lo que restamos 1
          const anio = parseInt(partesFecha[2], 10);
          const fechaFormateada = new Date(anio, mes, dia);
          const date = new Date(fechaFormateada);
          garantia.fInscr = isValidDate(date) ? date : null;
        });

        this.total2 = res.total;
        this.totald_modGarantia = String(res.total);
        this.currentPage2 = 1;

        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
  }
  LimpiarData() {
    this.nume_garantia = '';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.filtrog = new Garantias;
    this.filtrog.idDeudor = this.identi_garantia;
    const today = moment().toDate();
    this.filAcreedorInput.nativeElement.selectedIndex = 0;
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
      .pipe(
        finalize(() => {
          /* Trae todos los acreedores de este deudor sin paginar*/
          /* this.httpSubscription = this.garantiaService.fetchData_all_acr(null, null, null, this.filtrog).subscribe(
             res => {
               this.garantias_select = res.data;
               this.garantias_select.forEach(garantia_s => {  
                 
                 this.index = this.index+1;
                 const existe = this.acreedores_filtro.some((acreedor: any) => acreedor.label === garantia_s.nAcreedor);
                 
     
                 if (!existe) {
                   this.acreedores_filtro.push({
                     value: garantia_s.idAcreedor,
                     label: garantia_s.nAcreedor
                   });
                 }
                      
               });
             },
             err => console.error(err),
             () => {
               this.loading = false;
               this.loadingService.changeLoading(this.loading);
             }
           );*/
        })
      )
      .subscribe(
        res => {
          this.garantias = res.data;
          this.garantias.forEach(garantia => {
            const fecha = moment.utc(garantia.fInscr, "YYYY-MM-DDTHH:mm:ss[Z]");
            const fechaString = fecha.local().format('DD/MM/YYYY');
            const partesFecha = fechaString.split('/');
            const dia = parseInt(partesFecha[0], 10);
            const mes = parseInt(partesFecha[1], 10) - 1; // Los meses en JavaScript son base 0, por lo que restamos 1
            const anio = parseInt(partesFecha[2], 10);
            const fechaFormateada = new Date(anio, mes, dia);
            const date = new Date(fechaFormateada);
            garantia.fInscr = isValidDate(date) ? date : null;
          });
          this.total2 = res.total;
          this.totald_modGarantia = String(res.total);
          this.currentPage2 = 1;

        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
    function padLeft(valor: number): string {
      return valor.toString().padStart(2, '0');
    }
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
  }


}
