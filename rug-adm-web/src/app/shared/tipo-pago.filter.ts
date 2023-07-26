import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'pago'
})
export class TipoPagoPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    switch(value) {
      case '1':
        return 'Efectivo';
      case '2':
        return 'Cheque Propio';
      case '3':
        return 'Cheque Otros Bancos';
      case '5':
        return 'Ventanilla';
    }
  }
}
