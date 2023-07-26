import { Component, OnInit, ViewChild, ElementRef, EventEmitter, Input } from '@angular/core';
import { Counter } from '../shared/counter.model';
import { Subscription } from 'rxjs';
import { Chart } from 'chart.js';
import * as moment from 'moment';
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from 'ngx-daterangepicker';

import { LoadingService } from '../shared/loading.service';
import { GuaranteesService } from '../shared/guarantees.service';
import { GuaranteeStats } from '../shared/guarantee-stats.model';
import { StatsFilter } from '../shared/stats-filter.model';
import { ExcelService } from '../shared/excel.service';
import { Guarantee } from '../shared/guarantee.model';
import { ExternalUser } from '../shared/external-user.model';
import { Transaction } from '../shared/transaction.model';
import { Part } from '../shared/part.model';
import { MaterializeAction } from 'angular2-materialize';
import { environment } from '../../environments/environment';
import { TransactionPreview } from '../shared/transaction-preview.model';

@Component({
  selector: 'app-guarantees-report',
  templateUrl: './guarantees-report.component.html',
  styleUrls: ['./guarantees-report.component.css']
})
export class GuaranteesReportComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  @Input() dateRange: NgxDateRangePickerComponent;
  rangoFechas: string | any;
  public loading = false;
  guarantees: Transaction[];
  exportGuarantees: Transaction[];
  httpSubscription: Subscription;
  exportHttpSubscription: Subscription;
  partsHttpSubscripcion: Subscription;
  filtro: Transaction;
  pageSize: number = 10;
  currentPage: number = 1;
  total: number;
  filNombre: string;
  filNumero: number;
  @ViewChild('filNombreInput') filNombreInput: ElementRef;
  @ViewChild('filNumeroInput') filNumeroInput: ElementRef;
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalTransaction: TransactionPreview;
  parts: Part[];
  baseUrl: string = environment.base_url;
  fechaInicio: string;
  fechaFin: string;
  incluirTodos: false;
  activate_loading: boolean = false;
  download_files: boolean = false;

  constructor(private guaranteesService: GuaranteesService,
    private loadingService: LoadingService,
    private excelService: ExcelService) { }

  ngOnInit() {
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
      startOfWeek: 0
    };
    this.fechaInicio = moment().format('YYYY-MM-') + '01';
    this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.filtro = new Transaction;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(this.currentPage, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.guarantees = res.data;
        console.log(res.data);
        this.total = res.total;
      },
      err => {
        console.error(err);
      },
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }


  startSignature(idTramite, idGarantia){
    this.activate_loading = true;
    this.guaranteesService.signatureFile(idTramite, idGarantia).subscribe(response => {
      console.log(" data" ,response)
      if(response.data != ""){
        this.guaranteesService.checkSignature(response.data).subscribe(resp => {
          if(resp == 1){
            this.guaranteesService.downloadFiles(response.data).subscribe(r => {
              const blob = new Blob([r], { type: 'application/pdf'})
              const file = document.createElement('a')
              file.download = response.data + '.pdf'
              file.href = (window.URL).createObjectURL(blob)
              file.click()
              this.activate_loading = false;
            },
            error => {
              console.log(error)
            })
          }
        },
        error => {
          console.log(error)
        })
      }
    })
  }

  onPageChange(page: number) {
    this.guarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(page, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.guarantees = res.data;
        this.total = res.total;
        this.currentPage = page;
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

  filNombreChanged(filNombre: string) {
    let solicitante = new ExternalUser;
    solicitante.name = filNombre;
    this.filtro.solicitante = solicitante;
    this.refreshData();
  }

  filNumeroChanged(filNumero: number) {
    let guarantee = new Guarantee;
    guarantee.idGarantia = filNumero;
    this.filtro.guarantee = guarantee;
    this.refreshData();
  }

  onFilNombre() {
    let event = new Event('blur', {});
    this.filNombreInput.nativeElement.dispatchEvent(event);
  }

  onFilNumero() {
    let event = new Event('blur', {});
    this.filNumeroInput.nativeElement.dispatchEvent(event);
  }

  refreshData() {
    this.guarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(1, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
      res => {
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
  }

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

  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.parts = [];
  }

  onRangeChanged(event) {
    if (this.fechaInicio !== event.from || this.fechaFin !== event.to) {
      this.fechaInicio = event.from;
      this.fechaFin = event.to;
      this.refreshData();
    }
  }

  onTodosChanged(event) {
    // cambiar rango
    if (this.incluirTodos) {
      this.fechaInicio = '2008-01-01';
      this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    } else {
      console.log(this.rangoFechas);
      this.fechaInicio = moment(this.rangoFechas.from).format('YYYY-MM-DD');
      this.fechaFin = moment(this.rangoFechas.to).format('YYYY-MM-DD');
    }
    this.refreshData();

    /*if (this.statsFiltro.im !== this.incluirTodos) {
      this.statsFiltro.im = this.incluirTodos;
      this.updateData(true);
    }*/
  }

  exportData() {
    this.exportGuarantees = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchData(null, null, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.exportGuarantees = res.data;
        console.log(res.data);
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
}
