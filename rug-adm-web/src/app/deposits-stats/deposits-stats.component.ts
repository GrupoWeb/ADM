import { Component, OnInit } from '@angular/core';
import { NgxDateRangePickerOptions } from 'ngx-daterangepicker';
import { LoadingService } from '../shared/loading.service';
import { Chart } from 'chart.js';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { StatsFilter } from '../shared/stats-filter.model';
import { DepositStats } from '../shared/deposit-stats.model';
import { DepositsService } from '../shared/deposits.service';
import { ExcelService } from '../shared/excel.service';
import { Deposit } from '../shared/deposit.model';
import { Counter } from '../shared/counter.model';

@Component({
  selector: 'app-deposits-stats',
  templateUrl: './deposits-stats.component.html',
  styleUrls: ['./deposits-stats.component.css']
})
export class DepositsStatsComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  rangoFechas: any;
  public loading = false;
  depositsStats: DepositStats[];
  statsHttpSubscription: Subscription;
  filtro: StatsFilter;
  chart:Chart = [];
  depositsSum: Counter[];
  sumHttpSubscription: Subscription;

  constructor(private depositsService: DepositsService,
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
    this.filtro.fields = 'cf,eb,tp';
    this.filtro.cf = 'S';
    this.filtro.dis = 4;

    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.updateData(true);
  }

  updateData(updateSummary: boolean) {
    if (updateSummary) {
      this.sumHttpSubscription = this.depositsService.fetchSumData(this.filtro).subscribe(
        data => {
          this.depositsSum = data;
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      );
    }

    this.statsHttpSubscription = this.depositsService.fetchStatsData(this.filtro).subscribe(
      data => {
        this.depositsStats = data;
        let aprobadas = data.map(res => res.aprobadas);
        let pendientesAprobacion = data.map(res => res.pendientesAprobacion);
        let rechazadas = data.map(res => res.rechazadas);
        let variacionAprobadas = data.map(res => res.variacionAprobadas);
        let variacionPendientesAprobacion = data.map(res => res.variacionPendientesAprobacion);
        let variacionRechazadas = data.map(res => res.variacionRechazadas);
        let alldates = data.map(res => res.labelFecha);
        let colors = [
          'rgba(33, 102, 172, 0.46)',
          'rgba(67, 147, 195, 0.46)',
          'rgba(214, 96, 77, 0.46)'
        ];

        if (typeof this.chart.id === 'undefined') {
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: alldates,
              datasets: [
                {
                  label: 'Aprobadas',
                  data: aprobadas,
                  borderColor: colors[0],
                  backgroundColor: colors[0],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Pendientes de aprobación',
                  data: pendientesAprobacion,
                  borderColor: colors[1],
                  backgroundColor: colors[1],
                  fill: true,
                  yAxisID: 'y-cantidad'
                },
                {
                  label: 'Rechazadas',
                  data: rechazadas,
                  borderColor: colors[2],
                  backgroundColor: colors[2],
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
                }]
              }
            }
          });
        } else {
          if (alldates.length === 1) {
            alldates.unshift('');
            alldates.push('');
            aprobadas.unshift(null);
            aprobadas.push(null);
            pendientesAprobacion.unshift(null);
            pendientesAprobacion.push(null);
            rechazadas.unshift(null);
            rechazadas.push(null);
          }
          this.chart.data.labels = alldates;
          this.chart.data.datasets[0].data = aprobadas;
          this.chart.data.datasets[1].data = pendientesAprobacion;
          this.chart.data.datasets[2].data = rechazadas;
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

  exportData() {
    const temp = this.depositsStats.map(el => ({
      fecha: el.fecha,
      aprobadas: el.aprobadas,
      variacionAprobadas: el.variacionAprobadas,
      pendientesAprobacion: el.pendientesAprobacion,
      variacionPendientesAprobacion: el.variacionPendientesAprobacion,
      rechazadas: el.rechazadas,
      variacionRechazadas: el.variacionRechazadas,
    }));
    this.excelService.export(temp, 'depositos');
  }
}
