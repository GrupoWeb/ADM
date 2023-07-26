import { Component, OnInit } from '@angular/core';
import { Counter } from '../shared/counter.model';
import { Subscription } from 'rxjs';
import { Chart } from 'chart.js';
import * as moment from 'moment';
import { NgxDateRangePickerOptions } from 'ngx-daterangepicker';

import { LoadingService } from '../shared/loading.service';
import { GuaranteesService } from '../shared/guarantees.service';
import { GuaranteeStats } from '../shared/guarantee-stats.model';
import { StatsFilter } from '../shared/stats-filter.model';
import { ExcelService } from '../shared/excel.service';

@Component({
  selector: 'app-guarantees-stats',
  templateUrl: './guarantees-stats.component.html',
  styleUrls: ['./guarantees-stats.component.css']
})
export class GuaranteesStatsComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  rangoFechas: any;
  public loading = false;
  guaranteesCount: Counter[];
  guaranteesCountType: Counter[];
  guaranteesStats: GuaranteeStats[];
  countHttpSubscription: Subscription;
  countTypeHttpSubscription: Subscription;
  statsHttpSubscription: Subscription;
  filtro: StatsFilter;
  chart:Chart = [];
  incluirMigracion = false;

  constructor(private guaranteesService: GuaranteesService,
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

    this.filtro = new StatsFilter;
    this.filtro.fechaInicio = moment().format('YYYY-MM-') + '01';
    this.filtro.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
    this.filtro.fields = 'cf,tt';
    this.filtro.cf = 'S';
    this.filtro.dis = 4;
    this.filtro.im = false;

    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.updateData(true);
  }

  updateData(updateSummary: boolean) {
    if (updateSummary) {
      this.countHttpSubscription = this.guaranteesService.fetchCountData(this.filtro).subscribe(
        data => {
          this.guaranteesCount = data;
          // this.guaranteesCount = [...this.guaranteesCount];
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

      this.countTypeHttpSubscription = this.guaranteesService.fetchCountTypeData(this.filtro).subscribe(
        data => {
          this.guaranteesCountType = data;
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

    this.statsHttpSubscription = this.guaranteesService.fetchStatsData(this.filtro).subscribe(
      data => {
        this.guaranteesStats = data;
        let inscripciones = data.map(res => res.inscripciones);
        let cancelaciones = data.map(res => res.cancelaciones);
        let certificaciones = data.map(res => res.certificaciones);
        let modificaciones = data.map(res => res.modificaciones);
        let renovaciones = data.map(res => res.renovaciones);
        let consultas = data.map(res => res.consultas);
        let ejecuciones = data.map(res => res.ejecuciones);
        let traslados = data.map(res => res.traslados);
        let vinculaciones = data.map(res => res.vinculaciones);
        let variacionInscripciones = data.map(res => res.variacionInscripciones);
        let variacionCancelaciones = data.map(res => res.variacionCancelaciones);
        let variacionCertificaciones = data.map(res => res.variacionCertificaciones);
        let variacionModificaciones = data.map(res => res.variacionModificaciones);
        let variacionRenovaciones = data.map(res => res.variacionRenovaciones);
        let variacionConsultas = data.map(res => res.variacionConsultas);
        let variacionEjecuciones = data.map(res => res.variacionEjecuciones);
        let variacionTraslados = data.map(res => res.variacionTraslados);
        let variacionVinculaciones = data.map(res => res.variacionVinculaciones);
        let alldates = data.map(res => res.labelFecha);
        let colorsFill = [
          '#2166ac',
          '#4393c3',
          '#d6604d',
          '#b2182b',
          '#1a9850',
          '#66bd63',
          '#d73027',
          '#f46d43',
          '#7120a4'
        ];
        let colors = [
          'rgba(33, 102, 172, 0.46)',
          'rgba(67, 147, 195, 0.46)',
          'rgba(214, 96, 77, 0.46)',
          'rgba(178, 24, 43, 0.46)',
          'rgba(26, 152, 80, 0.46)',
          'rgba(102, 189, 99, 0.46)',
          'rgba(215, 48, 39, 0.46)',
          'rgba(244, 109, 67, 0.46)',
          'rgba(113, 32, 164, 0.46)'
        ];

        if (typeof this.chart.id === 'undefined') {
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: alldates,
              datasets: [
                {
                  label: 'Inscripciones',
                  data: inscripciones,
                  borderColor: colors[0],
                  backgroundColor: colors[0],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Cancelaciones',
                  data: cancelaciones,
                  borderColor: colors[1],
                  backgroundColor: colors[1],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Certificaciones',
                  data: certificaciones,
                  borderColor: colors[2],
                  backgroundColor: colors[2],
                  fill: true
                },
                {
                  label: 'Modificaciones',
                  data: modificaciones,
                  borderColor: colors[3],
                  backgroundColor: colors[3],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Renovaciones',
                  data: renovaciones,
                  borderColor: colors[4],
                  backgroundColor: colors[4],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Consultas',
                  data: consultas,
                  borderColor: colors[5],
                  backgroundColor: colors[5],
                  fill: true
                },
                {
                  label: 'Ejecuciones',
                  data: ejecuciones,
                  borderColor: colors[6],
                  backgroundColor: colors[6],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Traslados',
                  data: traslados,
                  borderColor: colors[7],
                  backgroundColor: colors[7],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Vinculaciones',
                  data: vinculaciones,
                  borderColor: colors[8],
                  backgroundColor: colors[8],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                /*{
                  label: 'Variacion Inscripciones',
                  data: variacionInscripciones,
                  borderColor: colorsFill[0],
                  backgroundColor: colorsFill[0],
                  fill: true,
                  yAxisID: 'y-variacion'
                },*/
                /*{
                  label: 'Variacion Cancelaciones',
                  data: variacionCancelaciones,
                  borderColor: colorsFill[1],
                  backgroundColor: colorsFill[1],
                  fill: true,
                  yAxisID: 'y-variacion'
                },*/
                /*{
                  label: 'Variacion Certificaciones',
                  data: variacionCertificaciones,
                  borderColor: colorsFill[2],
                  backgroundColor: colorsFill[2],
                  fill: true
                },*/
                /*{
                  label: 'Variacion Modificaciones',
                  data: variacionModificaciones,
                  borderColor: colorsFill[3],
                  backgroundColor: colorsFill[3],
                  fill: true,
                  yAxisID: 'y-variacion'
                },*/
                /*{
                  label: 'Variacion Renovaciones',
                  data: variacionRenovaciones,
                  borderColor: colorsFill[4],
                  backgroundColor: colorsFill[4],
                  fill: true
                },*/
                /*{
                  label: 'Variacion Consultas',
                  data: variacionConsultas,
                  borderColor: colorsFill[5],
                  backgroundColor: colorsFill[5],
                  fill: true
                },*/
                /*{
                  label: 'Variacion Ejecuciones',
                  data: variacionEjecuciones,
                  borderColor: colorsFill[6],
                  backgroundColor: colorsFill[6],
                  fill: true
                },*/
                /*{
                  label: 'Variacion Traslados',
                  data: variacionTraslados,
                  borderColor: colorsFill[7],
                  backgroundColor: colorsFill[7],
                  fill: true,
                  yAxisID: 'y-variacion'
                }*/
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
                }],
                yAxes: [{
                  type: 'linear',
                  display: true,
                  position: 'left',
                  id: 'y-cantidad',
                  scaleLabel: {
                    display: true,
                    labelString: 'Cantidad'
                  }
                }/*, {
                  type: 'linear',
                  display: true,
                  position: 'right',
                  id: 'y-variacion',
                  scaleLabel: {
                    display: true,
                    labelString: '% Variacion'
                  }
                }*/]
              }
            }
          });
        } else {
          if (alldates.length === 1) {
            alldates.unshift('');
            alldates.push('');
            inscripciones.unshift(null);
            inscripciones.push(null);
            cancelaciones.unshift(null);
            cancelaciones.push(null);
            certificaciones.unshift(null);
            certificaciones.push(null);
            modificaciones.unshift(null);
            modificaciones.push(null);
            renovaciones.unshift(null);
            renovaciones.push(null);
            consultas.unshift(null);
            consultas.push(null);
            ejecuciones.unshift(null);
            ejecuciones.push(null);
            traslados.unshift(null);
            traslados.push(null);
            vinculaciones.unshift(null);
            vinculaciones.push(null);
          }
          this.chart.data.labels = alldates;
          this.chart.data.datasets[0].data = inscripciones;
          this.chart.data.datasets[1].data = cancelaciones;
          this.chart.data.datasets[2].data = certificaciones;
          this.chart.data.datasets[3].data = modificaciones;
          this.chart.data.datasets[4].data = renovaciones;
          this.chart.data.datasets[5].data = consultas;
          this.chart.data.datasets[6].data = ejecuciones;
          this.chart.data.datasets[7].data = traslados;
          this.chart.data.datasets[8].data = vinculaciones;
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

  onRangeChanged(event) {
    if (this.filtro.fechaInicio !== event.from || this.filtro.fechaFin !== event.to) {
      this.filtro.fechaInicio = event.from;
      this.filtro.fechaFin = event.to;
      this.updateData(true);
    }
  }

  onPeriodChanged(event) {
    if (this.filtro.cf !== event.target.value) {
      this.filtro.cf = event.target.value;
      this.updateData(false);
    }
  }

  onDiaChanged(event) {
    if (this.filtro.dis !== event.target.value) {
      this.filtro.dis = event.target.value;
      this.updateData(false);
    }
  }

  onMigracionChanged(event) {
    if (this.filtro.im !== this.incluirMigracion) {
      this.filtro.im = this.incluirMigracion;
      this.updateData(true);
    }
  }

  exportData() {
    const temp = this.guaranteesStats.map(el => ({
      fecha: el.fecha,
      inscripciones: el.inscripciones,
      variacionInscripciones: el.variacionInscripciones,
      cancelaciones: el.cancelaciones,
      variacionCancelaciones: el.variacionCancelaciones,
      certificaciones: el.certificaciones,
      variacionCertificaciones: el.variacionCertificaciones,
      modificaciones: el.modificaciones,
      variacionModificaciones: el.variacionModificaciones,
      renovaciones: el.renovaciones,
      variacionRenovaciones: el.variacionRenovaciones,
      consultas: el.consultas,
      variacionConsultas: el.variacionConsultas,
      ejecuciones: el.ejecuciones,
      variacionEjecuciones: el.variacionEjecuciones,
      traslados: el.traslados,
      variacionTraslados: el.variacionTraslados,
      vinculaciones: el.vinculaciones,
      variacionVinculaciones: el.variacionVinculaciones,
    }));
    this.excelService.export(temp, 'operaciones');
  }

  exportDataDeudores() {
    this.guaranteesService.fetchTypeData(this.filtro).subscribe(
      data => {
        const temp = data.map(el => ({
          "Nombre": el.descripcion,
          "Tramite": el.subdescripcion,
          "Cantidad": el.total
        }));
        this.excelService.export(temp, 'deudores');
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
