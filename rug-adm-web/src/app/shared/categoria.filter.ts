import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'categoria'
})
export class CategoriaPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Software';
            case 2:
                return 'Hardware';
            case 3:
                return 'Negocio';
            case 5:
                return 'Otra';
            case 4:
                return 'Terceros';
            default:
                return 'Ninguna';
        }
    }
}