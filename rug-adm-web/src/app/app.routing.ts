import { NgModule } from "@angular/core";
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import { LoginComponent } from "./login/login.component";
import { UsersComponent } from "./users/users.component";
import { DepositsComponent } from "./deposits/deposits.component";
import { LayoutComponent } from "./layout/layout.component";
import { AuthGuard } from "./shared/auth.guard";
import { ExternalUsersComponent } from "./external-users/external-users.component";
import { ApprovalComponent } from "./approval/approval.component";
import { ExternalUsersReportComponent } from "./external-users-report/external-users-report.component";
import { GuaranteesReportComponent } from "./guarantees-report/guarantees-report.component";
import { MailsComponent } from "./mails/mails.component";
import { DepositsReportComponent } from "./deposits-report/deposits-report.component";
import { ExternalUsersStatsComponent } from "./external-users-stats/external-users-stats.component";
import { GuaranteesStatsComponent } from "./guarantees-stats/guarantees-stats.component";
import { DepositsStatsComponent } from "./deposits-stats/deposits-stats.component";
import { VinculacionComponent } from "./vinculacion/vinculacion.component";
import { ExternalUsersSearchComponent } from "./external-users-search/external-users-search.component";
import { ModificarMigradoComponent } from "./modificar-migrado/modificar-migrado.component";
import { QueryReportComponent } from "./query-report/query-report.component";
import { VinculacionesReportComponent } from "./vinculaciones-report/vinculaciones-report.component";
import { ReporteTramiteComponent } from "./reporte-tramite/reporte-tramite.component";
import { ModificarCorreoComponent } from "./modificar-correo/modificar-correo.component";
import { IncidentsComponent } from "./incidents/incidents.component";
import { ChangesComponent } from "./changes/changes.component"
import { CorreccionesComponent } from "./correcciones/correcciones.component";
import { InscripcionComponent } from "./inscripcion/inscripcion.component";
import { ReportsDashboardComponent } from "./reports-dashboard/reports-dashboard.component";
import { TerminosComponent } from "./terminos/terminos.component";
import { CategoriasComponent } from "./categorias/categorias.component";
import { AssetSearchComponent } from "./asset-search/asset-search.component";
import { SurveyStatsComponent } from "./survey-stats/survey-stats.component";
import { FirmaComponent } from "./firma/firma.component";
import { ReinicioSaldoComponent } from "./reinicio-saldo/reinicio-saldo.component";
import { ReportDeudoresComponent } from "./report-deudores/report-deudores.component";
import { ReportAcreedoresComponent } from "./report-acreedores/report-acreedores.component";
import { ReportUsuariosComponent } from "./report-usuarios/report-usuarios.component";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    component: LayoutComponent,
    canActivateChild: [AuthGuard],
    children: [
      {
        path: 'home',
        component: HomeComponent,
        data: {
          roles: ['R', 'V', 'C']
        }
      },
      {
        path: 'approval',
        component: ApprovalComponent,
        data: {
          roles: ['R', 'V']
        }
      },
      {
        path: 'external-users',
        component: ExternalUsersComponent,
        data: {
          roles: ['R', 'V']
        }
      },
      {
        path: 'users',
        component: UsersComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'deposits',
        component: DepositsComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'external-users-report',
        component: ExternalUsersReportComponent,
        data: {
          roles: ['R', 'V']
        }
      },
      {
        path: 'guarantees-report',
        component: GuaranteesReportComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'deposits-report',
        component: DepositsReportComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'external-users-stats',
        component: ExternalUsersStatsComponent,
        data: {
          roles: ['R', 'V']
        }
      },
      {
        path: 'guarantees-stats',
        component: GuaranteesStatsComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'survey-stats',
        component: SurveyStatsComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'deposits-stats',
        component: DepositsStatsComponent,
        data: {
          roles: ['R', 'C']
        }
      },
      {
        path: 'mails',
        component: MailsComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'vinculacion',
        component: VinculacionComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'external-users-search',
        component: ExternalUsersSearchComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'reinicio-saldo',
        component: ReinicioSaldoComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'modificar-migrado',
        component: ModificarMigradoComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'query-report',
        component: QueryReportComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'vinculaciones-report',
        component: VinculacionesReportComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'tramites-reporte',
        component: ReporteTramiteComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'modificar-correo',
        component: ModificarCorreoComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'incidents',
        component: IncidentsComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'changes',
        component: ChangesComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'correcciones',
        component: CorreccionesComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'inscripcion',
        component: InscripcionComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'reports-dashboard/:id',
        component: ReportsDashboardComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'reports-dashboard',
        component: ReportsDashboardComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'categorias',
        component: CategoriasComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'terminos',
        component: TerminosComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'asset-search',
        component: AssetSearchComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'firma',
        component: FirmaComponent,
        data: {
          roles: ['R']
        }
      },
      {
        path: 'report-deudores',
        component: ReportDeudoresComponent,
        data: {
          roles: ['R','C']
        }
      },
      {
        path: 'report-acreedores',
        component: ReportAcreedoresComponent,
        data: {
          roles: ['R','C']
        }
      },
      {
        path: 'report-usuarios',
        component: ReportUsuariosComponent,
        data: {
          roles: ['R','C']
        }
      },
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, {useHash: true}) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {

}
