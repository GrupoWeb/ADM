import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'serialize'
})
export class SerializePipe implements PipeTransform {
  joinCharacter: string = ';';

  transform(value: any, args?: any): any {
    let serialized: string = '';
    if (value && Array.isArray(value)) {
      serialized = value.map(e => e.name).join(this.joinCharacter);
    }
    return serialized;
  }
}
