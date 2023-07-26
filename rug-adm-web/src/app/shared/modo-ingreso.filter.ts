import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'modoIngreso'
})
export class ModoIngresoPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Email';
            case 2:
                return 'Llamada';
            case 3:
                return 'Formulario Web';
            case 4:
                return 'Otro';
            default:
                return 'Ninguno';
        }
    }
}