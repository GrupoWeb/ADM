import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { garantia } from '../shared/garantias/garantia.model';
import { Guarantee } from '../shared/guarantee.model';
import { GuaranteesService } from '../shared/guarantees.service';
import { LoadingService } from '../shared/loading.service';
import { Transaction } from '../shared/transaction.model';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-firma',
  templateUrl: './firma.component.html',
  styleUrls: ['./firma.component.css']
})
export class FirmaComponent implements OnInit {

  Garantia: garantia;
  Warranty: number;
  httpSubscription: Subscription;
  currentPage: number = 1;
  pageSize: number = 10;
  fechaInicio: string;
  fechaFin: string;
  tablaShow: boolean;
  baseUrl: string = environment.base_url;
  activate_loading: boolean = false;
  download_files: boolean = false;


  @ViewChild('idWarranty') idWarranty: ElementRef;

  constructor(
    private loadingService: LoadingService,
    private guaranteesService: GuaranteesService
  ) { }

  ngOnInit() {
    this.Garantia = new garantia;
    this.Garantia.filtro = new Transaction;
    this.tablaShow = false;
    // this.Garantia.loading = true;
    // this.loadingService.changeLoading(this.Garantia.loading);

  }

  getGarantia() {
    this.Garantia.idGarantia = new Event('blur', {});
    this.idWarranty.nativeElement.dispatchEvent(this.Garantia.idGarantia);
  }

  changeNumberWarranty(Warranty: number) {
    this.Garantia.guarantee = new Guarantee;
    this.Garantia.guarantee.idGarantia = Warranty;
    this.Garantia.filtro.guarantee = this.Garantia.guarantee;
    this.refresh();
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

  sleep(ms) {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  refresh() {
    this.Garantia.guarantees = [];
    this.activate_loading = true;
    // this.Garantia.loading = true;
    this.loadingService.changeLoading(this.Garantia.loading);
    this.httpSubscription = this.guaranteesService.fetchData(
      1, this.pageSize, this.Garantia.filtro, this.fechaInicio, this.fechaFin
    )
      .subscribe(
        response => {
          this.Garantia.guarantees = response.data;
          console.log(response.data);
          this.currentPage = 1;
          this.tablaShow = true;
          this.activate_loading = false;
        },
        error => {
          console.log(error)
          this.Garantia.loading = false;
          this.loadingService.changeLoading(this.Garantia.loading);
        },
        () => {
          this.Garantia.loading = false;
          this.loadingService.changeLoading(this.Garantia.loading);
        }
      );
  }

}
