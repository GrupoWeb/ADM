import { PipeTransform, Pipe } from "@angular/core";
import { PERSONA_DEUDOR, PERSONA_ACREEDOR, PERSONA_SOLICITANTE } from "./constants";

@Pipe({
  name: 'parte'
})
export class PartePipe implements PipeTransform {
  transform(value: any, args?: any): any {
    switch(value) {
      case PERSONA_DEUDOR:
        return 'Deudor';
      case PERSONA_ACREEDOR:
        return 'Acreedor';
      case PERSONA_SOLICITANTE:
        return 'Solicitante';
    }
  }
}
