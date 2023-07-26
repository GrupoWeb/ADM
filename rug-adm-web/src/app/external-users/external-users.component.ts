import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { ExternalUser } from '../shared/external-user.model';
import { ExternalUsersService } from '../shared/external-users.service';
import { MaterializeAction } from 'angular2-materialize';
import { Observable, Subscription } from 'rxjs';
import { NgForm } from '@angular/forms';
import { LoadingService } from '../shared/loading.service';
import { Mail } from '../shared/mail.model';
import { MailsService } from '../shared/mails.service';

@Component({
  selector: 'app-external-users',
  templateUrl: './external-users.component.html',
  styleUrls: ['./external-users.component.css']
})
export class ExternalUsersComponent implements OnInit {
  public loading = false;
  users: ExternalUser[];
  asyncUsers: Observable<ExternalUser[]>;
  total: number;
  pageSize: number = 10;
  currentPage: number = 1;
  modalActions = new EventEmitter<string|MaterializeAction>();
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modalUser: ExternalUser;
  modalIndex: number;
  httpSubscription: Subscription;
  localSubscription: Subscription;
  mailsHttpSubscription: Subscription;
  mails: Mail[];
  @ViewChild('f') rejectForm: NgForm;
  /*public config: PaginationInstance = {
    id: 'custom',
    itemsPerPage: 8,
    currentPage: 1
  };*/
  filNombre: string;
  filCorreo: string;
  filDocId: string;
  filNit: string;
  filtro: ExternalUser;
  @ViewChild('filNombreInput') filNombreInput: ElementRef;
  @ViewChild('filCorreoInput') filCorreoInput: ElementRef;
  @ViewChild('filDocIdInput') filDocIdInput: ElementRef;
  @ViewChild('filNitInput') filNitInput: ElementRef;
  viewMessage: boolean = false;
  htmlMail: any;

  constructor(private externalUsersService: ExternalUsersService,
    private mailsService: MailsService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.filtro = new ExternalUser;
    this.externalUsersService.users = [];
    this.mails = [];
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

    this.localSubscription = this.mailsService.mailsChanged.subscribe(
      res => {
        this.mails = res;
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }

  onAproveUserClicked(user: ExternalUser, i: number) {
    user.personaId = this.users[i].personaId;
    user.status = 'IN';
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.externalUsersService.updateUserState(i, user);
  }

  onRejectUserClicked(user: ExternalUser, i: number) {
    this.modalUser = user;
    this.modalIndex = i;
    this.modRejectActions.emit({action:"modal",params:['open']});
  }

  onViewMailsClicked(user: ExternalUser) {
    this.htmlMail = null;
    this.viewMessage = false;
    this.modalUser = user;
    // obtener correos no enviados del usuario
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.mailsService.fetchData(user.personaId, false).subscribe(
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

  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.modalUser = new ExternalUser;
  }

  closeModalReject() {
    this.modRejectActions.emit({action:"modal",params:['close']});
    this.modalUser = new ExternalUser;
    this.modalIndex = -1;
  }

  rejectUser() {
    this.modalUser.status = 'RE';
    this.modalUser.cause = this.rejectForm.value.causa;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.externalUsersService.updateUserState(this.modalIndex, this.modalUser);
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

  onViewMessageClicked(mail: Mail) {
    this.htmlMail = mail.mensaje;
    this.viewMessage = true;
  }

  onBackClicked() {
    this.htmlMail = null;
    this.viewMessage = false;
  }

  forwardMail(index: number, mail: Mail) {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.mailsService.forwardMail(index, mail.idMail);
  }

  reprocessMail(index: number, mail: Mail) {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    mail.idStatusMail = 1;
    this.mailsService.updateMailState(index, mail);
  }
}
