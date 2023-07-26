import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'impacto'
})
export class ImpactoPipe implements PipeTransform {
    transform(value: any, args?:any):any {
        switch(value) {
            case 1: 
                return 'Bajo';
            case 2:
                return 'Medio';
            case 3:
                return 'Alto';            
            default:
                return 'Ninguno';
        }
    }
}