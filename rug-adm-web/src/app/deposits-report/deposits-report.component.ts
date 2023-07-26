import { Component, OnInit, ElementRef, ViewChild, EventEmitter } from '@angular/core';
import { Deposit } from '../shared/deposit.model';
import { Subscription } from 'rxjs/Subscription';
import { DepositsService } from '../shared/deposits.service';
import { LoadingService } from '../shared/loading.service';
import { PaginationInstance } from 'ngx-pagination';
import { ExternalUser } from '../shared/external-user.model';
import { StatsFilter } from '../shared/stats-filter.model';
import * as moment from 'moment';
import { ExcelService } from '../shared/excel.service';
import { BancoPipe } from '../shared/banco.filter';
import { TipoPagoPipe } from '../shared/tipo-pago.filter';
import { UsadaPipe } from '../shared/usada.filter';
import { MaterializeAction } from 'angular2-materialize';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-deposits-report',
  templateUrl: './deposits-report.component.html',
  styleUrls: ['./deposits-report.component.css']
})
export class DepositsReportComponent implements OnInit {
  public loading = false;
  deposits: Deposit[];
  depositsSum: Deposit[];
  exportDeposits: Deposit[];
  total: number;
  pageSize: number = 10;
  currentPage: number = 1;
  httpSubscription: Subscription;
  exportHttpSubscription: Subscription;
  sumHttpSubscription: Subscription;
  filtro: Deposit;
  filBanco: string;
  filBoleta: string;
  filPersona: string;
  filTipoPago: string;
  filEstado: string;
  @ViewChild('filBancoInput') filBancoInput: ElementRef;
  @ViewChild('filBoletaInput') filBoletaInput: ElementRef;
  @ViewChild('filPersonaInput') filPersonaInput: ElementRef;
  @ViewChild('filTipoPagoInput') filTipoPagoInput: ElementRef;
  @ViewChild('filEstadpInput') filEstadoInput: ElementRef;
  @ViewChild('f') rejectForm: NgForm;
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modalDeposit: Deposit;
  modalIndex: number;
  localSubscription: Subscription;

  constructor(private depositsService: DepositsService,
    private loadingService: LoadingService,
    private excelService: ExcelService,
    private banco: BancoPipe,
    private tipoPago: TipoPagoPipe,
    private usada: UsadaPipe) { }

  ngOnInit() {
    this.filtro = new Deposit;
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, this.currentPage, this.pageSize, this.filtro).subscribe(
      res => {
        this.deposits = res.data;
        
        this.total = res.total;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );

    this.localSubscription = this.depositsService.depositsChanged.subscribe(
      (deposits: Deposit[]) => {
        this.deposits = deposits;
       
        this.closeModalReject();
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onPageChange(page: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, page, this.pageSize, this.filtro).subscribe(
      res => {
        this.deposits = res.data;
        this.total = res.total;
        this.currentPage = page;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  filBancoChanged(filBanco: string) {
    this.filtro.agencia = filBanco;
    this.refreshData();
  }

  filBoletaChanged(filBoleta: string) {
    this.filtro.numero = filBoleta;
    this.refreshData();
  }

  filPersonaChanged(filPersona: string) {
    let persona = new ExternalUser;
    persona.name = filPersona;
    this.filtro.externalUser = persona;
    this.refreshData();
  }

  filTipoPagoChanged(filTipoPago: string) {
    this.filtro.tipoPago = filTipoPago;
    this.refreshData();
  }

  filEstadoChanged(filEstado: number) {
    this.filtro.usada = filEstado;
    this.refreshData();
  }

  onFilBanco() {
    let event = new Event('blur', {});
    this.filBancoInput.nativeElement.dispatchEvent(event);
  }

  onFilBoleta() {
    let event = new Event('blur', {});
    this.filBoletaInput.nativeElement.dispatchEvent(event);
  }

  onFilPersona() {
    let event = new Event('blur', {});
    this.filPersonaInput.nativeElement.dispatchEvent(event);
  }

  onFilTipoPago() {
    let event = new Event('blur', {});
    this.filTipoPagoInput.nativeElement.dispatchEvent(event);
  }

  onFilEstado() {
    let event = new Event('blur', {});
    this.filEstadoInput.nativeElement.dispatchEvent(event);
  }

  refreshData() {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, 1, this.pageSize, this.filtro).subscribe(
      res => {
        this.deposits = res.data;
        this.total = res.total;
        this.currentPage = 1;
        this.depositsService.addDeposits(this.deposits);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  exportData() {
    this.exportDeposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(null, null, null, this.filtro).subscribe(
      
      res => {
        this.exportDeposits = res.data;
       
        const temp = this.exportDeposits.map(el => ({
          "Fecha": moment(el.fechaHora, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Banco": this.banco.transform(el.agencia),
          "Boleta": el.numero,
          "Contraloria": el.resolucion == 'N/A' ? '' : el.resolucion,
          "Nombre": el.externalUser ? el.externalUser.name : '',
          "Identificacion": el.externalUser ? el.externalUser.docId : '',
          "Monto": el.monto,
          "Tipo de pago": this.tipoPago.transform(el.tipoPago),
          "Estado": this.usada.transform(el.usada)
        }));
        this.excelService.export(temp, 'boletas');
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onRevertClicked(deposit: Deposit, i: number) {
    this.modalDeposit = deposit;
    this.modalIndex = i;
    this.modRejectActions.emit({action:"modal", params:['open']});
  }

  closeModalReject() {
    this.modRejectActions.emit({action:"modal",params:['close']});
    this.modalDeposit = new Deposit;
    this.modalIndex = -1;
  }

  rejectDeposit() {
    this.modalDeposit.usada = -1;
    this.modalDeposit.cause = this.rejectForm.value.causa;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.depositsService.updateDepositState(this.modalIndex, this.modalDeposit);
  }
}
