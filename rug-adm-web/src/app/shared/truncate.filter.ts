import { PipeTransform, Pipe } from "@angular/core";

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {
  limit: number = 50;
  
  transform(value: any, args?: any): any {
    if (value && value.length > this.limit) {
      value = value.substring(0, this.limit) + '...';
    }
    return value;
  }
}
