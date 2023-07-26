import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'tipoSolicitud'
})
export class TipoSolicitudPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Incidente';
            case 2:
                return 'Problema';
            case 3:
                return 'Nueva Funcionalidad';
            case 4:
                return 'Otros';
            default:
                return 'Ninguna';
        }
    }
}