import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'usada'
})
export class UsadaPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    if (typeof value == 'string') {
      value = parseInt(value);
    }
    switch(value) {
      case 0:
        return 'Pendiente';
      case 1:
        return 'Aprobada';
      case -1:
        return 'Rechazada';
    }
  }
}
