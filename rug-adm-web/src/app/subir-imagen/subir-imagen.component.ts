import { Component, OnInit ,EventEmitter} from '@angular/core';
import { ImagenService } from '../shared/imagen.service';
import { environment } from "../../environments/environment";
import { HttpClient } from '@angular/common/http';
import { ResponseRs } from '../shared/response.model';
import { MaterializeAction } from 'angular2-materialize';

@Component({
  selector: 'app-subir-imagen',
  templateUrl: './subir-imagen.component.html',
  styleUrls: ['./subir-imagen.component.css']
})
export class SubirImagenComponent implements OnInit {
  imagenSeleccionada: File;
  modalAviso = new EventEmitter<string | MaterializeAction>();
  mensajeModal="";
  isSuccess: boolean;
  tituloModal: String;
  constructor(private imagenService: ImagenService,private http: HttpClient) { }
  onFileSelected(event) {
    this.imagenSeleccionada = event.target.files[0] as File;
    
    const extension = this.imagenSeleccionada.name.split('.').pop().toLowerCase();
    if (extension === 'jpg' || extension === 'png') {
      this.imagenSeleccionada = this.imagenSeleccionada;
    } else {
      this.imagenSeleccionada = null;
      console.log('Solo se permiten archivos JPG y PNG.');
    }
  }

  onUpload() {
   /* try {
      
  
      const base64String = await this.imagenService.subirImagen(this.imagenSeleccionada);
      const encodedBase64 = encodeURIComponent(base64String);

      // Enviar la cadena base64 en la solicitud HTTP
      const url = environment.api_url + '/fileUpload/';
      console.log(url);
      const data = { imagen: encodedBase64 };
      const response = await this.http.post(url,encodedBase64).toPromise();
      
      console.log('Respuesta del servidor:', response);
    } catch (error) {
      console.error('Error al enviar la imagen:', error);
    }*/
    this.imagenService.subirImagen(this.imagenSeleccionada).subscribe(      
        // Lógica para manejar la respuesta del servidor en caso de éxito
        res => {

          this.isSuccess = true;
          this.tituloModal = "Éxito";
          this.mensajeModal="Imagen subida exitosamente";
          this.modalAviso.emit({ action: "modal", params: ['open'] });
          
        },
        
     
        err => {
          this.isSuccess = false;
          this.tituloModal = "Error";
          this.mensajeModal="Error al Subir la imagen. Intente de nuevo.";
          this.modalAviso.emit({ action: "modal", params: ['open'] });
        },
        () => {
          console.log("VACIO");
        }
    );
  }
  ngOnInit() {
    
  }
  closeModal(){
     
    this.modalAviso.emit({ action: "modal", params: ['close'] });
    
  
  }

}