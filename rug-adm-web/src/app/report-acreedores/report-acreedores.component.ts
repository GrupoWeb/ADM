import { Component, OnInit, ElementRef, ViewChild, EventEmitter ,Input,ChangeDetectorRef} from '@angular/core';
import { Deudor } from '../shared/deudores.model'
import { DeudoresService } from '../shared/deudores.service';
import { Garantias } from '../shared/garantias.model'
import { Subscription } from 'rxjs/Subscription';
import { LoadingService } from '../shared/loading.service';
import { MaterializeAction } from 'angular2-materialize';
import { NgForm } from '@angular/forms';
import * as moment from 'moment';
import { ExcelService } from '../shared/excel.service';
import { GarantiasService } from '../shared/garantias.service';
import { Acreedor } from '../shared/acreedores.model'
import { AcreedoresService } from '../shared/acreedores.service';
import { Usuario } from '../shared/usuarios.model';
import { UsuariosService } from '../shared/usuarios.service';
import { concat, finalize } from 'rxjs/operators';
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from 'ngx-daterangepicker';
@Component({
  selector: 'app-report-acreedores',
  templateUrl: './report-acreedores.component.html',
  styleUrls: ['./report-acreedores.component.css']
})
export class ReportAcreedoresComponent implements OnInit {
  index =1;
  public loading = false;
  deudores: Deudor[];
  garantias: Garantias[];
  garantias_select: Garantias[];
  garantias_deu: Garantias[];
  garantias_usu: Garantias[];
  acreedores: Acreedor[];
  deudores_b: Deudor[];
  usuarios: Usuario[];
  deudor_budqueda:String;
  user_budqueda:String;
  exportAcreedor: Acreedor[];
  exportDeudor: Deudor[];
  exportUsuario: Usuario[];
  filtrou: Usuario;
  total: number;
  total2: number;
  total_deu: number;
  total_usr : number;
  pageSize: number = 10;
  currentPage: number = 1;
  currentPage2: number = 1;
  currentPage_deu: number = 1;
  currentPage_usr: number = 1;
  pageSize2: number = 10;
  pageSize_deu: number = 10;
  pageSize_usr: number = 10;
  currentPageG: number = 1;

  identi_garantia = '';
  nit_garantia = '';
  nomrec_garantia = '';
  
    
  identificador_Musuario = '';
  nit_Musuario = '';
  nomrec_Musuario = '';

  deudores_filtro: any[] = [];
  filAcreedores: any[] = [];
  filDeudores: any[] = [];
  httpSubscription: Subscription;
  httpSubscription2: Subscription;
  httpSubscription_s: Subscription;
  exportHttpSubscription: Subscription;
  sumHttpSubscription: Subscription;
  filtro: Deudor;
  filtrog: Garantias;
  filtroa: Acreedor;
  localSubscription: Subscription;
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modDetallesActions = new EventEmitter<string|MaterializeAction>();
  modGarantiasActions = new EventEmitter<string|MaterializeAction>();
  modDetallesGActions = new EventEmitter<string|MaterializeAction>();
  modDetallesDActions = new EventEmitter<string|MaterializeAction>();
  modDetallesDeudorActions = new EventEmitter<string|MaterializeAction>();
  modDetallesUsuActions = new EventEmitter<string|MaterializeAction>();
  
  //modalDeposit: Deudor;
  //modalIndex: number;

  fechaInicio: string;
  fechaFin: string;
  nume_garantia = '';
  acreedores_filtro: any[] = [];

  usuario_busqueda:String;

  @ViewChild('f') rejectForm: NgForm;
  textob='';
  m_identificacion='';
  m_nit= '';
  m_nombre = '';
    m_tipo = '';
  m_nacionalidad= '';
  m_correo = '';
  m_domicilio = '';
  mg_numero= '';
  mg_movs= '';
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
  options: NgxDateRangePickerOptions;
  @Input() dateRange: NgxDateRangePickerComponent;
  @ViewChild('filDeudorInput') filDeudorInput: ElementRef;
  rangoFechas: string | any;
  manualDate: string;

  exportGarantia: Garantias[];

  identificador_Macreedor = '';
  nit_gMacreedor = '';
  nomrec_Macreedor = '';
  totald_acreedores = '';
  totala_modGarantia='';
  totala_modDeudores = '';
  totala_modUsuarios = '';

  constructor(
    private deudorService: DeudoresService,
    private loadingService: LoadingService,
    private excelService: ExcelService,
    private garantiaService: GarantiasService,
    private acreedorService: AcreedoresService,
    private usuariosService: UsuariosService,
    private cdr: ChangeDetectorRef,
  ) { }

  ngOnInit() {
    this.filtro = new Deudor;
    this.filtrog = new Garantias;
    this.filtroa = new Acreedor;
    this.filtrou = new Usuario;
    this.acreedorService.acreedores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
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
          {alias: 'td', text: 'Hoy', operation: '0d'},
          {alias: 'tm', text: 'Este mes', operation: '0m'},
          {alias: 'lm', text: 'Mes pasado', operation: '-1m'},
          {alias: 'tw', text: 'Esta semana', operation: '0w'},
          {alias: 'lw', text: 'Semana pasada', operation: '-1w'},
          {alias: 'ty', text: 'Este año', operation: '0y'},
          {alias: 'ly', text: 'Año pasado', operation: '-1y'},
      ],
      dateFormat: 'DD/MM/YYYY',
      outputFormat: 'YYYY-MM-DD',
      outputType: "object",
      startOfWeek: 0,
      locale: 'es',
    };
    this.httpSubscription = this.acreedorService.fetchData(null, this.currentPage, this.pageSize, this.filtroa).subscribe(
      res => {
        this.acreedores = res.data;
        this.totald_acreedores = String(res.total);
        this.total = res.total;        
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );    
  }
  refreshData() {
    
    console.log(this.textob);
    this.filtroa.textobusqueda=this.textob;
    this.acreedorService.acreedores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData(null, 1, this.pageSize, this.filtroa).subscribe(
      res => {
        this.acreedores = res.data;
        this.totald_acreedores = String(res.total);
        this.total = res.total;
        this.currentPage = 1;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  exportData() {
    this.exportAcreedor = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData(null, null, null, this.filtroa).subscribe(
      
      res => {
        this.exportAcreedor = res.data;
       console.log(this.exportAcreedor)
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
        this.excelService.export(temp, 'deudores');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onDetalles(acreedor: Acreedor, i: number) {    
    console.log(acreedor)    
    this.m_identificacion = String(acreedor.idPersona);
    //this.m_nit = String(acreedor.rfc);
    this.m_nit = acreedor.rfc !== null && acreedor.rfc !== undefined ? String(acreedor.rfc) : '';   
    //this.m_nombre = String(acreedor.nombrePersona);
    this.m_nombre = acreedor.nombrePersona !== null && acreedor.nombrePersona !== undefined ? String(acreedor.nombrePersona) : '';   
    this.m_tipo = String(acreedor.perJuridica);
//    this.m_nacionalidad = String(acreedor.nomNacionalidad);
    this.m_nacionalidad = acreedor.nomNacionalidad !== null && acreedor.nomNacionalidad !== undefined ? String(acreedor.nomNacionalidad) : '';   
    //this.m_correo = String(acreedor.email);
    this.m_correo = acreedor.email !== null && acreedor.email !== undefined ? String(acreedor.email) : '';   
//    this.m_domicilio = String(acreedor.direccion);
    this.m_domicilio = acreedor.direccion !== null && acreedor.direccion !== undefined ? String(acreedor.direccion) : '';   

    this.modDetallesActions.emit({action:"modal", params:['open']});
  }
  closeModalDetalles() {
    this.modDetallesActions.emit({action:"modal",params:['close']});    
  }
  llenar_select(){
    /* Trae todos los acreedores de este deudor sin paginar*/
    console.log("Aqui va a cargar el select");
    this.httpSubscription_s = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          console.log("Ya se cargo");
          this.cdr.detectChanges(); 
          this.filDeudores=this.deudores_filtro;
        })
      )
      .subscribe(
        res => {
          this.deudores_filtro = [];
          this.loading = true;
          this.loadingService.changeLoading(this.loading);
          this.garantias_select = res.data;
          console.log(res.data);
          this.garantias_select.forEach(garantia_s => {

            this.index = this.index + 1;
            var existe = this.deudores_filtro.some((deudor: any) => deudor.label === garantia_s.nDeudor);

            if (!existe) {
              this.deudores_filtro.push({
                value: garantia_s.idDeudor,
                label: garantia_s.nDeudor
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
  onGarantias(acreedor: Acreedor, i: number) {

    this.filtrog = new Garantias;
    this.filtrog.idAcreedor = String(acreedor.idPersona);      
    //this.modalIndex = 1;    
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.llenar_select();
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
    .pipe(
      finalize(() => {
        /* Trae todos los deudores de este deudor sin paginar*/
      
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
        this.totala_modGarantia = String(res.total);
        this.currentPage2 = 1;
        this.identi_garantia=String(acreedor.idPersona);
        //this.nit_garantia=acreedor.rfc;
        this.nit_garantia = acreedor.rfc !== null && acreedor.rfc !== undefined ? String(acreedor.rfc) : '';   
        //this.nomrec_garantia=acreedor.nombrePersona;
        this.nomrec_garantia = acreedor.nombrePersona !== null && acreedor.nombrePersona !== undefined ? String(acreedor.nombrePersona) : '';   
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
    this.modGarantiasActions.emit({action:"modal", params:['open']});
  }
  closeModalGarantia() {
    this.modGarantiasActions.emit({action:"modal",params:['close']});
  }fetchData_b
  onDetallesG(garantia: Garantias, i: number) {    
    this.closeModalGarantia();
    console.log(garantia)    
    this.mg_numero = String(garantia.idGarantia);
    this.mg_movs = String(garantia.movs);
    this.mg_inscr = String(garantia.inscr);
    this.mg_cert = String(garantia.cert);
    this.mg_mod = String(garantia.modif);
    this.mg_canc = String(garantia.cancelado);
    this.mg_ejec = String(garantia.ejec);
    //this.mg_prorr = String("");
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
    this.mg_detalle = garantia.dGarantia !== null && garantia.dGarantia !== undefined ? String(garantia.dGarantia) : '';   

    this.modDetallesGActions.emit({action:"modal", params:['open']});
  }
  closeModalDetallesG() {
    this.modDetallesGActions.emit({action:"modal",params:['close']});
  }
  closeModalAcreedores() {
    this.modDetallesDeudorActions.emit({action:"modal",params:['close']});    
    
    
  }
  onDeudores(acreedor: Acreedor, i: number) {
    
    this.filtrog.textobusqueda = String(acreedor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    
    this.httpSubscription = this.garantiaService.fetchData_acr(null, 1, this.pageSize, this.filtrog).subscribe(
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
          
          this.deudor_budqueda =garantia.idDeudor;
         
      
      this.filtro.textobusqueda = String(this.deudor_budqueda);
      this.deudorService.deudores = [];
      this.loading = true;
      this.loadingService.changeLoading(this.loading);
      this.httpSubscription = this.deudorService.fetchData_b(null, 1, this.pageSize, this.filtro).subscribe(
      res => {
        
        this.deudores = res.data;      
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
    function padLeft(valor: number): string {
      return valor.toString().padStart(2, '0');
    }
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
        });
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
    
    
    
    this.modDetallesDActions.emit({action:"modal", params:['open']});
  }
  closeModalDetallesD() {
    this.modDetallesDActions.emit({action:"modal",params:['close']});
  }
  onUser(acreedor: Acreedor, i: number) {
    
    this.filtrog.textobusqueda = String(acreedor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    
    this.httpSubscription = this.garantiaService.fetchData_acr(null, 1, this.pageSize, this.filtrog).subscribe(
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
          
          this.user_budqueda =garantia.idUsuario;
          console.log("ESTE UNO:"+this.deudor_budqueda)
          console.log(this.deudor_budqueda);
          console.log("ESTE DOS:"+this.deudor_budqueda)
      
    this.filtro.textobusqueda = String(this.user_budqueda);
    this.deudorService.deudores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData_b(null, 1, this.pageSize, this.filtro).subscribe(
      res => {
        
        this.deudores = res.data;      
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
    function padLeft(valor: number): string {
      return valor.toString().padStart(2, '0');
    }
    function isValidDate(date: Date): boolean {
      return date instanceof Date && !isNaN(date.getTime());
    }
        });
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
    
    
    
    this.modDetallesDActions.emit({action:"modal", params:['open']});
  }
  onPageChange(page: number) {
    this.acreedorService.acreedores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData(null, page, this.pageSize, this.filtroa).subscribe(
      res => {
        this.acreedores = res.data;
        this.totald_acreedores = String(res.total);
        this.total = res.total;
        this.currentPage = page;        
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
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
  BuscarData(){
    
    //this.filtrog = new Garantias;
    this.filtrog.idGarantia=parseInt(this.nume_garantia);
    this.filtrog.idAcreedor=this.identi_garantia;
    this.filtrog.fechaInicio=this.fechaInicio;
    this.filtrog.fechaFin=this.fechaFin;
    
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
        this.totala_modGarantia = String(res.total);
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
  LimpiarData(){
    this.nume_garantia='';
    this.fechaInicio = '';
    this.fechaFin = '';
    this.filtrog = new Garantias;
    this.filtrog.idAcreedor = this.identi_garantia;
    const today = moment().toDate();
    this.filDeudorInput.nativeElement.selectedIndex = 0;
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
    .pipe(
      finalize(() => {
        /* Trae todos los acreedores de este deudor sin paginar*/
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
        this.totala_modGarantia = String(res.total);
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
  filDeudoresChanged(filDeudor: String) {
    console.log(filDeudor);
    this.filtrog.idDeudor = filDeudor;
    //this.refreshData();
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
          "idGarantia":el.idGarantia,
          "movs":el.movs,
          "inscr":el.inscr,
          "cert":el.cert,
          "modif":el.modif,
          "cancelado":el.cancelado,
          "ejec":el.ejec,
          "fInscr":el.fInscr,
          "idTramite":el.idTramite,
          "dTramite":el.dTramite,
          "vigencia":el.vigencia,
 //          "dGarantia":el.dGarantia, da error porque es muy larga para la celda length must not exceed 32767 characters
          "gStatus":el.gStatus,
          "solicitante":el.solicitante,
          "sOriginal":el.sOriginal,
          "idDeudor":el.idDeudor,
          "nDeudor":el.nDeudor,
          "idAcreedor":el.idAcreedor,
          "nAcreedor":el.nAcreedor,
          "idUsuario":el.idUsuario,
          "nUsuario":el.nUsuario,
               
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
  onDeudores_b(acreedor: Acreedor, i: number) {
    console.log("onDeudores");
    this.identificador_Macreedor = String(acreedor.idPersona);
    //this.nit_gMacreedor = String(acreedor.rfc);
    this.nit_gMacreedor = acreedor.rfc !== null && acreedor.rfc !== undefined ? String(acreedor.rfc) : '';   
    //this.nomrec_Macreedor = String(acreedor.nombrePersona);
    this.nomrec_Macreedor = acreedor.nombrePersona !== null && acreedor.nombrePersona !== undefined ? String(acreedor.nombrePersona) : '';   
    this.filtrog = new Garantias;
    this.filtrog.idAcreedor = String(acreedor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        console.log("this.deudor_budqueda: "+ this.deudor_budqueda);    
        const ultimoCaracter = this.deudor_budqueda.slice(-1);
        if(ultimoCaracter==","){this.deudor_budqueda = this.deudor_budqueda.slice(0, -1);}    
        this.filtro.idDeudor_b = String(this.deudor_budqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.deudorService.fetchData_b(null, 1, this.pageSize_deu, this.filtro).subscribe(
          res => {
            this.totala_modDeudores=String(res.total);
            this.deudores_b = res.data;      
            this.total_deu = res.total;
            this.currentPage_deu = 1;
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
        this.deudor_budqueda = '';
        this.garantias_deu = res.data;
        this.garantias_deu.forEach(garantiaDeu => {
          if(res.data==1){
            this.deudor_budqueda = garantiaDeu.idDeudor;
          }else{
            this.deudor_budqueda = garantiaDeu.idDeudor+","+this.deudor_budqueda;
          }
          
        });
      }
    );

    
    
    this.modDetallesDeudorActions.emit({action:"modal", params:['open']});
  }
  exportDataDeudor() {
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
  onUsuarios(acreedor: Acreedor, i: number) {
    console.log("onUsuarios");
    this.identificador_Musuario = String(acreedor.idPersona);
    //this.nit_Musuario = String(acreedor.rfc);
    this.nit_Musuario = acreedor.rfc !== null && acreedor.rfc !== undefined ? String(acreedor.rfc) : '';   
    //this.nomrec_Musuario = String(acreedor.nombrePersona);
    this.nomrec_Musuario = acreedor.nombrePersona !== null && acreedor.nombrePersona !== undefined ? String(acreedor.nombrePersona) : '';   

    
    this.filtrog = new Garantias;
    this.filtrog.idAcreedor = String(acreedor.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        console.log("this.usuario_busqueda: "+ this.usuario_busqueda);
        const ultimoCaracter = this.usuario_busqueda.slice(-1);
        if(ultimoCaracter==","){this.usuario_busqueda = this.usuario_busqueda.slice(0, -1);}    
        this.filtrou.idUsuario_b = String(this.usuario_busqueda);
        this.usuariosService.usuarios = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.usuariosService.fetchData_b(null, 1, this.pageSize_usr, this.filtrou).subscribe(
          res => {
            
            this.usuarios = res.data;      
            this.totala_modUsuarios = String(res.total);
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
          if(res.data==1){
            this.usuario_busqueda = garantiaUser.idUsuario;
          }else{
            this.usuario_busqueda = garantiaUser.idUsuario+","+this.usuario_busqueda;
          }
          
        });
      }
    );
    
    this.modDetallesUsuActions.emit({action:"modal", params:['open']});
  }
  closeModalUsuarios() {
    this.modDetallesUsuActions.emit({action:"modal",params:['close']});    
    
    
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

  onPageChangeG(page2: number) {            
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
        this.totala_modGarantia = String(res.total);
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
  onPageChangeDeu(pageac: number) {    
    console.log("onAcreedores");

    //this.filtrog = new Garantias;
    //this.filtrog.idDeudor = String(deudor.idPersona);

    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        console.log("this.acreedor_budqueda: "+ this.deudor_budqueda);
        
        const ultimoCaracter = this.deudor_budqueda.slice(-1);
        if(ultimoCaracter==","){this.deudor_budqueda = this.deudor_budqueda.slice(0, -1);}
        
        this.filtroa.idAcreedor_b = String(this.deudor_budqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.acreedorService.fetchData_b(null, pageac, this.pageSize_deu, this.filtroa).subscribe(
          res => {
            
            this.deudores_b = res.data;      
            this.total_deu = res.total;
            this.totala_modDeudores=String(res.total);
            this.currentPage_deu = pageac;
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
        this.deudor_budqueda = '';
        this.garantias_deu = res.data;
        this.garantias_deu.forEach(garantiaAcr => {
          if(res.data==1){
            this.deudor_budqueda = garantiaAcr.idAcreedor;
          }else{
            this.deudor_budqueda = garantiaAcr.idAcreedor+","+this.deudor_budqueda;
          }
          
        });
      }
    );

        
  }
  onPageChangeUsr(pageusr: number) 
  {
    console.log("onUsuarios");
    
    /*this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);*/
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        console.log("this.usuario_busqueda: "+ this.usuario_busqueda);   
        const ultimoCaracter = this.usuario_busqueda.slice(-1);
        if(ultimoCaracter==","){this.usuario_busqueda = this.usuario_busqueda.slice(0, -1);}         
        this.filtrou.idUsuario_b = String(this.usuario_busqueda);
        this.usuariosService.usuarios = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.usuariosService.fetchData_b(null, pageusr, this.pageSize_usr, this.filtrou).subscribe(
          res => {
            
            this.usuarios = res.data;      
            this.total_usr = res.total;
            this.totala_modUsuarios = String(res.total);
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
          if(res.data==1){
            this.usuario_busqueda = garantiaUser.idUsuario;
          }else{
            this.usuario_busqueda = garantiaUser.idUsuario+","+this.usuario_busqueda;
          }
          
        });
      }
    );

  }
}
