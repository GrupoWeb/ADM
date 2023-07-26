import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'banco'
})
export class BancoPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    switch(value) {
      case '1':
        return 'Banrural';
      case '2':
        return 'CHN';
      case 'BI3':
        return 'Banco Industrial'
      default:
        return 'Sin Nombre'
    }
  }
}
