import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'prioridad'
})
export class PrioridadPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Baja';
            case 2:
                return 'Normal';
            case 3:
                return 'Media';
            case 4:
                return 'Alta';
            default:
                return 'Ninguna';
        }
    }
}