import { Component, OnInit } from '@angular/core';
import { NgxDateRangePickerOptions } from 'ngx-daterangepicker';
import { LoadingService } from '../shared/loading.service';
import { Chart } from 'chart.js';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { StatsFilter } from '../shared/stats-filter.model';
import { SurveyStats } from '../shared/survey-stats.model';
import { SurveyService } from '../shared/survey.service';
import { ExcelService } from '../shared/excel.service';
import { Deposit } from '../shared/deposit.model';
import { Counter } from '../shared/counter.model';

@Component({
  selector: 'app-survey-stats',
  templateUrl: './survey-stats.component.html',
  styleUrls: ['./survey-stats.component.css']
})
export class SurveyStatsComponent implements OnInit {
  options: NgxDateRangePickerOptions;
  rangoFechas: any;
  public loading = false;
  surveyStats: SurveyStats[];
  statsHttpSubscription: Subscription;
  filtro: StatsFilter;
  chart:Chart = [];
  chart1:Chart = [];
  chart2:Chart = [];
  chart3:Chart = [];
  chart4:Chart = [];
  chart5:Chart = [];
  chart6:Chart = [];  
  surveySum: Counter[];
  sumHttpSubscription: Subscription;

  constructor(private surveyService: SurveyService,
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
    this.statsHttpSubscription = this.surveyService.fetchStatsData(this.filtro).subscribe(
      data => {
        this.surveyStats = data;                
        let data1si = data.map(res => res.data1si);
        let data1no = data.map(res => res.data1no);
        let colors = [
          'rgba(33, 102, 172, 0.46)',
          'rgba(67, 147, 195, 0.46)',
          'rgba(214, 96, 77, 0.46)'
        ];

        if (typeof this.chart.id === 'undefined') {
          this.chart = new Chart('canvas', {
            type: 'bar',
            data: {
              labels: ['usuario'],
              datasets: [
                {
                    label: 'SI',
                    data: data1si,
                    backgroundColor: 'rgba(30, 169, 224, 0.8)',
                    yAxisID: 'y-cantidad'                 
                },
                {
                    label: 'NO',
                    data: data1no,
                    backgroundColor: 'rgba(77,83,96,0.2)',
                    yAxisID: 'y-cantidad'             
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
                  },
                  ticks: { 
                    min: 0
                  }
                }]
              }
            }
          });          
        } 

        if (typeof this.chart1.id === 'undefined') {
            this.chart1 = new Chart('canvas1', {
              type: 'bar',
              data: {
                labels: ['1','2','3','4','5'],
                datasets: [
                  {
                      label: 'Meses',
                      data: [5,7,9,6,9,0],
                      backgroundColor: 'rgba(30, 169, 224, 0.8)'                 
                  }
                ]            
              }
            });
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
    const temp = this.surveyStats.map(el => ({
      fecha: el.fecha      
    }));
    this.excelService.export(temp, 'depositos');
  }
}
