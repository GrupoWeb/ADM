import { Component, OnInit, Input, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from 'ngx-daterangepicker';
import * as moment from 'moment';
import { LoadingService } from '../shared/loading.service';
import { ExcelService } from '../shared/excel.service';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MaterializeAction } from 'angular2-materialize';
import { Incident } from '../shared/incident.model';
import { Subscription } from 'rxjs';
import { IncidentsService } from '../shared/incidents.service';
import { PrioridadPipe } from '../shared/prioridad.filter';
import { EstadoPipe } from "../shared/estado.filter";
import { ImpactoPipe } from "../shared/impacto.filter";
import { ModoIngresoPipe } from "../shared/modo-ingreso.filter";
import { CategoriaPipe } from "../shared/categoria.filter";
import { TipoSolicitudPipe } from "../shared/tipo-solicitud.filter";
import { User } from '../shared/user.model';
import { UsersService } from '../shared/users.service';
import { Attachment } from '../shared/attachment.model';

declare var Materialize:any;

@Component({
    selector: 'app-incidents',
    templateUrl: './incidents.component.html',
    styleUrls: ['./incidents.component.css']
  })
  export class IncidentsComponent implements OnInit {

    options: NgxDateRangePickerOptions;
    @Input() dateRange: NgxDateRangePickerComponent;
    rangoFechas: string | any;
    public loading = false;
    public user: User;
    incidentForm: FormGroup;
    incidents: Incident[];
    exportIncidents: Incident[];
    modalActions1 = new EventEmitter<string|MaterializeAction>();
    modalActions2 = new EventEmitter<string|MaterializeAction>();
    modalActions3 = new EventEmitter<string|MaterializeAction>();
    modalIncident: Incident;
    httpSubscription: Subscription;
    httpSubscriptionAtt: Subscription;
    localSubscriptionAtt: Subscription;
    localSubscription: Subscription;
    editMode = false;
    editIndex: number;
    editId: number;
    attachments: Attachment[];
    modalAttachment : Attachment;
    attachmentForm : FormGroup;
    
    pageSize: number = 10;
    currentPage: number = 1;
    total: number;
    fechaInicio: string;
    fechaFin: string;    
    currentIdIncident: number;

    filtro: Incident;
    filTexto: string;
    filImpacto: string;
    filEstado: string;
    filTipo: string;
    filCategoria: string;
    filModo: string;
    filPrioridad: string;
    @ViewChild('filTextoInput') filTextoInput: ElementRef;
    @ViewChild('filImpactoInput') filImpactoInput: ElementRef;
    @ViewChild('filEstadoInput') filEstadoInput: ElementRef;
    @ViewChild('filTipoInput') filTipoInput: ElementRef;
    @ViewChild('filCategoriaInput') filCategoriaInput: ElementRef;
    @ViewChild('filModoInput') filModoInput: ElementRef;
    @ViewChild('filPrioridadInput') filPrioridadInput: ElementRef;


    constructor(
        private prioridad: PrioridadPipe,
        private estado: EstadoPipe,
        private impacto: ImpactoPipe,
        private modoIngreso: ModoIngresoPipe,
        private categoria: CategoriaPipe,
        private tipoSolicitud : TipoSolicitudPipe,
        private usersService: UsersService,
        private incidentService: IncidentsService,
        private loadingService: LoadingService,
        private excelService: ExcelService) { }

    ngOnInit() {
        this.filtro = new Incident;
        const today = moment().toDate();
        this.user = this.usersService.authenticatedUser;
        
        this.rangoFechas = {
            from: new Date(today.getFullYear(), today.getMonth(), 1),
            to: new Date(today.getFullYear(), today.getMonth(), 1)
            };
            this.options = {
            theme: 'default',
            range: 'tm', // The alias of item menu
            labels: ['Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab', 'Dom'],
            menu: [
                {alias: 'td', text: 'Hoy', operation: '0d'},
                {alias: 'tm', text: 'Este mes', operation: '0m'},
                {alias: 'lm', text: 'Mes pasado', operation: '-1m'},
                {alias: 'tw', text: 'Esta semana', operation: '0w'},
                {alias: 'lw', text: 'Semana pasada', operation: '-1w'},
                {alias: 'ty', text: 'Este año', operation: '0y'},
                {alias: 'ly', text: 'Año pasado', operation: '-1y'},
            ],
            dateFormat: 'DD/MM/YYYY',
            outputFormat: 'YYYY-MM-DD',
            outputType: "object",
            startOfWeek: 0
        };
        this.fechaInicio = moment().format('YYYY-MM-') + '01';
        this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
        this.incidentService.incidents = [];
        this.loading = false;
        this.loadingService.changeLoading(this.loading);

        this.httpSubscription = this.incidentService.fetchData(this.currentPage,
                                                               this.pageSize,
                                                               this.filtro,
                                                               this.fechaInicio,
                                                               this.fechaFin).subscribe(
            res => {
                this.incidents = res.data;
                this.total = res.total;
                this.incidentService.addIncidents(this.incidents);
            },
            err => console.error(err),
            () => {
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );

        this.localSubscription = this.incidentService.incidentsChanged.subscribe(
            (incidents: Incident[]) => {
                this.incidents = incidents;
                this.onCancelClicked();
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );

        this.localSubscriptionAtt = this.incidentService.attachmentsChanged.subscribe(
            (attachments: Attachment[]) => {
                this.attachments = attachments;
                this.onCancelAttClicked();
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );

        this.initForm();        
    }

    private initForm(){        
        this.incidentForm = new FormGroup({
            'asunto' : new FormControl('', Validators.required),
            'fechaCreacion' : new FormControl(''),
            'fechaFin' : new FormControl(''),
            'tipoSolicitud' : new FormControl(''),
            'categoria' : new FormControl(''),
            'modoIngreso' : new FormControl(''),
            'prioridad' : new FormControl(''),
            'impacto' : new FormControl(''),
            'estado' : new FormControl(''),
            'solicitante' : new FormControl(''),
            'descripcion' : new FormControl(''),
            'resolucion' : new FormControl('')
        });

        this.attachmentForm = new FormGroup({            
            'adjuntoatt': new FormControl('')
        });

    }

    onAddIncidentClicked() {
        this.modalIncident = new Incident;
        this.editMode = false;
        this.incidentForm.get('fechaCreacion').patchValue(moment().toDate());
        //console.log(this.incidentForm.get('fechaCreacion').value);
        Materialize.updateTextFields();
        this.modalActions1.emit({
            action:"modal",
            params: ["open"]
        });
    }

    onAddAdjuntoClicked() {    
        this.attachmentForm.reset();    
        this.modalAttachment = new Attachment;

        Materialize.updateTextFields();   
        this.modalActions3.emit({
            action:"modal",
            params: ["open"]
        });     
    }

    onViewAttachClicked(incident: Incident, index: number) {        
        this.currentIdIncident = incident.incidenteId;

        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscriptionAtt = this.incidentService.fetchDataAtt(this.currentIdIncident).subscribe(
          res => {
            this.attachments = res.data;                      
          },
          err => {
            console.error(err);
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          },
          () => {
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          }
        );

        this.modalActions2.emit({
            action:"modal",
            params:["open"]
        });
    }

    onEditIncidentClicked(incident: Incident, index: number) {
        this.modalIncident = incident;
        this.editMode = true;
        this.editIndex = index;
        this.editId = incident.incidenteId;

        var fechaInicioF = null;
        if (incident.fechaCreacion != null) {
            const str = incident.fechaCreacion.split('/');

            const year1 = Number(str[2]);
            const month1 = Number(str[1]) - 1;
            const day1 = Number(str[0]);

            fechaInicioF = new Date(year1, month1, day1);
        }

        var fechaFinF = null;
        if (incident.fechaFin != null) {
            const str1 = incident.fechaFin.split('/');

            const year2 = Number(str1[2]);
            const month2 = Number(str1[1]) - 1;
            const day2 = Number(str1[0]);

            fechaFinF = new Date(year2, month2, day2);
        }

        this.incidentForm.patchValue({
            asunto : incident.asunto,
            fechaCreacion : fechaInicioF,
            fechaFin : fechaFinF,
            tipoSolicitud : incident.tipoSolicitud,
            categoria : incident.categoria,
            modoIngreso : incident.modoIngreso,
            prioridad : incident.prioridad,
            impacto : incident.impacto,
            estado : incident.estado,
            solicitante : incident.usuarioSolicitante,
            descripcion : incident.descripcion,
            resolucion : incident.resoulucion
        });
        Materialize.updateTextFields();
        this.modalActions1.emit({
            action:"modal",
            params:["open"]
        });
    }

    onSubmit() {
        let incident: Incident = new Incident;
        incident.asunto = this.incidentForm.value.asunto;
        incident.fechaCreacion = moment(new Date(this.incidentForm.value.fechaCreacion)).format('DD/MM/YYYY').toString();
        incident.fechaFin = this.incidentForm.value.fechaFin==null?null:moment(new Date(this.incidentForm.value.fechaFin)).format('DD/MM/YYYY').toString();
        incident.tipoSolicitud = this.incidentForm.value.tipoSolicitud;
        incident.categoria = this.incidentForm.value.categoria;
        incident.modoIngreso = this.incidentForm.value.modoIngreso;
        incident.prioridad = this.incidentForm.value.prioridad;
        incident.impacto = this.incidentForm.value.impacto;
        incident.estado = this.incidentForm.value.estado;
        incident.usuarioSolicitante = this.incidentForm.value.solicitante;
        incident.descripcion = this.incidentForm.value.descripcion;
        incident.resoulucion = this.incidentForm.value.resolucion;
        incident.usuarioId = this.user.usuarioId;

        this.loading = true;
        this.loadingService.changeLoading(this.loading);

        if(this.editMode){
            incident.incidenteId = this.editId;
            this.incidentService.updateIncidents(this.editIndex,incident);
        } else {
            this.incidentService.addIncident(incident);
        }
    }

    onSubmitAtt() {
        let attachment : Attachment = new Attachment;        
        attachment.adjunto = this.attachmentForm.value.adjuntoatt;

        this.loading = true;
        this.loadingService.changeLoading(this.loading);

        this.incidentService.addAttachment(attachment, this.attachments);
        
    }

    onFileChange(event : any) {
        let formData: FormData = new FormData();
        if(event.target.files && event.target.files.length > 0) {
          let file = event.target.files[0];
          formData.append('adjunto', file);
        }
        formData.append('idIncidente', this.currentIdIncident.toString());

        this.attachmentForm.get("adjuntoatt").patchValue(formData);
      }

    onCancelClicked() {
        this.modalActions1.emit({
            action:"modal",
            params:["close"]
        });
        this.incidentForm.reset();
        this.editMode = false;
        this.editIndex = -1;
    }

    onCancelAttsClicked() {
        this.modalActions2.emit({
            action: "modal",
            params:["close"]
        });        
    }

    onCancelAttClicked() {
        this.modalActions3.emit({
            action: "modal",
            params:["close"]
        });
        this.attachmentForm.reset();
    }

    onPageChange(page: number) {
        this.incidents = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.incidentService.fetchData(page, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
          res => {
            this.incidents = res.data;
            this.total = res.total;
            this.currentPage = page;
          },
          err => {
            console.error(err);
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          },
          () => {
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          }
        );
      }

    filTextoChanged(filTexto: string) {
        this.filtro.texto = filTexto;
        this.refreshData();
    }

    onFilTexto() {
        let event = new Event('blur', {});
        this.filTextoInput.nativeElement.dispatchEvent(event);
    }

    filImpactoChanged(filImpacto: number) {
        this.filtro.impacto = filImpacto;
        this.refreshData();
    }

    onFilImpacto() {
        let event = new Event('blur', {});
        this.filImpactoInput.nativeElement.dispatchEvent(event);
    }

    filTipoChanged(filTipo: number) {
        this.filtro.tipoSolicitud = filTipo;
        this.refreshData();
    }

    onFilTipo() {
        let event = new Event('blur', {});
        this.filTipoInput.nativeElement.dispatchEvent(event);
    }

    filCategoriaChanged(filCategoria: number) {
        this.filtro.categoria = filCategoria;
        this.refreshData();
    }

    onFilCategoria() {
        let event = new Event('blur', {});
        this.filCategoriaInput.nativeElement.dispatchEvent(event);
    }

    filModoChanged(filModo: number) {
        this.filtro.modoIngreso = filModo;
        this.refreshData();
    }

    onFilModo() {
        let event = new Event('blur', {});
        this.filModoInput.nativeElement.dispatchEvent(event);
    }

    filPrioridadChanged(filPrioridad: number) {
        this.filtro.prioridad = filPrioridad;
        this.refreshData();
    }

    onFilPrioridad() {
        let event = new Event('blur', {});
        this.filPrioridadInput.nativeElement.dispatchEvent(event);
    }

    filEstadoChanged(filEstado: number) {
        this.filtro.estado = filEstado;
        this.refreshData();
    }

    onFilEstado() {
        let event = new Event('blur', {});
        this.filEstadoInput.nativeElement.dispatchEvent(event);
    }

    onRangeChanged(event) {
        if (this.fechaInicio !== event.from || this.fechaFin !== event.to) {
            this.fechaInicio = event.from;
            this.fechaFin = event.to;
            this.refreshData();
        }
    }

    refreshData() {
        this.incidentService.incidents = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.incidentService.fetchData(1, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
            res => {
                this.incidents = res.data;
                this.total = res.total;
                this.currentPage = 1;
                this.incidentService.addIncidents(this.incidents);
            },
            err => console.error(err),
            () => {
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );
    }

    exportData() {
        this.exportIncidents = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.incidentService.fetchData(null, null, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
          res => {
            this.exportIncidents = res.data;
            const temp = this.exportIncidents.map(el => ({
              "No. Referencia": el.incidenteId ? el.incidenteId : '',
              "Fecha Inicio": el.fechaCreacion,
              "Fecha Fin": el.fechaFin,              
              "Asunto": el.asunto,              
              "Tipo Solicitud": this.tipoSolicitud.transform(el.tipoSolicitud),
              "Categoria": this.categoria.transform(el.categoria),
              "Modo Ingreso": this.modoIngreso.transform(el.modoIngreso),
              "Prioridad": this.prioridad.transform(el.prioridad),
              "Impacto": this.impacto.transform(el.impacto),
              "Estado": this.estado.transform(el.estado),
              "Solicitante": el.usuarioSolicitante,
              "Descripcion": el.descripcion,
              "Resolucion": el.resoulucion
            }));
            this.excelService.export(temp, 'Incidentes');
          },
          err => {
            console.error(err);
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          },
          () => {
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          }
        );
      }
  }