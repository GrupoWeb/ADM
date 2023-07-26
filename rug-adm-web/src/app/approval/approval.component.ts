import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { ExternalUser } from '../shared/external-user.model';
import { ExternalUsersService } from '../shared/external-users.service';
import { MaterializeAction } from 'angular2-materialize';
import { Subscription } from 'rxjs/Subscription';
import { NgForm } from '@angular/forms';
import { LoadingService } from '../shared/loading.service';

@Component({
  selector: 'app-approval',
  templateUrl: './approval.component.html',
  styleUrls: ['./approval.component.css']
})
export class ApprovalComponent implements OnInit {
  public loading = false;
  users: ExternalUser[];
  total: number;
  pageSize: number = 5;
  currentPage: number = 1;
  modalActions = new EventEmitter<string|MaterializeAction>();
  modRejectActions = new EventEmitter<string|MaterializeAction>();
  modalUser: ExternalUser;
  modalIndex: number;
  httpSubscription: Subscription;
  localSubscription: Subscription;
  @ViewChild('f') rejectForm: NgForm;
  filtro: ExternalUser;
  filNombre: string;
  filCorreo: string;
  filDocId: string;
  filNit: string;
  @ViewChild('filNombreInput') filNombreInput: ElementRef;
  @ViewChild('filCorreoInput') filCorreoInput: ElementRef;
  @ViewChild('filDocIdInput') filDocIdInput: ElementRef;
  @ViewChild('filNitInput') filNitInput: ElementRef;

  constructor(private externalUsersService: ExternalUsersService,
    private loadingService: LoadingService) { }

  ngOnInit() {

    

    this.filtro = new ExternalUser;
    this.externalUsersService.users = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.externalUsersService.fetchData("PA", this.currentPage, this.pageSize, this.filtro).subscribe(
      
      res => {
        this.users = res.data;
        this.total = res.total;
        this.externalUsersService.addUsers(this.users);

        // console.log(res.data.map( respuesta => {
        //   return respuesta
        // }));

        console.log(res.data[0]['docId']);
        
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
        this.closeModal();
        this.closeModalReject();
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

  onViewAttachmentClicked(user: ExternalUser) {
    this.modalUser = user;
    this.modalActions.emit({action:"modal",params:['open']});
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
    this.httpSubscription = this.externalUsersService.fetchData("PA", page, this.pageSize, this.filtro).subscribe(
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
    this.httpSubscription = this.externalUsersService.fetchData("PA", 1, this.pageSize, this.filtro).subscribe(
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
}
