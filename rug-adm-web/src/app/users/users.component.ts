import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { User } from '../shared/user.model';
import { UsersService } from '../shared/users.service';
import { MaterializeAction } from 'angular2-materialize';
import { Subscription } from 'rxjs/Subscription';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { LoadingService } from '../shared/loading.service';
import { Rol } from '../shared/roles.model';
import { MenusService } from '../shared/menus.service';

declare var Materialize:any;

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  public loading = false;
  userForm: FormGroup;
  users: User[];
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalActions_p = new EventEmitter<string|MaterializeAction>();
  modalActions_ac= new EventEmitter<string|MaterializeAction>();
  @ViewChild('btnSi') btnSiElement: ElementRef;
  modalUser: User;
  httpSubscription: Subscription;
  localSubscription: Subscription;
  editMode = false;
  editIndex: number;
  editId: number;
  roles : Rol[];
  filRoles: any[] = [];
  mensaje_titulo = "";
  mensaje_modal1 = "";
  mensaje_modal2 = "";
  logo_modal = "";
  logo_modal_color = "";
  constructor(private usersService: UsersService,
    private loadingService: LoadingService,
    private menuService: MenusService) { }

  ngOnInit() {
    this.usersService.users = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.usersService.fetchData().subscribe(
      data => {
        this.users = data;
        this.usersService.addUsers(this.users);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );

    this.localSubscription = this.usersService.usersChanged.subscribe(
      (users: User[]) => {
        this.users = users;
        this.onCancelClicked();
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.httpSubscription = this.menuService.fetchDataR().subscribe(
      res => {
        
        this.roles = res.data;
        //this.totalr = res.total;
        this.filRoles=this.roles;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
    this.initForm();
  }

  private initForm() {
    this.userForm = new FormGroup({
      'nombre': new FormControl('', Validators.required),
      'email': new FormControl('', [Validators.required, Validators.email]),
      'rol': new FormControl('', Validators.required),
      'password1': new FormControl('', Validators.required),
      'password2': new FormControl('', Validators.required)
    });
  }

  onAddUserClicked() {
    this.modalUser = new User;
    this.editMode = false;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onEditUserClicked(user: User, index: number) {
    this.modalUser = user;
    this.editMode = true;
    this.editIndex = index;
    this.editId = user.usuarioId;
    this.userForm.patchValue({
      nombre: user.nombre,
      email: user.email,
      rol: user.rol
    });
    Materialize.updateTextFields();
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onRemoveUserClicked(index: number) {
    let user: User = new User;
    user.usuarioId = this.users[index].usuarioId;
    user.estado = 'I';
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.usersService.updateUser(index, user);
  }

  onSubmit() {
    
    let user: User = new User;
    user.nombre = this.userForm.value.nombre;
    user.email = this.userForm.value.email;
    user.rol = this.userForm.value.rol;
    var pas1 = this.userForm.value.password1;
    var pas2 = this.userForm.value.password2;
    if(pas1!=null)
    {
      if(pas1==pas2){
        user.password = this.userForm.value.password1      
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        if (this.editMode) {
          user.usuarioId = this.editId;
          this.usersService.updateUser(this.editIndex, user);
        } else {

          this.usersService.saveData(user).subscribe(
            (response) => {
              let savedUser: User = response;
              this.usersService.users.push(savedUser);
              this.usersService.usersChanged.next(this.usersService.getUsers());
              this.mensaje_titulo ="Usuario";
              this.mensaje_modal1 = "El usuario se creo con exito..";
              this.logo_modal = "check_circle";
              this.logo_modal_color  = "green";
              this.modalActions_p.emit({action:"modal", params:["open"]});
            },
            err => {
              this.loading = false;              
              this.loadingService.changeLoading(this.loading);
              this.mensaje_titulo = "Alerta";
              const partes = err.error.split("-");
              const parte1 = partes[0];
              const parte2 = partes[1];
              if(parte2=="A"){
                this.mensaje_modal1 = parte1;
                this.mensaje_modal2 = "No se puede utilizar este correo.";
                this.btnSiElement.nativeElement.disabled = true;
              }else if(parte2=="I"){
                this.mensaje_modal1 = "El correo esta inactivo.";
                this.mensaje_modal2 = "¿Desea reactivar este correo y asignarle los nuevos  datos.?";
                this.btnSiElement.nativeElement.disabled = false;
              }

              
              
              this.modalActions_ac.emit({action:"modal", params:["open"]});      
              console.log(err);
            }
          );
                 }
      }
      else{
        this.mensaje_titulo ="Contraseñas";
        this.mensaje_modal1 = "Las contraseñas no coinciden.";
        this.logo_modal = "warning";
        this.logo_modal_color  = "red";
        this.modalActions_p.emit({action:"modal", params:["open"]});      
      }
    }
    else
    {
      this.loading = true;
      this.loadingService.changeLoading(this.loading);
      if (this.editMode) {
        user.usuarioId = this.editId;
        this.usersService.updateUser(this.editIndex, user);
      } else {
        this.usersService.addUser(user);
      }
    }   
  }
  reactivar(){
    let user: User = new User;
    user.nombre = this.userForm.value.nombre;
    user.email = this.userForm.value.email;
    user.rol = this.userForm.value.rol;
    user.password = this.userForm.value.password1;
    user.estado  = 'A';
    //this.usersService.updateUser(this.editIndex, user);
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.usersService.reactivData(user).subscribe(
      (response) => {
        console.log(response);
        this.mensaje_titulo ="Usuario";
        this.mensaje_modal1 = "El usuario se reactivo con exito..";
        this.logo_modal = "check_circle";
        this.logo_modal_color  = "green";
        this.modalActions_p.emit({action:"modal", params:["open"]});

        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        this.onCancel_Clicked();
        this.onCancelClicked();
        this.httpSubscription = this.usersService.fetchData().subscribe(
          data => {
            this.users = data;
            this.usersService.addUsers(this.users);
          },
          err => console.error(err),
          () => {
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          }
        );
      });
  }
  onCancelClicked() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.userForm.reset();
    this.editMode = false;
    this.editIndex = -1;
  }
  onCancel_Clicked() {
    this.modalActions_ac.emit({action:"modal",params:['close']});
    //this.userForm.reset();
    this.editMode = false;
    this.editIndex = -1;
  }
  onCancel_Clicked_() {
    this.modalActions_p.emit({action:"modal",params:['close']});
   
  }
  
}
