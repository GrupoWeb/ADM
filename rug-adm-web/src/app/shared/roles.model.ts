export class Rol {

  public rolId: number;
  public nomRol: string;
  public tipoRol: string;
  
  
  getFilter() {

    let filter = '';
    if (this.rolId && this.rolId > 0) {

      filter += 'rolId=' + this.rolId + '&';
    }
    if (this.nomRol && this.nomRol.length > 0) {

      filter += 'nomRol=' + this.nomRol + '&';
    }
    if (this.tipoRol && this.tipoRol.length > 0) {

      filter += 'tipoRol=' + this.tipoRol + '&';
    }
    
    filter = filter.substr(0, filter.length -1);
    return filter;

  }

}

