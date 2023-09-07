import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { MaterializeModule } from 'angular2-materialize';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { SweetAlert2Module } from '@toverux/ngx-sweetalert2';
import { LoadingModule, ANIMATION_TYPES } from 'ngx-loading';
import { NgxPaginationModule } from 'ngx-pagination';
import { NgxDateRangePickerModule } from 'ngx-daterangepicker';
import { CalendarModule } from 'primeng/calendar';
import { FileUploadModule } from 'ng2-file-upload';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { UsersComponent } from './users/users.component';
import { DepositsComponent } from './deposits/deposits.component';
import { AppRoutingModule } from "./app.routing";
import { LayoutComponent } from './layout/layout.component';
import { AuthGuard } from './shared/auth.guard';
import { ExternalUsersComponent } from './external-users/external-users.component';
import { UsersService } from './shared/users.service';
import { ApprovalComponent } from './approval/approval.component';
import { ExternalUsersService } from './shared/external-users.service';
import { GuaranteesReportComponent } from './guarantees-report/guarantees-report.component';
import { ExternalUsersReportComponent } from './external-users-report/external-users-report.component';
import { LoadingService } from './shared/loading.service';
import { MailsComponent } from './mails/mails.component';
import { TipoPagoPipe } from './shared/tipo-pago.filter';
import { UsadaPipe } from './shared/usada.filter';
import { SitUsuarioPipe } from './shared/sit-usuario.filter';
import { BancoPipe } from './shared/banco.filter';
import { TipoPersonaPipe } from './shared/tipo-persona.filter';
import { TruncatePipe } from './shared/truncate.filter';
import { DepositsReportComponent } from './deposits-report/deposits-report.component';
import { DepositsService } from './shared/deposits.service';
import { GuaranteesService } from './shared/guarantees.service';
import { MailsService } from './shared/mails.service';
import { ExcelService } from './shared/excel.service';
import { TokenInterceptor } from './shared/token.interceptor';
import { ExternalUsersStatsComponent } from './external-users-stats/external-users-stats.component';
import { GuaranteesStatsComponent } from './guarantees-stats/guarantees-stats.component';
import { DepositsStatsComponent } from './deposits-stats/deposits-stats.component';
import { PartePipe } from './shared/parte.filter';
import { VinculacionComponent } from './vinculacion/vinculacion.component';
import { SerializePipe } from './shared/serialize.filter';
import { ExternalUsersSearchComponent } from './external-users-search/external-users-search.component';
import { ModificarMigradoComponent } from './modificar-migrado/modificar-migrado.component';
import { QueryReportComponent } from './query-report/query-report.component';
import { VinculacionesReportComponent } from './vinculaciones-report/vinculaciones-report.component';
import { ModificarCorreoComponent } from './modificar-correo/modificar-correo.component';
import { IncidentsComponent } from "./incidents/incidents.component";
import { IncidentsService } from "./shared/incidents.service";
import { PrioridadPipe} from "./shared/prioridad.filter";
import { EstadoPipe } from "./shared/estado.filter";
import { ImpactoPipe } from "./shared/impacto.filter";
import { ModoIngresoPipe } from "./shared/modo-ingreso.filter";
import { CategoriaPipe } from "./shared/categoria.filter";
import { TipoSolicitudPipe } from "./shared/tipo-solicitud.filter";
import { ChangesComponent } from "./changes/changes.component"
import { ChangeService } from "./shared/change.service";
import { CorreccionesComponent } from './correcciones/correcciones.component';
import { InscripcionComponent } from './inscripcion/inscripcion.component';
import { ReportsDashboardComponent } from './reports-dashboard/reports-dashboard.component'
import { PentahoService } from './reports-dashboard/pentaho.service';
import { CategoriasComponent } from './categorias/categorias.component';
import { TerminosComponent } from './terminos/terminos.component';
import { CategoriesService } from './shared/categories.service';
import { TermsService } from './shared/terms.service';
import { CountriesService } from './shared/countries.service';
import { AssetSearchComponent } from './asset-search/asset-search.component'
import { SurveyService } from './shared/survey.service';
import { SurveyStatsComponent } from './survey-stats/survey-stats.component';
import { FirmaComponent } from './firma/firma.component';
import { ReinicioSaldoComponent } from './reinicio-saldo/reinicio-saldo.component';
import { LoadingComponent } from './loading/loading.component';
import { ReporteTramiteComponent } from './reporte-tramite/reporte-tramite.component';
import { UserTramites } from './shared/user-tramites';
import { ReportDeudoresComponent } from './report-deudores/report-deudores.component';
import { ReportAcreedoresComponent } from './report-acreedores/report-acreedores.component';
import { ReportUsuariosComponent } from './report-usuarios/report-usuarios.component';
import { DeudoresService } from './shared/deudores.service';
import { AcreedoresService } from './shared/acreedores.service';
import { UsuariosService } from './shared/usuarios.service';
import { GarantiasService } from './shared/garantias.service';
import { SubirImagenComponent } from './subir-imagen/subir-imagen.component';
import { ImagenService } from './shared/imagen.service';
import { MenusService } from './shared/menus.service';
import { PermisosMenuComponent } from './permisos-menu/permisos-menu.component';
import { NuevoRolMenuComponent } from './nuevo-rol-menu/nuevo-rol-menu.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UsersComponent,
    DepositsComponent,
    LayoutComponent,
    ExternalUsersComponent,
    ApprovalComponent,
    GuaranteesReportComponent,
    ExternalUsersReportComponent,
    MailsComponent,
    TipoPagoPipe,
    UsadaPipe,
    SitUsuarioPipe,
    BancoPipe,
    TipoPersonaPipe,
    TruncatePipe,
    PartePipe,
    SerializePipe,
    DepositsReportComponent,
    ExternalUsersStatsComponent,
    GuaranteesStatsComponent,
    DepositsStatsComponent,
    VinculacionComponent,
    ExternalUsersSearchComponent,
    ModificarMigradoComponent,
    QueryReportComponent,
    VinculacionesReportComponent,
    ModificarCorreoComponent,
    IncidentsComponent,
    PrioridadPipe,
    EstadoPipe,
    ImpactoPipe,
    ModoIngresoPipe,
    CategoriaPipe,
    TipoSolicitudPipe,
    ChangesComponent,
    CorreccionesComponent,
    InscripcionComponent,
    ReportsDashboardComponent,
    CategoriasComponent,
    TerminosComponent,
    AssetSearchComponent,
    SurveyStatsComponent,
    FirmaComponent,
    ReinicioSaldoComponent,
    LoadingComponent,
    ReporteTramiteComponent,
    ReportDeudoresComponent,
    ReportAcreedoresComponent,
    ReportUsuariosComponent,
    SubirImagenComponent,
    PermisosMenuComponent,
    NuevoRolMenuComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterializeModule,
    SweetAlert2Module.forRoot({
      buttonsStyling: false,
      customClass: 'modal-content',
      confirmButtonClass: 'btn waves-effect waves-light indigo',
      cancelButtonClass: 'btn waves-effect waves-light red darken-4 swal-2'
    }),
    LoadingModule.forRoot({
      animationType: ANIMATION_TYPES.threeBounce,
      backdropBackgroundColour: 'rgba(0,0,0,0.3)',
      backdropBorderRadius: '0',
      primaryColour: '#0D47A1',
      secondaryColour: '#1E88E5',
      tertiaryColour: '#1565C0'
    }),
    NgxPaginationModule,
    NgxDateRangePickerModule,
    CalendarModule,
    BrowserAnimationsModule,
    FileUploadModule
  ],
  providers: [
    AuthGuard,
    UsersService,
    ExternalUsersService,
    UserTramites,
    DepositsService,
    GuaranteesService,
    LoadingService,
    ExcelService,
    ImagenService,
    MenusService,
    
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    MailsService,
    IncidentsService,
    SitUsuarioPipe,
    BancoPipe,
    TipoPagoPipe,
    UsadaPipe,
    PrioridadPipe,
    EstadoPipe,
    ImpactoPipe,
    ModoIngresoPipe,
    CategoriaPipe,
    TipoSolicitudPipe,
    ChangeService,
    PentahoService,
    CategoriesService,
    TermsService,
    CountriesService,
    SurveyService,
    DeudoresService,
    GarantiasService,
    AcreedoresService,
    UsuariosService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
