export class StatsFilter {
  public fechaInicio: string;
  public fechaFin: string;
  public fields: string;
  public cf: string;
  public dis: number;
  public im: boolean;
  public idPregunta: number;

  getFilter() {
    let filter = '';
    if (this.fechaInicio && this.fechaInicio.length > 0) {
      filter += 'fechaInicio=' + this.fechaInicio + '&';
    }
    if (this.fechaFin && this.fechaFin.length > 0) {
      filter += 'fechaFin=' + this.fechaFin + '&';
    }
    if (this.fields && this.fields.length > 0) {
      filter += 'fields=' + this.fields + '&';
    }
    if (this.cf && this.cf.length > 0) {
      filter += 'cf=' + this.cf + '&';
    }
    if (this.dis) {
      filter += 'dis=' + this.dis + '&';
    }
    if (this.im) {
      filter += 'im=' + this.im + '&';
    }
    if (this.idPregunta) {
      filter += 'idPregunta=' + this.idPregunta + '&';
    }

    filter = filter.substr(0, filter.length -1);

    return filter;
  }
}
