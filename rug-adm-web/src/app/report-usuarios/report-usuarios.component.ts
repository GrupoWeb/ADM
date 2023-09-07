import { Component, OnInit, ElementRef, ViewChild, EventEmitter ,Input,ChangeDetectorRef} from '@angular/core';
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
import { ExternalUser } from '../shared/external-user.model';
import { Deposit } from '../shared/deposit.model';
import { DepositsService } from '../shared/deposits.service';
import { BancoPipe } from '../shared/banco.filter';
import { TipoPagoPipe } from '../shared/tipo-pago.filter';
import { EstadoPipe } from '../shared/estado.filter';
import { UsadaPipe } from '../shared/usada.filter';
import { Transaction } from '../shared/transaction.model';
import { GuaranteesService } from '../shared/guarantees.service';
import { TransactionPreview } from '../shared/transaction-preview.model';
@Component({
  selector: 'app-report-usuarios',
  templateUrl: './report-usuarios.component.html',
  styleUrls: ['./report-usuarios.component.css']
})
export class ReportUsuariosComponent implements OnInit {
  public loading = false;
  index =1;
  operacionSeleccionado: string;
  deudores: Deudor[];
  deudores_b: Deudor[];
  garantias: Garantias[];
  garantias_select: Garantias[];
  garantias_acre: Garantias[];
  garantias_usu: Garantias[];
  garantias_deu: Garantias[];

  partsHttpSubscripcion: Subscription;
  
  deposits: Deposit[];
  depositsa: Deposit[];
  depositsr: Deposit[];
  depositos: Deposit[];
  depositsp: Deposit[];
  
  deposits_b: Deposit[];

  acreedores_b: Acreedor[];
  @ViewChild('filDeudorInput') filDeudorInput: ElementRef;
  @ViewChild('filAcreedorInput') filAcreedorInput: ElementRef;
  @ViewChild('select_y') select_y: ElementRef;
  

  usuarios: Usuario[];
  exportDeudor: Deudor[];
  filAcreedores: any[] = [];
  filDeudores: any[] = [];
  deudores_filtro: any[] = [];
  acreedores_filtro: any[] = [];
  exportGarantia: Garantias[];
  exportAcreedor: Acreedor[];
  exportUsuario: Usuario[];
  exportHistorial: Deposit[];
  exportHistorial_a: Deposit[];
  exportHistorial_r: Deposit[];
  exportHistorial_p: Deposit[];
  exportGuarantees: Transaction[];
  guarantees: Transaction[];

  acreedor_budqueda:String;
  usuario_busqueda:String;
  deudor_busqueda:String;
  total: number;
  total2: number;
  total_acr: number;
  total_usr : number;
  total_deu : number
  totalH: number;
  totalHA: number;
  totalHR: number;
  totalHP: number;
  totalOpe: number;
  
  
  
  currentPage: number = 1;
  currentPage2: number = 1;
  currentPage_acr: number = 1;
  currentPage_usr: number = 1;
  currentPage_deu: number = 1;
  currentPageG: number = 1;
  currentPageH: number = 1;
  currentPageHA: number = 1;
  currentPageHR: number = 1;
  currentPageHP: number = 1;
  currentPageOpe: number = 1;
  
  
  pageSize: number = 10;
  pageSize2: number = 10;
  pageSize_acr: number = 10;
  pageSize_deu: number = 10;
  pageSize_usr: number = 10;
  pageSizeH: number = 10;
  pageSizeHA: number = 10;
  pageSizeHR: number = 10;
  pageSizeHP: number = 10;
  pageSizeOpe: number = 10;

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
  filtrot: Transaction;
  localSubscription: Subscription;
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modDetallesActions = new EventEmitter<string|MaterializeAction>();
  modGarantiasActions = new EventEmitter<string|MaterializeAction>();
  modDetallesGActions = new EventEmitter<string|MaterializeAction>();
  modDetallesAcreActions = new EventEmitter<string|MaterializeAction>();
  modDetallesUsuActions = new EventEmitter<string|MaterializeAction>();
  modDetallesDeudorActions = new EventEmitter<string|MaterializeAction>();
  modHistorialPagoActions = new EventEmitter<string|MaterializeAction>();
  modHistorialPagoAcActions = new EventEmitter<string|MaterializeAction>();
  modHistorialPagoRActions = new EventEmitter<string|MaterializeAction>();
  modHistorialPagoPActions = new EventEmitter<string|MaterializeAction>();
  modOperacionesActions = new EventEmitter<string|MaterializeAction>();
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalTransaction: TransactionPreview;

  modalDeposit: Deudor;
  modalIndex: number;
  fechaInicioH: string;
  fechaFinH: string;
  @ViewChild('f') rejectForm: NgForm;
  identi_garantia = '';
  textob_acreedor = '';
  textob_deudor = '';
  nit_garantia = '';
  nomrec_garantia = '';
  deudor_budqueda:String;
  identificador_Macreedor = '';
  identificador_Mdeudores = '';
  nit_gMacreedor = '';
  nomrec_Macreedor = '';
  
  identificador_Musuario = '';
  nit_Musuario = '';
  nomrec_Musuario = '';

  nume_garantia = '';
  textob='';
  m_identificacion='';
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
  options: NgxDateRangePickerOptions;
  @Input() dateRange: NgxDateRangePickerComponent;
  
  rangoFechas: string | any;
  rangoFechasHistorial: string | any;
  rangoFechasHistorial_a: string | any;
  rangoFechasHistorial_r: string | any;
  rangoFechasHistorial_p: string | any;
  rangoFechasOp: string | any;
  manualDate: string;

  nomusuario_historial: string;
  identi_historial:string;
  monto_historial : string;
  monto_historial_a : string;
  monto_historial_r : string;
  monto_historial_p : string;
  nomusuario_historial_a: string;
  nomusuario_historial_r: string;
  nomusuario_historial_p: string;

  filEstado: string;
  filtro_d: Deposit;
  filtro_da: Deposit;
  filtro_dr: Deposit;
  filtro_dp: Deposit;

  totalu_usuarios = '';
  totalu_modGarantia='';
  totalu_modAcreedores = '';
  totalu_modDeudores = '';
  totalu_modHistorialPago = '';
  totalu_modHistorialPagoA = '';
  totalu_modHistorialPagoR = '';
  totalu_modHistorialPagoP = '';

  fechaInicio: string;
  fechaFin: string;

  constructor(private deudorService: DeudoresService,
    private loadingService: LoadingService,
    private excelService: ExcelService,
    private garantiaService: GarantiasService,
    private acreedorService: AcreedoresService,
    private usuariosService: UsuariosService,
    private cdr: ChangeDetectorRef,
    private depositsService: DepositsService,
    private bancoPipe: BancoPipe,
    private tipoPagoPipe: TipoPagoPipe,
    private usadaPipe: UsadaPipe,
    private guaranteesService: GuaranteesService,
    
    ) { }

  ngOnInit() {
    this.acreedor_budqueda="";
    const today = moment().toDate();
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.rangoFechasHistorial = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.rangoFechasHistorial_a = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    
    this.rangoFechasHistorial_r = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    
    this.rangoFechasHistorial_p = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.rangoFechasOp = {
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
    //this.fechaInicio = moment().format('YYYY-MM-') + '01';
    //this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.filtro = new Deudor;
    this.filtrog = new Garantias;
    this.filtroa = new Acreedor;
    this.filtrou = new Usuario;
    this.usuariosService.usuarios = [];
    

    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usuariosService.fetchData(null, this.currentPage, this.pageSize, this.filtrou).subscribe(
      res => {
        this.usuarios = res.data;
        
        this.total = res.total;
        this.totalu_usuarios = String(res.total);
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
  /* Funcion para refrescar los datos de la tabla inicial */
  refreshData() {
    this.filtrou.textobusqueda=this.textob;
    
    this.usuariosService.usuarios = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usuariosService.fetchData(null, 1, this.pageSize, this.filtrou).subscribe(
      res => {
        this.usuarios = res.data;
        this.total = res.total;
        this.totalu_usuarios = String(res.total);
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
/* Funciones para controlar el cambio de paginas en las tablas*/
  onPageChange(page: number) {    
    this.usuariosService.usuarios = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usuariosService.fetchData(null, page, this.pageSize, this.filtrou).subscribe(
      res => {
        this.usuarios = res.data;
        this.total = res.total;
        this.totalu_usuarios = String(res.total);
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
        this.totalu_modGarantia = String(res.total);
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
    //console.log("onAcreedores");
    this.modalIndex = 1;
    //this.filtrog = new Garantias;
    //this.filtrog.idDeudor = String(deudor.idPersona);

    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
  
    this.modalIndex = 1;
    this.filtrog = new Garantias;
    //this.filtrog.idUsuario = String(usuario.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.modalIndex = 1;
    this.filtroa.idAcreedor_b = String(this.identificador_Macreedor);
    this.acreedorService.acreedores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData_b(null, pageac, this.pageSize_acr, this.filtroa).subscribe(
      res => {
        
        this.acreedores_b = res.data;      
        this.totalu_modAcreedores = String(res.total);
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
  /*  this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        
        this.modalIndex = 1;
        this.filtroa.idAcreedor_b = String(this.acreedor_budqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.acreedorService.fetchData_b(null, pageac, this.pageSize_acr, this.filtroa).subscribe(
          res => {
            
            this.acreedores_b = res.data;      
            this.total_acr = res.total;
            this.totalu_modAcreedores = String(res.total);
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
          if(res.data==1){
            this.acreedor_budqueda = garantiaAcr.idAcreedor;
          }else{
            this.acreedor_budqueda = garantiaAcr.idAcreedor+","+this.acreedor_budqueda;
          }
          
        });
      }
    );
*/
        
  }
  onPageChangeUsr(pageusr: number) 
  {
    //console.log("onUsuarios");
    this.modalIndex = 1;
    /*this.filtrog = new Garantias;
    this.filtrog.idDeudor = String(deudor.idPersona);*/
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        
        this.modalIndex = 1;
        this.filtrou.idUsuario_b = String(this.usuario_busqueda);
        this.usuariosService.usuarios = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.usuariosService.fetchData_b(null, pageusr, this.pageSize_usr, this.filtrou).subscribe(
          res => {
            
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
          if(res.data==1){
            this.usuario_busqueda = garantiaUser.idUsuario;
          }else{
            this.usuario_busqueda = garantiaUser.idUsuario+","+this.usuario_busqueda;
          }
          
        });
      }
    );

  }
  onPageChangeDeu(pageac: number) {    
    //console.log("onDeudores");

    //this.filtrog = new Garantias;
    //this.filtrog.idDeudor = String(deudor.idPersona);

    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);

    this.modalIndex = 1;
    this.filtrog = new Garantias;
    //this.filtrog.idUsuario = String(usuario.idPersona);
    this.filtro.idDeudor_b = String(this.identificador_Mdeudores);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData_b(null, pageac, this.pageSize_deu, this.filtro).subscribe(
      res => {
        
        this.deudores_b = res.data;      
        this.total_deu = res.total;
        this.totalu_modDeudores = String(res.total);
        this.currentPage_deu = pageac;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
   /* this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        
        
        this.filtro.idDeudor_b = String(this.deudor_budqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.deudorService.fetchData_b(null, pageac, this.pageSize_deu, this.filtro).subscribe(
          res => {
            
            this.deudores_b = res.data;      
            this.total_deu = res.total;
            this.totalu_modDeudores = String(res.total);
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
*/
        
  }
  onPageChangeH(pageh: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, pageh, this.pageSizeH, this.filtro_d).subscribe(
      res => {
        this.deposits = res.data;
        this.totalH = res.total;
        this.totalu_modHistorialPago = String(res.total);
        this.currentPageH = pageh;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChangeHA(pageha: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, pageha, this.pageSizeHA, this.filtro_da).subscribe(
      res => {
        this.depositsa = res.data;
        this.totalHA = res.total;
        this.totalu_modHistorialPagoA = String(res.total);
        this.currentPageHA= pageha;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChangeHR(pagehr: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, pagehr, this.pageSizeHR, this.filtro_dr).subscribe(
      res => {
        this.depositsr = res.data;
        this.totalHR = res.total;
        this.totalu_modHistorialPagoR = String(res.total);
        this.currentPageHR= pagehr;
        this.depositsService.addDeposits(this.depositsr);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChangeHP(pagehp: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, pagehp, this.pageSizeHP, this.filtro_dp).subscribe(
      res => {
        this.depositsp = res.data;
        this.totalHP = res.total;
        this.totalu_modHistorialPagoP = String(res.total);
        this.currentPageHP= pagehp;
        this.depositsService.addDeposits(this.depositsp);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  onPageChangeOpe(page: number) {
    this.guarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.fechaInicio = '2008-01-01';
    this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.httpSubscription = this.guaranteesService.fetchData(page, this.pageSizeOpe, this.filtrot, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.guarantees = res.data;
        this.totalOpe = res.total;
        this.currentPageOpe = page;
      },
      err => {
        console.error(err);
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  /* Terminan funciones para controlar el cambio de paginas en las tablas*/

/* Funciones para exportar a excel la información que se visualiza en las tablas toma encuenta los filtros*/
  exportData() {
    this.exportDeudor = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usuariosService.fetchData(null, null, null, this.filtrou).subscribe(
      
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
        this.excelService.export(temp, 'usuarios');
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
  exportDataH(){
    this.exportHistorial=[];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, null, null, this.filtro_d).subscribe(
      res => {
        this.exportHistorial = res.data;
        const temp = this.exportHistorial.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Banco": this.bancoPipe.transform(el.agencia),
          "Boleta": el.numero,
          "Nombre": el.externalUser.name ,
          "Identificacion": el.externalUser.docId,
          "Monto": "Q " +el.monto,          
          "Tipo de pago": this.tipoPagoPipe.transform(el.tipoPago) ,
          "Estado": this.usadaPipe.transform(el.usada),
        }));
        this.excelService.export(temp, 'Historial');       
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportData_a(){
    this.exportHistorial_a=[];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, null, null, this.filtro_da).subscribe(
      res => {
        this.exportHistorial_a = res.data;
        const temp = this.exportHistorial_a.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Banco": this.bancoPipe.transform(el.agencia),
          "Boleta": el.numero,
          "Nombre": el.externalUser.name ,
          "Identificacion": el.externalUser.docId,
          "Monto": "Q " +el.monto,          
          "Tipo de pago": this.tipoPagoPipe.transform(el.tipoPago) ,
          "Estado": this.usadaPipe.transform(el.usada),
        }));
        this.excelService.export(temp, 'Historial_Aprobado');       
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportData_r(){
    this.exportHistorial_r=[]; 
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, null, null, this.filtro_dr).subscribe(
      res => {
        this.exportHistorial_r = res.data;
        const temp = this.exportHistorial_r.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Banco": this.bancoPipe.transform(el.agencia),
          "Boleta": el.numero,
          "Nombre": el.externalUser.name ,
          "Identificacion": el.externalUser.docId,
          "Monto": "Q " +el.monto,          
          "Tipo de pago": this.tipoPagoPipe.transform(el.tipoPago) ,
          "Estado": this.usadaPipe.transform(el.usada),
        }));
        this.excelService.export(temp, 'Historial_Rechazado');       
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportData_p(){
    this.exportHistorial_p=[]; 
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, null, null, this.filtro_dp).subscribe(
      res => {
        this.exportHistorial_p = res.data;
        const temp = this.exportHistorial_p.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Banco": this.bancoPipe.transform(el.agencia),
          "Boleta": el.numero,
          "Nombre": el.externalUser.name ,
          "Identificacion": el.externalUser.docId,
          "Monto": "Q " +el.monto,          
          "Tipo de pago": this.tipoPagoPipe.transform(el.tipoPago) ,
          "Estado": this.usadaPipe.transform(el.usada),
        }));
        this.excelService.export(temp, 'Historial_Pendientes');       
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  exportDataOp(){
    this.exportGuarantees = [];
    this.loading = true;   
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(null, null, this.filtrot, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.exportGuarantees = res.data;
        //console.log(res.data);
        const temp = this.exportGuarantees.map(el => ({
          "Número garantía": el.guarantee ? el.guarantee.idGarantia : '',
          "Trámite": el.descripcion,
          "Fecha": moment(el.fechaCreacion, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Solicitante": el.solicitante ? el.solicitante.name : '',
          "Solicitante Original": el.guarantee ? el.guarantee.original : '',
          "Acreedor": el.acreedores ? el.acreedores.map(e => e.name).join(";") : '',
          "Deudor": el.deudores ? el.deudores.map(e => e.name).join(";") : '',
          "Descripción": el.guarantee ? el.guarantee.descGarantia : '',
          "Identificador": el.bienLista ? el.bienLista.identificador : '',
          "Bien": el.bienLista ? el.bienLista.descripcion : '',
        }));
        this.excelService.export(temp, 'operaciones');
      },
      err => {
        console.error(err);
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  /* Terminan funciones para exportar a excel*/
  /* Funcion 1que determina la accion de cada operacion seleccionada en el select*/
  Operaciones(usuario: Usuario, i: number) {
    //this.select_y.nativeElement.selectedIndex = 0;
//    this.select_y.nativeElement.disabled = true;
    //console.log("ENTRA OPERACIONES");
    if(this.operacionSeleccionado=="1"){
      this.onDetalles(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="2"){
      this.onGarantias(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="3"){
      this.onDeudores(usuario,Number(this.operacionSeleccionado));
    }    
    else if(this.operacionSeleccionado=="4"){
      this.onAcreedores_b(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="5"){
      this.onHistorialPago(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="6"){
      this.onHistorialPagoA(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="7"){
      this.onHistorialPagoR(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="8"){
      this.onHistorialPagoP(usuario,Number(this.operacionSeleccionado));
    }
    else if(this.operacionSeleccionado=="9"){
      this.onOperaciones(usuario,Number(this.operacionSeleccionado));
    }
  }
  /* Terminan funcion Operaciones*/
  /* Funciones que realizan la logica y levantan los modal de cada una de las operaciones*/
  onDetalles(usuario: Usuario, i: number) {
    //this.modalDeposit = usuario;
    this.modalIndex = 1;
    this.m_identificacion = String(usuario.idPersona);
    //this.m_nit = String(usuario.rfc);
    this.m_nit = usuario.rfc !== null && usuario.rfc !== undefined ? String(usuario.rfc) : '';   
    //this.m_nombre = String(usuario.nombrePersona);
    this.m_nombre = usuario.nombrePersona !== null && usuario.nombrePersona !== undefined ? String(usuario.nombrePersona) : '';   
    this.m_tipo = String(usuario.perJuridica);
    //this.m_nacionalidad = String(usuario.nomNacionalidad);
    this.m_nacionalidad = usuario.nomNacionalidad !== null && usuario.nomNacionalidad !== undefined ? String(usuario.nomNacionalidad) : '';   
    //this.m_correo = String(usuario.email);
    this.m_correo = usuario.email !== null && usuario.email !== undefined ? String(usuario.email) : '';   
//    this.m_domicilio = String(usuario.direccion);
    this.m_domicilio = usuario.direccion !== null && usuario.direccion !== undefined ? String(usuario.direccion) : '';   

    this.modDetallesActions.emit({action:"modal", params:['open']});
  }
  onGarantias(usuario: Usuario, i: number) {
    //console.log("ENTRA A ONGARANTIAS");
    this.filtrog = new Garantias;
    this.filtrog.idUsuario = String(usuario.idPersona);      
    this.modalIndex = 1;    
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.llenar_select_deu();
    //this.llenar_select_acre();
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
    .pipe(
      finalize(() => {
        
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
        this.totalu_modGarantia = String(res.total);
        this.currentPage2 = 1;
        this.identi_garantia=String(usuario.idPersona);
        //this.nit_garantia=usuario.rfc;
        this.nit_garantia = usuario.rfc !== null && usuario.rfc !== undefined ? String(usuario.rfc) : '';   
        this.nomrec_garantia=usuario.nombrePersona;
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
  onDeudores(usuario: Usuario, i: number) {
    //console.log("onDeudores");
    this.identificador_Mdeudores = String(usuario.idPersona);
    //this.nit_gMacreedor = String(usuario.rfc);
    this.nit_gMacreedor = usuario.rfc !== null && usuario.rfc !== undefined ? String(usuario.rfc) : '';   
    this.nomrec_Macreedor = String(usuario.nombrePersona);
    this.modalIndex = 1;
    this.filtrog = new Garantias;
    this.filtrog.idUsuario = String(usuario.idPersona);
    this.filtro.dusuarios = String(usuario.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.deudorService.fetchData_b(null, 1, this.pageSize_deu, this.filtro).subscribe(
      res => {
        
        this.deudores_b = res.data;      
        this.total_deu = res.total;
        this.totalu_modDeudores = String(res.total);
        this.currentPage_deu = 1;
        //this.deudorService.addDeposits(this.deudores);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    /*this.httpSubscription = this.garantiaService.fetchData(null, 1, this.pageSize_deu, this.filtrog)
    .pipe(
      finalize(() => {
        //console.log(String(this.deudor_busqueda));
        this.modalIndex = 1;
        
        const ultimoCaracter = this.deudor_busqueda.slice(-1);
          if(ultimoCaracter==","){this.deudor_busqueda = this.deudor_busqueda.slice(0, -1);}
          this.filtro.idDeudor_b = String(this.deudor_busqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.deudorService.fetchData_b(null, 1, this.pageSize_deu, this.filtro).subscribe(
          res => {
            
            this.deudores_b = res.data;      
            this.total_deu = res.total;
            this.totalu_modDeudores = String(res.total);
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
        this.deudor_busqueda = '';
        this.garantias_deu = res.data;
        this.garantias_deu.forEach(garantiaDeu => {
          if(res.data==1){
            this.deudor_busqueda = garantiaDeu.idDeudor;
          }else{
            this.deudor_busqueda = garantiaDeu.idDeudor+","+this.deudor_busqueda;
          }
          
        });
      }
    );*/

    
    
    this.modDetallesDeudorActions.emit({action:"modal", params:['open']});
  }
  onAcreedores_b(usuario: Usuario, i: number) {
    //console.log("onAcreedores :"+String(usuario.idPersona));
    this.identificador_Macreedor = String(usuario.idPersona);
    //this.nit_gMacreedor = String(usuario.rfc);
    this.nit_gMacreedor = usuario.rfc !== null && usuario.rfc !== undefined ? String(usuario.rfc) : '';   
    this.nomrec_Macreedor = String(usuario.nombrePersona);
    this.modalIndex = 1;
    this.filtrog = new Garantias;
    this.filtrog.idUsuario = String(usuario.idPersona);
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.modalIndex = 1;
    
    this.filtroa.ausuarios = String(usuario.idPersona);
    this.acreedorService.acreedores = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.acreedorService.fetchData_b(null, 1, this.pageSize_acr, this.filtroa).subscribe(
      res => {
        
        this.acreedores_b = res.data;      
        this.totalu_modAcreedores = String(res.total);
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
   /* this.httpSubscription = this.garantiaService.fetchData(null, null, null, this.filtrog)
    .pipe(
      finalize(() => {
        
        this.modalIndex = 1;
        this.filtroa.idAcreedor_b = String(this.acreedor_budqueda);
        this.acreedorService.acreedores = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.acreedorService.fetchData_b(null, 1, this.pageSize_acr, this.filtroa).subscribe(
          res => {
            
            this.acreedores_b = res.data;      
            this.totalu_modAcreedores = String(res.total);
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
          if(res.data==1){
            this.acreedor_budqueda = garantiaAcr.idAcreedor;
          }else{
            this.acreedor_budqueda = garantiaAcr.idAcreedor+","+this.acreedor_budqueda;
          }
          
        });
      }
    );
*/
    
    
    this.modDetallesAcreActions.emit({action:"modal", params:['open']});
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
    this.mg_prorr = String("");
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
    this.mg_soriginal = String(garantia.sOriginal);
    //this.mg_detalle = String(garantia.dGarantia);
    this.mg_detalle = garantia.dGarantia !== null && garantia.dGarantia !== undefined ? String(garantia.dGarantia) : '';   
    

    this.modDetallesGActions.emit({action:"modal", params:['open']});
  }
  onHistorialPago(usuario: Usuario, i: number){
    //console.log(usuario);    
    this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = usuario.idPersona;    
    
    this.filtro_d.externalUser=persona;
    
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageH, this.pageSizeH, this.filtro_d).subscribe(
      res => {
        //console.log(res);
        this.deposits = res.data;
        this.totalH = res.total;
        this.totalu_modHistorialPago = String(res.total);
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.nomusuario_historial = usuario.nombrePersona;
    this.identi_historial = String(usuario.idPersona);

    this.modHistorialPagoActions.emit({action:"modal", params:['open']});
  }
  onHistorialPagoA(usuario: Usuario, i: number){
    //console.log("onHistorialPagoA");
    //console.log(usuario.idPersona);
    
    this.filtro_da = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = usuario.idPersona;    
    
    this.filtro_da.externalUser=persona;
    this.filtro_da.usada = 1;
    
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHA, this.pageSizeHA, this.filtro_da).subscribe(
      res => {
        this.depositsa = res.data;
        this.totalHA = res.total;
        this.totalu_modHistorialPagoA = String(res.total);
        this.depositsService.addDeposits(this.depositsa);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.nomusuario_historial_a = "Usuario: "+usuario.nombrePersona;

    this.modHistorialPagoAcActions.emit({action:"modal", params:['open']});
  }
  onHistorialPagoR(usuario: Usuario, i: number){
    //console.log("onHistorialPagoR");
    //console.log(usuario.idPersona);
    
    this.filtro_dr = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = usuario.idPersona;    
    
    this.filtro_dr.externalUser=persona;
    this.filtro_dr.usada = -1;
    
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHR, this.pageSizeHR, this.filtro_dr).subscribe(
      res => {
        
        this.depositsr = res.data;
        this.totalHR = res.total;
        this.totalu_modHistorialPagoR = String(res.total);
        this.depositsService.addDeposits(this.depositsr);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.nomusuario_historial_r = "Usuario: "+usuario.nombrePersona;

    this.modHistorialPagoRActions.emit({action:"modal", params:['open']});
  }
  onHistorialPagoP(usuario: Usuario, i: number){
    //console.log("onHistorialPagoP");
    //console.log(usuario.idPersona);
    
    this.filtro_dp = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = usuario.idPersona;    
    
    this.filtro_dp.externalUser=persona;
    this.filtro_dp.usada = 0;
    
    
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHP, this.pageSizeHP, this.filtro_dp).subscribe(
      res => {
        this.depositsp = res.data;
        this.totalHP = res.total;
        this.totalu_modHistorialPagoP = String(res.total);
        this.depositsService.addDeposits(this.depositsp);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.nomusuario_historial_p = "Usuario: "+usuario.nombrePersona;

    this.modHistorialPagoPActions.emit({action:"modal", params:['open']});
  }
  onOperaciones(usuario: Usuario, i: number){
    this.filtrot= new Transaction;
    let solicitante = new ExternalUser;
    //console.log("OnOperaciones: "+usuario.idPersona);
    solicitante.personaId = usuario.idPersona;
    this.filtrot.solicitante = solicitante;
    //console.log("filtrot: "+this.filtrot);
    //this.refreshData();fgfhjkhg
    this.fechaInicio = '2008-01-01';
    this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.guarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(1, this.pageSizeOpe, this.filtrot, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        //console.log(res.data);
        this.guarantees = res.data;
        
        this.totalOpe = res.total;
        this.currentPageOpe = 1;
      },
      err => {
        console.error(err);
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.modOperacionesActions.emit({action:"modal", params:['open']});
    
  }
  /* Termina funciones que realizan la logica y levantan los modal de cada una de las operaciones*/
  /* Funciones que cierran los modal de cada operacion*/
  closeModalReject() {
    this.modRejectActions.emit({action:"modal",params:['close']});
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalDetalles() {
    this.modDetallesActions.emit({action:"modal",params:['close']});
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalAcreedores() {
    this.modDetallesAcreActions.emit({action:"modal",params:['close']});    
    this.modalIndex = -1;
    
  }
  closeModalDetallesG() {
    this.modDetallesGActions.emit({action:"modal",params:['close']});
    this.modalDeposit = new Deudor;
    this.modalIndex = -1;
  }
  closeModalGarantia() {
    this.modGarantiasActions.emit({action:"modal",params:['close']});    
    this.modalIndex = -1;
    
  }
  closeModalUsuarios() {
    this.modDetallesUsuActions.emit({action:"modal",params:['close']});    
    this.modalIndex = -1;
    
  }
  closeModalDeudores() {
    this.modDetallesDeudorActions.emit({action:"modal",params:['close']});    
    this.modalIndex = -1;
    
  }
  closeModalHistorialPago() {
    this.modHistorialPagoActions.emit({action:"modal",params:['close']});        
    
  }
  closeModalHistorialPagoA() {
    this.modHistorialPagoAcActions.emit({action:"modal",params:['close']});        
    
  }
  closeModalHistorialPagoR() {
    this.modHistorialPagoRActions.emit({action:"modal",params:['close']});        
    
  }
  closeModalHistorialPagoP() {
    this.modHistorialPagoPActions.emit({action:"modal",params:['close']});        
    
  }
  closeModalonOperaciones() {
    this.modOperacionesActions.emit({action:"modal",params:['close']});        
    
  }
  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
    
  }
   /* Terminan funciones que cierran los modal de cada operacion*/
   /*Funcion que campura el cambio en el filtro de rango de fecha*/
   onRangeChanged(event) {
    
    if (this.fechaInicioH !== event.from || this.fechaFinH !== event.to) {
      this.filtrog.fechaInicio = event.from
      this.filtrog.fechaFin = event.to
      this.fechaInicioH = event.from;
      this.fechaFinH = event.to;
      //this.refreshData();
    }
  }
   onRangeChangedHistorial(event) {
    
    if (this.fechaInicioH !== event.from || this.fechaFinH !== event.to) {
      this.filtro_d.fechaInicio = event.from
      this.filtro_d.fechaFin = event.to
      this.fechaInicioH = event.from;
      this.fechaFinH = event.to;
      //this.refreshData();
    }
  }
  onRangeChangedHistorial_a(event) {
    
    if (this.fechaInicioH !== event.from || this.fechaFinH !== event.to) {
      this.filtro_da.fechaInicio = event.from
      this.filtro_da.fechaFin = event.to
      this.fechaInicioH = event.from;
      this.fechaFinH = event.to;
      //this.refreshData();
    }
  }
  onRangeChangedHistorial_r(event) {
    
    if (this.fechaInicioH !== event.from || this.fechaFinH !== event.to) {
      this.filtro_dr.fechaInicio = event.from
      this.filtro_dr.fechaFin = event.to
      this.fechaInicioH = event.from;
      this.fechaFinH = event.to;
      //this.refreshData();
    }
  }
  onRangeChangedHistorial_p(event) {
    
    if (this.fechaInicioH !== event.from || this.fechaFinH !== event.to) {
      this.filtro_dp.fechaInicio = event.from
      this.filtro_dp.fechaFin = event.to
      this.fechaInicioH = event.from;
      this.fechaFinH = event.to;
      //this.refreshData();
    }
  }
  onRangeChangedOp(event) {
    if (this.fechaInicio !== event.from || this.fechaFin !== event.to) {
      this.fechaInicio = event.from;
      this.fechaFin = event.to;
      this.guarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(1, this.pageSize, this.filtrot, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        //console.log(res.data);
        this.guarantees = res.data;
        
        this.total = res.total;
        this.currentPage = 1;
      },
      err => {
        console.error(err);
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
      //this.refreshData();
    }
  }
  
  /*Termina funcion que campura el cambio en el filtro de rango de fecha*/
  /*Funcion que busca los datos de acuerdo a los filtros ingresados en la tabla de Garantias*/
  BuscarData() {

    //this.filtrog = new Garantias;
    this.filtrog.idGarantia = parseInt(this.nume_garantia);
    this.filtrog.idUsuario = this.identi_garantia;
    this.filtrog.fechaInicio = this.fechaInicioH;
    this.filtrog.fechaFin = this.fechaFinH;   
    this.filtrog.nDeudor = this.textob_deudor;
    this.filtrog.nAcreedor = this.textob_acreedor;
       
    this.modalIndex = 1;    
    this.garantiaService.garantias = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    /*this.llenar_select_deu();
    this.llenar_select_acre();*/
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
    .pipe(
      finalize(() => {/*
        
      this.httpSubscription = this.garantiaService.fetchData_all_acr(null, null, null, this.filtrog).subscribe(
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
            this.acreedores_filtro =  res.data.map((item: any) => ({
              value: item.idAcreedor,
              label: item.nAcreedor
            }));        
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
        this.currentPage2 = 1;
    
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
  }
  BuscarDataH(){
    //console.log("BUSCAR Historico completo");     
    //this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.identi_historial);    
    
    this.filtro_d.externalUser=persona;
    this.filtro_d.monto = Number(this.monto_historial);
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageH, this.pageSizeH, this.filtro_d).subscribe(
      res => {
        //console.log(res.data); 
        this.deposits = res.data;
        this.totalH = res.total;
        this.currentPageH = 1;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }  
  BuscarData_a(){
    //console.log("BUSCAR Historico aprobada");     
    //this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.identi_historial);    
    
    this.filtro_da.externalUser=persona;
    this.filtro_da.monto = Number(this.monto_historial_a);
    this.filtro_da.usada = 1;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHA, this.pageSizeHA, this.filtro_da).subscribe(
      res => {
        this.depositsa = res.data;
        this.totalHA = res.total;
        this.depositsService.addDeposits(this.depositsa);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  BuscarData_r(){
    //console.log("BUSCAR Historico rechazado");     
    //this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.identi_historial);    
    

    this.filtro_dr.externalUser=persona;
    this.filtro_dr.monto = Number(this.monto_historial_r);
    this.filtro_dr.usada = -1;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHR, this.pageSizeHR, this.filtro_dr).subscribe(
      res => {
        this.depositsr = res.data;
        this.totalHR = res.total;
        this.depositsService.addDeposits(this.depositsa);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }  
  BuscarData_p(){
    //console.log("BUSCAR Historico pendientes");     
    //this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.identi_historial);    
    

    this.filtro_dp.externalUser=persona;
    this.filtro_dp.monto = Number(this.monto_historial_p);
    this.filtro_dp.usada = -0;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHP, this.pageSizeHP, this.filtro_dp).subscribe(
      res => {
        this.depositsp = res.data;
        this.totalHP = res.total;
        this.depositsService.addDeposits(this.depositsp);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  /*Termina funcion que busca los datos de acuerdo a los filtros ingresados en la tabla de Garantias*/
  /*Funcion que limpia los filtros seleccionados y muestra todos los datos en la tabla de Garantias*/
  LimpiarData(){
    this.nume_garantia='';
    this.fechaInicioH = '';
    this.fechaFinH = '';    
    const today = moment().toDate();
    //this.filDeudorInput.nativeElement.selectedIndex = 0;
    //this.filAcreedorInput.nativeElement.selectedIndex = 0;
    this.filtrog = new Garantias; 
    //console.log(this.identi_garantia);
    this.filtrog.idUsuario=this.identi_garantia;
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
    this.httpSubscription2 = this.garantiaService.fetchData(null, 1, this.pageSize2, this.filtrog)
    .pipe(
      finalize(() => {
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
  LimpiarDataH(){
    this.monto_historial='';
    this.fechaInicioH = '';
    this.fechaFinH = '';
   
    const today = moment().toDate();
    
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
     
    this.filtro_d = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    //persona.personaId = usuario.idPersona;    
    persona.personaId = Number(this.monto_historial);
    this.filtro_d.externalUser=persona;
    
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageH, this.pageSizeH, this.filtro_d).subscribe(
      res => {
        //console.log(res);
        this.deposits = res.data;
        this.totalH = res.total;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    
  }
  LimpiarData_a(){
    this.monto_historial_a='';
    this.fechaInicioH = '';
    this.fechaFinH = '';
   
    const today = moment().toDate();
    
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
     
    this.filtro_da = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.monto_historial_a);

    this.filtro_da.externalUser=persona;
    this.filtro_da.usada = 1;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHA, this.pageSizeHA, this.filtro_da).subscribe(
      res => {
        this.depositsa = res.data;
        this.totalHA = res.total;
        this.depositsService.addDeposits(this.depositsa);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    
  }
  LimpiarData_r(){
    this.monto_historial_r='';
    this.fechaInicioH = '';
    this.fechaFinH = '';
   
    const today = moment().toDate();
    
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
     
    this.filtro_dr = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    //persona.personaId = usuario.idPersona;    
    
    persona.personaId = Number(this.monto_historial_r);
    this.filtro_dr.externalUser=persona;
    this.filtro_dr.usada = -1;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHR, this.pageSizeHR, this.filtro_dr).subscribe(
      res => {
        this.depositsr = res.data;
        this.totalHR = res.total;
        this.depositsService.addDeposits(this.depositsa);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    
  }
  LimpiarData_p(){
    this.monto_historial_p='';
    this.fechaInicioH = '';
    this.fechaFinH = '';
   
    const today = moment().toDate();
    
    this.rangoFechas = {
      from: new Date(today.getFullYear(), today.getMonth(), 1),
      to: new Date(today.getFullYear(), today.getMonth(), 1)
    };
     
    this.filtro_dp = new Deposit;
    this.depositsService.deposits = [];
    let persona = new ExternalUser;
    persona.personaId = Number(this.monto_historial_p);   
    
    this.filtro_dp.externalUser=persona;
    this.filtro_dp.usada = 0;
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPageHP, this.pageSizeHP, this.filtro_dp).subscribe(
      res => {
        this.depositsp = res.data;
        this.totalHP = res.total;
        this.depositsService.addDeposits(this.depositsp);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    
  }
  limpiarDataOp(){
    this.fechaInicio = '2008-01-01';
    this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');  
    const today = moment().toDate();
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(1, this.pageSizeOpe, this.filtrot, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        //console.log(res.data);
        this.guarantees = res.data;
        
        this.totalOpe = res.total;
        this.currentPageOpe = 1;
      },
      err => {
        console.error(err);
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
  /*Termina funcion que limpia los filtros seleccionados y muestra todos los datos en la tabla de Garantias*/
  /*Funciones que detecta el cambio en los filtros en la tabla de Garantias*/
  filAcreedorChanged(filAcreedor: String) {
    
    this.filtrog.idAcreedor = filAcreedor;
    //this.BuscarData();
  }
  filDeudoresChanged(filDeudor: String) {
    
    this.filtrog.idDeudor = filDeudor;
    
    //this.refreshData();
  }
  /*Terminan funciones que detecta el cambio en los filtros en la tabla de Garantias*/
  /*Funciones que llenan los datos de los filtros en la tabla de Garantias*/
  llenar_select_deu(){
    //console.log("Entra a llenar");
    
    this.httpSubscription_s = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
    
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
          
          this.garantias_select.forEach(garantia_s => {

            this.index = this.index + 1;
            var existe = this.deudores_filtro.some((deudor: any) => deudor.value === garantia_s.idDeudor);           

            if (!existe) {
              this.deudores_filtro.push({
                value: garantia_s.idDeudor,
                label: garantia_s.nDeudor
              });
            }
            var existe2 = this.acreedores_filtro.some((acreedor: any) => acreedor.value === garantia_s.idAcreedor);
            
            if (!existe2) {
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
  llenar_select_acre(){
    
    
    this.httpSubscription_s = this.garantiaService.fetchData(null, null, null, this.filtrog)
      .pipe(
        finalize(() => {
          
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
  /*Terminan funciones que llenan los datos de los filtros en la tabla de Garantias*/

  onViewPartsClicked(guarantee: Transaction) {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    /*this.partsHttpSubscripcion = this.guaranteesService.fetchPartData(guarantee.idTramite).subscribe(
      res => {
        this.parts = res;
        this.modalActions.emit({action:"modal",params:['open']});
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );*/
    this.partsHttpSubscripcion = this.guaranteesService.fetchDetailData(guarantee.idTramite, guarantee.guarantee.idGarantia).subscribe(
      res => {
        this.modalTransaction = res;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        this.modalActions.emit({action:"modal",params:['open']});
      }
    );
    /*this.modalTransaction = guarantee;
    this.modalActions.emit({action:"modal",params:['open']});*/
  }
}
