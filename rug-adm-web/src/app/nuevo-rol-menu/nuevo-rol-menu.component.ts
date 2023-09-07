import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { LoadingService } from '../shared/loading.service';
import { environment } from '../../environments/environment';
import { MenusService } from '../shared/menus.service';
import { Menu } from '../shared/menus.model'
import { Rol } from '../shared/roles.model';
import { MaterializeAction } from 'angular2-materialize';

@Component({
  selector: 'app-nuevo-rol-menu',
  templateUrl: './nuevo-rol-menu.component.html',
  styleUrls: ['./nuevo-rol-menu.component.css']
})
export class NuevoRolMenuComponent implements OnInit {
  @ViewChild('delselect') selectElement: ElementRef;
  @ViewChild('menuselect') menuselect: ElementRef;
  @ViewChild('rolselect') rolselect: ElementRef;
  @ViewChild('delrolselect') delrolElement: ElementRef;
  modalAviso = new EventEmitter<string | MaterializeAction>();
  modalAviso2 = new EventEmitter<string | MaterializeAction>();
  loadingSubscription: Subscription;
  httpSubscription: Subscription;
  total: number;
  ttotal: number;
  currentPage: number = 1;
  menus: Menu[];
  tmenus: Menu[];
  nombreTemp = '';
  filtro: Menu;
  filtror:Rol;
  OtroB: Boolean;
  pageSize: number = 10;
  public loading = false;
  filMenus: any[] = [];
  filRoles: any[] = [];
  roles : Rol[]; // Aquí debería estar la lista de roles
  totalr: number;
  selectedMenu: any; // Aquí almacenaremos la selección del menú/submenú
  selectedRole: any; 
  OtroMenu:string;
  nomSunMenu:string;
  linkSunMenu:string;
  nomRol:string;
  LetraRol:string;
  mensajeModal="";
  isSuccess: boolean;
  tituloModal: String;

  constructor(private menuService: MenusService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.filtro= new Menu;
    this.filtror= new Rol;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
this.OtroB = false;
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
    this.httpSubscription = this.menuService.fetchDataR().subscribe(
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
    
  }
  filMenuChanged(filMenu: string) {
    
    this.filtro.nomMenu = filMenu;
    if(filMenu=="otro"){
      this.OtroB=true;
    }else{
      this.OtroB=false;
    }
    
    //this.refreshData();
  }
  delMenuChanged(delMenu: any) {
    console.log(this.filMenus);
    const selectedIndex = this.selectElement.nativeElement.selectedIndex;
    const selectedOption = this.selectElement.nativeElement.options[selectedIndex];
    const selectedText = selectedOption.text;

    console.log('Texto seleccionado:', selectedText);

    
    if(selectedText.includes("Cabecera de")){
      this.mensajeModal="La selección es una cabecera de menú, se eliminaran todos los submenús de este.Esto acción no se puede revertir.";
      this.tituloModal="Advertencia";
      this.isSuccess=false;
      this.modalAviso2.emit({ action: "modal", params: ['open'] });
    }
    this.filtro.nomMenu = delMenu;
    
    
    //this.refreshData();
  }
  delRolChanged(delMenu: any) {
    this.filtro.rol = delMenu;
  }
  filRolChanged(filRoles: string) {
    
    this.filtro.rol = filRoles;

    const selectedIndex2 = this.rolselect.nativeElement.selectedIndex;
    const selectedOption2 = this.rolselect.nativeElement.options[selectedIndex2];
    const selectedText2 = selectedOption2.text;
    
    console.log(selectedText2);
    //this.refreshData();
  }
  crearMenu(){
    const selectedValuemenu = this.menuselect.nativeElement.value;
    
    if(selectedValuemenu){
      const selectedValueRol = this.rolselect.nativeElement.value;
      if(selectedValueRol){
        if(this.nomSunMenu){
          if(this.linkSunMenu){
            console.log("TODO bien");
            this.loading = true;
            this.loadingService.changeLoading(this.loading);
            if(this.filtro.nomMenu=="otro"){
              this.filtro.nomMenu=this.OtroMenu;
            }
            this.filtro.nomSubMenu=this.nomSunMenu;
            this.filtro.link=this.linkSunMenu;

            
            this.httpSubscription = this.menuService.fetchDataC(this.filtro).subscribe(
              res => {
                
                if(res.total==50)  
                {
                  this.mensajeModal="El menú/Submenú fue creado.";
                  this.tituloModal="Exito";
                  this.isSuccess=true;
                  this.modalAviso.emit({ action: "modal", params: ['open'] });
                }else{
                  this.mensajeModal="El menú/Submenú no fue creado.";
                  this.tituloModal="Error";
                  this.isSuccess=false;
                  this.modalAviso.emit({ action: "modal", params: ['open'] });
                }
                this.loading = false;
                this.loadingService.changeLoading(this.loading); 
              },
              err => console.error(err),
              () => {
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
              }
            ); 
          }
          else{
            this.mensajeModal="Debe ingresar el link.";
            this.tituloModal="Error";
            this.isSuccess=false;
            this.modalAviso2.emit({ action: "modal", params: ['open'] });
          }
          
        }else{
          this.mensajeModal="Debe ingresar el nombre del submenu";
          this.tituloModal="Error";
          this.isSuccess=false;
          this.modalAviso2.emit({ action: "modal", params: ['open'] });
        }
        

      }else{
        this.mensajeModal="Debe seleccionar un rol.";
        this.tituloModal="Error";
        this.isSuccess=false;
        this.modalAviso2.emit({ action: "modal", params: ['open'] });
      }
    }else{
      this.mensajeModal="Debe seleccionar un menú.";
      this.tituloModal="Error";
      this.isSuccess=false;
      this.modalAviso2.emit({ action: "modal", params: ['open'] });
    }
    

    
  }

  crearRol(){
    
    if(this.nomRol)
    {
      if(this.LetraRol)
      {
        this.loading = true;
    this.loadingService.changeLoading(this.loading);
  
    this.filtror.nomRol=this.nomRol;
    this.filtror.tipoRol=this.LetraRol;

    
    this.httpSubscription = this.menuService.fetchDataCR(this.filtror).subscribe(
      res => {
         
        if(res.total==50)  
        {
          this.mensajeModal="El rol fue creado.";
          this.tituloModal="Exito";
          this.isSuccess=true;
          this.modalAviso.emit({ action: "modal", params: ['open'] });
        }else{
          this.mensajeModal="El rol no fue creado.";
          this.tituloModal="Error";
          this.isSuccess=false;
          this.modalAviso.emit({ action: "modal", params: ['open'] });
        }
        this.loading = false;
        this.loadingService.changeLoading(this.loading); 
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
      }
      else{
        this.mensajeModal="Ingrese la letra que se asignará al rol. Verifique las ya asignadas para evitar conflictos. ";
        this.tituloModal="Error";
        this.isSuccess=false;
        this.modalAviso2.emit({ action: "modal", params: ['open'] });
      }
    }
    else{
      this.mensajeModal="Ingrese el nombre del rol.";
      this.tituloModal="Error";
      this.isSuccess=false;
      this.modalAviso2.emit({ action: "modal", params: ['open'] });
    }
    
    
  }
  eliminarMenu(){
    this.filtro= new Menu;
    const selectedIndex = this.selectElement.nativeElement.value;
    if(selectedIndex){
      const selectedIndex2 = this.selectElement.nativeElement.selectedIndex;
      const selectedOption = this.selectElement.nativeElement.options[selectedIndex2];
      const selectedValue = selectedOption.value;
      const selectedText = selectedOption.text;
      if(selectedText.includes("Cabecera de")){
        this.filtro.escabecera=1;
        this.filtro.nomMenu = selectedText;
      }else{
        this.filtro.escabecera=2;
        this.filtro.nomSubMenu = selectedText;
      }
      
      this.filtro.menuId = selectedValue;
      
      this.httpSubscription = this.menuService.fetchDataMD(this.filtro).subscribe(
        res => {
          
          if(res.total==50)  
          {
            this.mensajeModal="El menú/Submenú fue eliminado.";
            this.tituloModal="Exito";
            this.isSuccess=true;
            this.modalAviso.emit({ action: "modal", params: ['open'] });
          }else{
            this.mensajeModal="El menú/Submenú no fue eliminado.";
            this.tituloModal="Error";
            this.isSuccess=false;
            this.modalAviso.emit({ action: "modal", params: ['open'] });
          }
          this.loading = false;
          this.loadingService.changeLoading(this.loading); 
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      ); 

    }else{
      this.mensajeModal="Debe seleccionar un menú o submenú a eliminar.";
      this.tituloModal="Error";
      this.isSuccess=false;
      this.modalAviso2.emit({ action: "modal", params: ['open'] });
    }
  }
  eliminarRol(){
    this.filtror= new Rol;
    const selectedIndex = this.delrolElement.nativeElement.value;
    if(selectedIndex){
      const selectedIndex2 = this.delrolElement.nativeElement.selectedIndex;
      const selectedOption = this.delrolElement.nativeElement.options[selectedIndex2];
      const selectedValue = selectedOption.value;
      const selectedText = selectedOption.text;
      
      
      this.filtror.nomRol = selectedText;
      this.filtror.rolId = selectedValue;
      
      
     this.httpSubscription = this.menuService.fetchDataRD(this.filtror).subscribe(
        res => {
          
          if(res.total==50)  
          {
            this.mensajeModal="El rol fue eliminado.";
            this.tituloModal="Exito";
            this.isSuccess=true;
            this.modalAviso.emit({ action: "modal", params: ['open'] });
          }else{
            this.mensajeModal="El rol no fue eliminado.";
            this.tituloModal="Error";
            this.isSuccess=false;
            this.modalAviso.emit({ action: "modal", params: ['open'] });
          }
          this.loading = false;
          this.loadingService.changeLoading(this.loading); 
        },
        err => console.error(err),
        () => {
          this.loading = false;
          this.loadingService.changeLoading(this.loading);
        }
      ); 

    }else{
      this.mensajeModal="Debe seleccionar rol a eliminar.";
      this.tituloModal="Error";
      this.isSuccess=false;
      this.modalAviso2.emit({ action: "modal", params: ['open'] });
    }
  }
  closeModal(){
     
    this.modalAviso.emit({ action: "modal", params: ['close'] });
    window.location.reload();
    
  
  }
  closeModal2(){
     
    this.modalAviso2.emit({ action: "modal", params: ['close'] });
    
    
  
  }
}
