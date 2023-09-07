export class Menu {

  public menuId: number;
  public nomMenu: string;
  public nomSubMenu: string;
  public rol: string;  
  public link: string;
  public escabecera:number;
  
  getFilter() {

    let filter = '';
    if (this.menuId && this.menuId > 0) {

      filter += 'menuId=' + this.menuId + '&';
    }    
    if (this.nomMenu && this.nomMenu.length > 0) {

      filter += 'nomMenu=' + this.nomMenu + '&';
    }
    if (this.nomSubMenu && this.nomSubMenu.length > 0) {

      filter += 'nomSubMenu=' + this.nomSubMenu + '&';
    }
    if (this.rol && this.rol.length > 0) {

      filter += 'rol=' + this.rol + '&';
    }
    if (this.link && this.link.length > 0) {

      filter += 'link=' + this.link + '&';
    }
    if (this.escabecera && this.escabecera > 0) {

      filter += 'escabecera=' + this.escabecera + '&';
    }
    filter = filter.substr(0, filter.length -1);
    return filter;

  }

}

