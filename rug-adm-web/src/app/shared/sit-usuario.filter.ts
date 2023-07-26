import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'sitUsuario'
})
export class SitUsuarioPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    switch(value) {
      case 'AC':
        return 'Activo';
      case 'IN':
        return 'Pendiente de activación';
      case 'PA':
        return 'Pendiente de aprobación';
      case 'RE':
        return 'Rechazado';
      case 'HO':
        return 'Homologado';
      case 'CA':
        return 'De baja';
    }
  }
}
