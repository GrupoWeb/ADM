import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ExternalUser } from '../shared/external-user.model';
import { Subscription } from 'rxjs';
import { ExternalUsersService } from '../shared/external-users.service';
import { LoadingService } from '../shared/loading.service';
import { Counter } from '../shared/counter.model';
import { ExternalUserStats } from '../shared/external-user-stats.model';
import { PaginationInstance } from 'ngx-pagination';
import { NgxDateRangePickerOptions } from 'ngx-daterangepicker';
import * as moment from 'moment';
import { Chart } from 'chart.js';
import { StatsFilter } from '../shared/stats-filter.model';
import { ExcelService } from '../shared/excel.service';

@Component({
  selector: 'app-external-users-stats',
  templateUrl: './external-users-stats.component.html',
  styleUrls: ['./external-users-stats.component.css']
})
export class ExternalUsersStatsComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  rangoFechas: any;
  public loading = false;
  users: ExternalUser[];
  usersCount: Counter[];
  usersStats: ExternalUserStats[];
  total: number;
  pageSize: number = 5;
  currentPage: number = 1;
  httpSubscription: Subscription;
  countHttpSubscription: Subscription;
  statsHttpSubscription: Subscription;
  filtro: ExternalUser;
  filNombre: string;
  filCorreo: string;
  filDocId: string;
  filNit: string;
  @ViewChild('filNombreInput') filNombreInput: ElementRef;
  @ViewChild('filCorreoInput') filCorreoInput: ElementRef;
  @ViewChild('filDocIdInput') filDocIdInput: ElementRef;
  @ViewChild('filNitInput') filNitInput: ElementRef;
  chart: Chart = [];
  incluirMigracion = false;
  statsFiltro: StatsFilter;

  constructor(private externalUsersService: ExternalUsersService,
    private loadingService: LoadingService,
    private excelService: ExcelService) { }

  ngOnInit() {
    this.rangoFechas = moment().format('DD/MM/YYYY');
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

    this.filtro = new ExternalUser;
    this.externalUsersService.users = [];

    this.statsFiltro = new StatsFilter;
    this.statsFiltro.fechaInicio = moment().format('YYYY-MM-') + '01';
    this.statsFiltro.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.statsFiltro.fields = 'cf,su';
    this.statsFiltro.cf = 'S';
    this.statsFiltro.dis = 4;
    this.statsFiltro.im = false;

    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchData("ALLNM", this.currentPage, this.pageSize, this.filtro).subscribe(
      res => {
        this.users = res.data;
        this.total = res.total;
        this.externalUsersService.addUsers(this.users);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );

    this.updateData(true);
  }

  updateData(updateSummary: boolean) {
    if (updateSummary) {
      this.countHttpSubscription = this.externalUsersService.fetchCountData(this.statsFiltro).subscribe(
        data => {
          this.usersCount = data;
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
    }

    this.statsHttpSubscription = this.externalUsersService.fetchStatsData(this.statsFiltro).subscribe(
      data => {
        this.usersStats = data;
        let activos = data.map(res => res.activos);
        let pendientesActivacion = data.map(res => res.pendientesActivacion);
        let pendientesAprobacion = data.map(res => res.pendientesAprobacion);
        let rechazados = data.map(res => res.rechazados);
        let variacionActivos = data.map(res => res.variacionActivos);
        let variacionPendientesActivacion = data.map(res => res.variacionPendientesActivacion);
        let variacionPendientesAprobacion = data.map(res => res.variacionPendientesAprobacion);
        let variacionRechazados = data.map(res => res.variacionRechazados);
        let alldates = data.map(res => res.labelFecha);
        let colors = [
          'rgba(33, 102, 172, 0.46)',
          'rgba(67, 147, 195, 0.46)',
          'rgba(214, 96, 77, 0.46)',
          'rgba(178, 24, 43, 0.46)',
          'rgba(26, 152, 80, 0.46)',
          'rgba(102, 189, 99, 0.46)',
          'rgba(215, 48, 39, 0.46)',
          'rgba(244, 109, 67, 0.46)'
        ];

        if (typeof this.chart.id === 'undefined') {
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: alldates,
              datasets: [
                {
                  label: 'Activos',
                  data: activos,
                  borderColor: colors[0],
                  backgroundColor: colors[0],
                  fill: true
                },
                {
                  label: 'Pendientes de activación',
                  data: pendientesActivacion,
                  borderColor: colors[1],
                  backgroundColor: colors[1],
                  fill: true
                },
                {
                  label: 'Pendientes de aprobación',
                  data: pendientesAprobacion,
                  borderColor: colors[2],
                  backgroundColor: colors[2],
                  fill: true
                },
                {
                  label: 'Rechazados',
                  data: rechazados,
                  borderColor: colors[3],
                  backgroundColor: colors[3],
                  fill: true
                }
              ]
            },
            options: {
              elements: {
                line: {
                  tension: 0
                }
              },
              legend: {
                display: true
              },
              scales: {
                xAxes: [{
                  display: true
                }]
              }
            }
          });
        } else {
          if (alldates.length === 1) {
            alldates.unshift('');
            alldates.push('');
            activos.unshift(null);
            activos.push(null);
            pendientesActivacion.unshift(null);
            pendientesActivacion.push(null);
            pendientesAprobacion.unshift(null);
            pendientesAprobacion.push(null);
            rechazados.unshift(null);
            rechazados.push(null);
          }
          this.chart.data.labels = alldates;
          this.chart.data.datasets[0].data = activos;
          this.chart.data.datasets[1].data = pendientesActivacion;
          this.chart.data.datasets[2].data = pendientesAprobacion;
          this.chart.data.datasets[3].data = rechazados;
          this.chart.update();
        }
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

  onPageChange(page: number) {
    this.externalUsersService.users = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchData("ALLNM", page, this.pageSize, this.filtro).subscribe(
      res => {
        this.users = res.data;
        this.total = res.total;
        this.currentPage = page;
        this.externalUsersService.addUsers(this.users);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  filNombreChanged(filNombre: string) {
    this.filtro.name = filNombre;
    this.refreshData();
  }

  filCorreoChanged(filCorreo: string) {
    this.filtro.email = filCorreo;
    this.refreshData();
  }

  filDocIdChanged(filDocId: string) {
    this.filtro.docId = filDocId;
    this.refreshData();
  }

  filNitChanged(filNit: string) {
    this.filtro.nit = filNit;
    this.refreshData();
  }

  onFilNombre() {
    let event = new Event('blur', {});
    this.filNombreInput.nativeElement.dispatchEvent(event);
  }

  onFilCorreo() {
    let event = new Event('blur', {});
    this.filCorreoInput.nativeElement.dispatchEvent(event);
  }

  onFilDocId() {
    let event = new Event('blur', {});
    this.filDocIdInput.nativeElement.dispatchEvent(event);
  }

  onFilNit() {
    let event = new Event('blur', {});
    this.filNitInput.nativeElement.dispatchEvent(event);
  }

  refreshData() {
    this.externalUsersService.users = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchData("ALLNM", 1, this.pageSize, this.filtro).subscribe(
      res => {
        this.users = res.data;
        this.total = res.total;
        this.currentPage = 1;
        this.externalUsersService.addUsers(this.users);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onRangeChanged(event) {
    if (this.statsFiltro.fechaInicio !== event.from || this.statsFiltro.fechaFin !== event.to) {
      this.statsFiltro.fechaInicio = event.from;
      this.statsFiltro.fechaFin = event.to;
      this.updateData(true);
    }
  }

  onPeriodChanged(event) {
    if (this.statsFiltro.cf !== event.target.value) {
      this.statsFiltro.cf = event.target.value;
      this.updateData(false);
    }
  }

  onDiaChanged(event) {
    if (this.statsFiltro.dis !== event.target.value) {
      this.statsFiltro.dis = event.target.value;
      this.updateData(false);
    }
  }

  onMigracionChanged(event) {
    if (this.statsFiltro.im !== this.incluirMigracion) {
      this.statsFiltro.im = this.incluirMigracion;
      this.updateData(true);
    }
  }

  exportData() {
    const temp = this.usersStats.map(el => ({
      fecha: el.fecha,
      activos: el.activos,
      variacionActivos: el.variacionActivos,
      pendientesActivacion: el.pendientesActivacion,
      variacionPendientesActivacion: el.variacionPendientesActivacion,
      pendientesAprobacion: el.pendientesAprobacion,
      variacionPendientesAprobacion: el.variacionPendientesAprobacion,
      rechazados: el.rechazados,
      variacionRechazados: el.variacionRechazados,
    }));
    this.excelService.export(temp, 'usuarios_stats');
  }
}
