import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { Deposit } from '../shared/deposit.model';
import { DepositsService } from '../shared/deposits.service';
import { MaterializeAction } from 'angular2-materialize';
import { Subscription } from 'rxjs/Subscription';
import { LoadingService } from '../shared/loading.service';
import { NgForm } from '@angular/forms';
import { ExternalUser } from '../shared/external-user.model';

@Component({
  selector: 'app-deposits',
  templateUrl: './deposits.component.html',
  styleUrls: ['./deposits.component.css']
})
export class DepositsComponent implements OnInit {
  public loading = false;
  deposits: Deposit[];
  total: number;
  pageSize: number = 5;
  currentPage: number = 1;
  modalActions = new EventEmitter<string|MaterializeAction>();
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modalDeposit: Deposit;
  modalIndex: number;
  httpSubscription: Subscription;
  localSubscription: Subscription;
  @ViewChild('f') rejectForm: NgForm;
  filtro: Deposit;
  filBanco: string;
  filBoleta: string;
  filPersona: string;
  filTipoPago: string;
  @ViewChild('filBancoInput') filBancoInput: ElementRef;
  @ViewChild('filBoletaInput') filBoletaInput: ElementRef;
  @ViewChild('filPersonaInput') filPersonaInput: ElementRef;
  @ViewChild('filTipoPagoInput') filTipoPagoInput: ElementRef;

  constructor(private depositsService: DepositsService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.filtro = new Deposit;
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(0, this.currentPage, this.pageSize, this.filtro).subscribe(
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
        this.closeModal();
        this.closeModalReject();
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onAproveDepositClicked(deposit: Deposit, i: number) {
    deposit.id = this.deposits[i].id;
    deposit.usada = 1;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.depositsService.updateDepositState(i, deposit);
  }

  onRejectDepositClicked(deposit: Deposit, i: number) {
    /*deposit.id = this.deposits[i].id;
    deposit.usada = -1;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.depositsService.updateDepositState(i, deposit);*/
    this.modalDeposit = deposit;
    this.modalIndex = i;
    this.modRejectActions.emit({action:'modal', params:['open']});
  }

  onViewAttachmentClicked(deposit: Deposit) {
    this.modalDeposit = deposit;
    this.modalActions.emit({action:"modal",params:['open']});
  }

  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
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

  onPageChange(page: number) {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(0, page, this.pageSize, this.filtro).subscribe(
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

  refreshData() {
    this.depositsService.deposits = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.depositsService.fetchData(0, 1, this.pageSize, this.filtro).subscribe(
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
}
