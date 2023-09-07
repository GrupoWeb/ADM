import { Component, EventEmitter, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { LoadingService } from '../shared/loading.service';
import { environment } from '../../environments/environment';
import { MenusService } from '../shared/menus.service';
import { Menu } from '../shared/menus.model'
import { Rol } from '../shared/roles.model';
import { MaterializeAction } from 'angular2-materialize';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-permisos-menu',
  templateUrl: './permisos-menu.component.html',
  styleUrls: ['./permisos-menu.component.css']
})
export class PermisosMenuComponent implements OnInit {
  modalAviso = new EventEmitter<string | MaterializeAction>();
  mensajeModal="";
  isSuccess: boolean;
  tituloModal: String;
  loadingSubscription: Subscription;
  httpSubscription: Subscription;
  total: number;
  ttotal: number;
  currentPage: number = 1;
  menus: Menu[];
  tmenus: Menu[];
  nombreTemp = '';
  filtro: Menu;

  pageSize: number = 10;
  public loading = false;
  filMenus: any[] = [];
  filRoles: any[] = [];

  roles : Rol[]; // Aquí debería estar la lista de roles
  totalr: number;
  selectedMenu: any; // Aquí almacenaremos la selección del menú/submenú
  selectedRole: any; // Aquí almacenaremos la selección del rol

  constructor( private menuService: MenusService,
    private loadingService: LoadingService,) { }

  ngOnInit() {

    this.filtro= new Menu;
    this.loadingSubscription = this.loadingService.loadingChanged.subscribe(
      (isLoading: boolean) => {
        this.loading = isLoading;
      }     
    );
    this.httpSubscription = this.menuService.fetchDataR()
    .pipe(
      finalize(() => {
        this.llenarTabla();
      })
    ).subscribe(
      res => {
        
        this.roles = res.data;
        this.totalr = res.total;
        this.filRoles=this.roles;
        
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
    this.httpSubscription = this.menuService.fetchData(null, null).subscribe(
      res => {
        
        this.menus = res.data;
        this.total = res.total;
        this.filMenus=this.menus;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
    
    
    

  }
  onPageChange(page: number) {
    
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.menuService.fetchData(page, this.pageSize).subscribe(
      res => {
        
        this.tmenus = res.data;
        this.ttotal = res.total;
        //this.filMenus=this.menus;
        this.currentPage = page;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
  }
  llenarTabla(){

    this.httpSubscription = this.menuService.fetchData(this.currentPage, this.pageSize).subscribe(
      res => {
        
        this.tmenus = res.data;
        this.ttotal = res.total;
        this.filMenus=this.menus;
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
  }
  filMenuChanged(filMenu: number) {
    
    this.filtro.menuId = filMenu;
    //this.refreshData();
  }
  filRolChanged(filRoles: string) {
    
    this.filtro.rol = filRoles;
    //this.refreshData();
  }
  asignarRole(){
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.menuService.fetchDataF(this.currentPage, this.pageSize,this.filtro).subscribe(
      res => {
        
        this.tmenus = res.data;
        this.ttotal = res.total;
        this.filMenus=this.menus;
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        this.isSuccess = true;
          this.tituloModal = "Éxito";
          this.mensajeModal="Se asignó permisos exitosamente";
          this.modalAviso.emit({ action: "modal", params: ['open'] });

      },
      err => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        this.isSuccess = false;
        this.tituloModal = "Error";
          this.mensajeModal="Ocurrio un problema al asignar permisos.";
          this.modalAviso.emit({ action: "modal", params: ['open'] });
      }
    ); 
  }
  closeModal(){
     
    this.modalAviso.emit({ action: "modal", params: ['close'] });
    window.location.reload();  
  
  }
  getMenuRolesNames(rol: string) {
    
    if (!rol) {
      return ''; // Si no hay roles, retorna una cadena vacía
    }
  
    const rolesArray = rol.split(','); // Divide los roles por comas
    const roleNames = [];
    
    for (const role of rolesArray) {
      
      this.roles.forEach(r => {
        
        if (r.tipoRol.trim().toLowerCase() === role.trim().toLowerCase()) {
          roleNames.push(r.nomRol);
        }
      });
    }
  
    return roleNames.join(', ');
  }
  
  
}
