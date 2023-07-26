import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'persona'
})
export class TipoPersonaPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    switch(value) {
      case 'PF':
        return 'Individual';
      case 'PM':
        return 'Jurídica';
    }
  }
}
