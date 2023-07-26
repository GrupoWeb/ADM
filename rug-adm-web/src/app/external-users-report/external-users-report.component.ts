import { Component, OnInit, ViewChild, ElementRef, EventEmitter } from '@angular/core';
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
import { SitUsuarioPipe } from '../shared/sit-usuario.filter';
import { Mail } from '../shared/mail.model';
import { MaterializeAction } from 'angular2-materialize';
import { MailsService } from '../shared/mails.service';

@Component({
  selector: 'app-external-users-report',
  templateUrl: './external-users-report.component.html',
  styleUrls: ['./external-users-report.component.css']
})
export class ExternalUsersReportComponent implements OnInit {
  public loading = false;
  users: ExternalUser[];
  exportUsers: ExternalUser[];
  total: number;
  pageSize: number = 10;
  currentPage: number = 1;
  httpSubscription: Subscription;
  exportHttpSubscription: Subscription;
  localSubscription: Subscription;
  filtro: ExternalUser;
  filNombre: string;
  filCorreo: string;
  filDocId: string;
  filNit: string;
  filStatus: string;
  @ViewChild('filNombreInput') filNombreInput: ElementRef;
  @ViewChild('filCorreoInput') filCorreoInput: ElementRef;
  @ViewChild('filDocIdInput') filDocIdInput: ElementRef;
  @ViewChild('filNitInput') filNitInput: ElementRef;
  @ViewChild('filStatusInput') filStatusInput: ElementRef;
  viewMessage: boolean = false;
  htmlMail: any;
  modalUser: ExternalUser;
  mails: Mail[];
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalAnswerActions = new EventEmitter<string|MaterializeAction>();
  modalSaldoActions = new EventEmitter<string|MaterializeAction>();
  saldo: number;

  constructor(private externalUsersService: ExternalUsersService,
    private loadingService: LoadingService,
    private excelService: ExcelService,
    private mailsService: MailsService,
    private sitUsuario: SitUsuarioPipe) { }

  ngOnInit() {
    this.filtro = new ExternalUser;
    this.externalUsersService.users = [];
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

    this.localSubscription = this.externalUsersService.usersChanged.subscribe(
      (users: ExternalUser[]) => {
        this.users = users;
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

  filStatusChanged(filStatus: string) {
    this.filtro.status = filStatus;
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

  onViewMailsClicked(user: ExternalUser) {
    this.htmlMail = null;
    this.viewMessage = false;
    this.modalUser = user;
    // obtener correos no enviados del usuario
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.mailsService.fetchData(user.personaId, true).subscribe(
      res => {
        this.mails = res;
        this.mailsService.mails = [];
        this.mailsService.addMails(this.mails);
        this.modalActions.emit({action:"modal",params:['open']});
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onViewMessageClicked(mail: Mail) {
    this.htmlMail = mail.mensaje;
    this.viewMessage = true;
  }

  onBackClicked() {
    this.htmlMail = null;
    this.viewMessage = false;
  }

  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.modalUser = new ExternalUser;
  }

  onViewAnswerClicked(user: ExternalUser) {
    this.modalUser = user;
    this.modalAnswerActions.emit({action:"modal",params:['open']});
  }

  closeModalAnswer() {
    this.modalAnswerActions.emit({action:"modal",params:['close']});
    this.modalUser = new ExternalUser;
  }

  onViewSaldoClicked(user: ExternalUser) {
    this.modalUser = user;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchSaldoData(user.personaId).subscribe(
      res => {
        this.saldo = res as number;
        this.modalSaldoActions.emit({action:"modal",params:['open']});
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  closeModalSaldo() {
    this.modalSaldoActions.emit({action:"modal",params:['close']});
    this.modalUser = new ExternalUser;
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

  exportData() {
    this.exportUsers = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.exportHttpSubscription = this.externalUsersService.fetchData("ALLNM", null, null, this.filtro).subscribe(
      res => {
        this.exportUsers = res.data;
        const temp = this.exportUsers.map(el => ({
          "Nombre": el.name,
          "Correo": el.email,
          "Documento de Identificación": el.docId,
          "NIT": el.nit,
          "Fecha de Registro": moment(el.registered, 'YYYY-MM-DD HH:mm:ss').format('DD/MM/YYYY HH:mm:ss'),
          "Registro del RGM": el.registryCode,
          "Estado": this.sitUsuario.transform(el.status)
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

  onRemoveUserClicked(user: ExternalUser, i: number) {
    user.personaId = this.users[i].personaId;
    user.status = 'CA';
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.externalUsersService.updateUserState(i, user);
  }
}
