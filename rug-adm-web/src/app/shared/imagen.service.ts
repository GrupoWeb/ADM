import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";

@Injectable()
export class ImagenService {

  private apiUrl = 'URL_DEL_BACKEND'; // Reemplaza con la URL de tu backend

  constructor(private http: HttpClient) { }

  subirImagen(imagen: File) {
    /*const reader = new FileReader();
    reader.readAsDataURL(imagen);
    var encodedBase64;
    reader.onload = () => {
      const base64String = reader.result.toString();
      encodedBase64 = encodeURIComponent(base64String);
      console.log('Imagen en base64:', encodedBase64);
      console.log(environment.api_url + 'fileUpload/upload/?texto='+encodedBase64);
    return this.http.get(environment.api_url + 'fileUpload/upload/?texto='+encodedBase64);
    };*/
    const formData = new FormData();
    formData.append('adjunto', imagen);
    console.log(environment.api_url + '/fileupload' ,formData );
    return this.http.post<ResponseRs>(environment.api_url + '/fileupload' ,formData);



  }
 /* subirImagen(imagen: File): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const base64String = reader.result.toString();
        //console.log('Imagen en base64:', base64String);
        resolve(base64String);
      };
      reader.onerror = () => {
        reject('Error al leer el archivo');
      };
  
      reader.readAsDataURL(imagen);
    });
  }*/

}