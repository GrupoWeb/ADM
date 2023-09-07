import { Component, OnInit, EventEmitter } from '@angular/core';
import { UsersService } from '../shared/users.service';
import { Router, ActivatedRoute } from '@angular/router';
import { User } from '../shared/user.model';
import { Subscription } from 'rxjs/Subscription';
import { LoadingService } from '../shared/loading.service';
import { DomSanitizer } from '@angular/platform-browser';
import { environment } from '../../environments/environment';
import { MenusService } from '../shared/menus.service';
import { Menu } from '../shared/menus.model'
@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {

  /*******************  env dashboard */
  id:string;
  url:string;
  secureUrl: any;
  panels: any[];

  /*********************************** */
  public loading = false;
  public user: User;
  sidenavActions = new EventEmitter<any>();
  sidenavParams = [{edge:'left', closeOnClick:true}];
  loadingSubscription: Subscription;
  httpSubscription: Subscription;
  total: number;
  menus: Menu[];
  nombreTemp = '';
  pageSize = 10;

  constructor(
              private usersService: UsersService,
              private loadingService: LoadingService,
              private sanitizer: DomSanitizer, //add dashboard
              private route_dash: ActivatedRoute,
              private router: Router,
              private menuService: MenusService){ }

  ngOnInit() {

    /*************DASHBOAD INITIAL ****** */

    this.initPanels();

    // this.route_dash.params.subscribe(
    //   params  =>  {
    //     this.id = params['id'];
    //     if(this.id){
    //       const panel = this.panels.filter(panel  =>  panel.link === this.id)[0];
    //       if(panel){
    //         this.url = environment.pentaho_url + panel.url;
    //         console.log("Variable", this.url);
    //       }
    //       this.secureUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.url);
    //     }else{
    //       this.secureUrl = null;
    //     }
    //   }
    // );
    /************************************ */

    this.user = this.usersService.authenticatedUser;

    this.loadingSubscription = this.loadingService.loadingChanged.subscribe(
      (isLoading: boolean) => {
        this.loading = isLoading;
      }

      
    );
    this.httpSubscription = this.menuService.fetchData(null,null).subscribe(
      res => {
        console.log(res.data);
        this.menus = res.data;
        this.total = res.total;
        
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    ); 
  }

  initPanels(){
    this.panels = [
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Tramites',
        link: 'tra',
        url: '/%3Ahome%3ATramite.wcdf/generatedContent?userid=jjolon&password=jjolon' 
        // url: '/api/repos/%3Ahome%3Ajjolon%3ATramite.wcdf/generatedContent?userid=jjolon&password=jjolon' 
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Personas',
        link: 'per',
        url: '/api/repos/%3Ahome%3APersonas.wcdf/generatedContent?userid=jjolon&password=jjolon'
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Pagos',
        link: 'pag',
        url: '/api/repos/%3Ahome%3Apagos.wcdf/generatedContent?userid=jjolon&password=jjolon'
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Tipo bien especial',
        link: 'tb',
        // url: '/api/repos/%3Ahome%3ATipoBien.wcdf/generatedContent?userid=jjolon&password=jjolon'
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Tipo persona',
        link: 'tp',
        url: '/api/repos/%3Ahome%3ATipoPersona.wcdf/generatedContent?userid=jjolon&password=jjolon'
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Monto garantizado',
        link: 'mg',
        url: '/api/repos/%3Ahome%3AMontoGarantizado.wcdf/generatedContent?userid=jjolon&password=jjolon'
      },
      {
        img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
        title: 'Bienes en garantia',
        link: 'bg',
        url: '/api/repos/%3Ahome%3AGarantiaBienes.wcdf/generatedContent?userid=jjolon&password=jjolon'
      }
    ];
  }
  // initPanels(){
  //   this.panels = [
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Tramites',
  //       link: 'tra',
  //       url: '/api/repos/%3Ahome%3A20190710-tramites.wcdf/generatedContent?userid=Admin&password=password' 
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Personas',
  //       link: 'per',
  //       url: '/api/repos/%3Ahome%3A20190710-personas.wcdf/generatedContent?userid=Admin&password=password'
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Pagos',
  //       link: 'pag',
  //       url: '/api/repos/%3Ahome%3A20190710-pagos.wcdf/generatedContent?userid=Admin&password=password'
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Tipo bien especial',
  //       link: 'tb',
  //       url: '/api/repos/%3Ahome%3A20190710-tipo-bien.wcdf/generatedContent?userid=Admin&password=password'
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Tipo persona',
  //       link: 'tp',
  //       url: '/api/repos/%3Ahome%3A20190710-tipo-persona.wcdf/generatedContent?userid=Admin&password=password'
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Monto garantizado',
  //       link: 'mg',
  //       url: '/api/repos/%3Ahome%3A20190808-monto-garantizado.wcdf/generatedContent?userid=Admin&password=password'
  //     },
  //     {
  //       img: '/rug-adm/assets/img/analytics-blur-chart-590020.jpg',
  //       title: 'Bienes en garantia',
  //       link: 'bg',
  //       url: '/api/repos/%3Ahome%3A20190710-bienes-garantia.wcdf/generatedContent?userid=Admin&password=password'
  //     }
  //   ];
  // }
  onLogout() {
    this.usersService.logout().subscribe(
      data => {
        this.closeSession();
      },
      err => {
        // aunque el token no sea valido se cierra la sesion en el cliente
        this.closeSession();
      }
    );
  }

  closeSession() {
    this.usersService.authenticatedUser = null;

    localStorage.removeItem('currentUser');

    this.router.navigate(['/login']);
  }

  public showSidenav(): void {
    //this.sidenavParams = ['show'];
    this.sidenavActions.emit('sideNav');
  }

  public hideSidenav(): void {
    //this.sidenavParams = ['hide'];
    this.sidenavActions.emit('sideNav');
  }
}
