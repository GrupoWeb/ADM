import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'estado'
})
export class EstadoPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Abierto';
            case 2:
                return 'Cerrado';
            case 3:
                return 'Resuelto';
            case 4:
                return 'En Espera';
            case 991:
                return 'Desarrollo';
            case 992:
                return 'Pruebas';
            case 993:
                return 'Publicado'
            default:
                return 'Ninguno';
        }
    }
}