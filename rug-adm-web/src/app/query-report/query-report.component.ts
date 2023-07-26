import { Component, OnInit, Input } from '@angular/core';
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from 'ngx-daterangepicker';
import { Subscription } from 'rxjs';
import { GuaranteesService } from '../shared/guarantees.service';
import { LoadingService } from '../shared/loading.service';
import { ExcelService } from '../shared/excel.service';
import * as moment from 'moment';
import { Query } from '../shared/query.model';

@Component({
  selector: 'app-query-report',
  templateUrl: './query-report.component.html',
  styleUrls: ['./query-report.component.css']
})
export class QueryReportComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  @Input() dateRange: NgxDateRangePickerComponent;
  rangoFechas: string | any;
  public loading = false;
  queries: Query[];
  exportQueries: Query[];
  httpSubscription: Subscription;
  exportHttpSubscription: Subscription;
  pageSize: number = 10;
  currentPage: number = 1;
  total: number;
  fechaInicio: string;
  fechaFin: string;

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
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchQueryData(this.currentPage, this.pageSize, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.queries = res.data;
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

  onPageChange(page: number) {
    this.queries = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchQueryData(page, this.pageSize, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.queries = res.data;
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

  refreshData() {
    this.queries = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchQueryData(1, this.pageSize, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.queries = res.data;
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

  onRangeChanged(event) {
    if (this.fechaInicio !== event.from || this.fechaFin !== event.to) {
      this.fechaInicio = event.from;
      this.fechaFin = event.to;
      this.refreshData();
    }
  }

  exportData() {
    this.exportQueries = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.guaranteesService.fetchQueryData(null, null, this.fechaInicio, this.fechaFin).subscribe(
      res => {
        this.exportQueries = res.data;
        const temp = this.exportQueries.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Solicitante": el.nombrePersona ? el.nombrePersona : '',
        }));
        this.excelService.export(temp, 'consultas');
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
