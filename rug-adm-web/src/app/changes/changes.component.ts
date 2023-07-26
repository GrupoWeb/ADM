import { Component, OnInit, Input, EventEmitter, ViewChild, ElementRef } from "@angular/core";
import { NgxDateRangePickerOptions, NgxDateRangePickerComponent } from "ngx-daterangepicker";
import { User } from "../shared/user.model";
import { FormGroup, FormControl } from "@angular/forms";
import { Change } from "../shared/changed.model";
import { MaterializeAction } from "angular2-materialize";
import { Subscription } from "rxjs";
import { EstadoPipe } from "../shared/estado.filter";
import { UsersService } from "../shared/users.service";
import { LoadingService } from "../shared/loading.service";
import { ExcelService } from "../shared/excel.service";
import { ChangeService } from "../shared/change.service";
import * as moment from 'moment';
import { ImpactoPipe } from "../shared/impacto.filter";

declare var Materialize: any;

@Component({
    selector: 'app-changes',
    templateUrl: './changes.component.html',
    styleUrls: ['./changes.component.css']
})
export class ChangesComponent implements OnInit {

    options: NgxDateRangePickerOptions;
    @Input() dateRange: NgxDateRangePickerComponent;
    rangoFechas: string | any;
    public loading = false;
    public user: User;
    changeForm: FormGroup;
    changes: Change[];
    exportChanges: Change[];
    modalActions = new EventEmitter<string | MaterializeAction>();
    modalChange: Change;
    httpSubscription: Subscription;
    localSubscription: Subscription;
    editMode = false;
    editIndex: number;
    editId: number;

    pageSize: number = 10;
    currentPage: number = 1;
    total: number;
    fechaInicio: string;
    fechaFin: string;

    filtro: Change;
    filTexto: string;
    filImpacto: string;
    filEstado: string;
    @ViewChild('filTextoInput') filTextoInput: ElementRef;
    @ViewChild('filImpactoInput') filImpactoInput: ElementRef;
    @ViewChild('filEstadoInput') filEstadoInput: ElementRef;

    constructor(
        private impacto: ImpactoPipe,
        private estado: EstadoPipe,
        private usersService: UsersService,
        private changeService: ChangeService,
        private loadingService: LoadingService,
        private excelService: ExcelService
    ) { }

    ngOnInit() {
        this.filtro = new Change;
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
                { alias: 'td', text: 'Hoy', operation: '0d' },
                { alias: 'tm', text: 'Este mes', operation: '0m' },
                { alias: 'lm', text: 'Mes pasado', operation: '-1m' },
                { alias: 'tw', text: 'Esta semana', operation: '0w' },
                { alias: 'lw', text: 'Semana pasada', operation: '-1w' },
                { alias: 'ty', text: 'Este año', operation: '0y' },
                { alias: 'ly', text: 'Año pasado', operation: '-1y' },
            ],
            dateFormat: 'DD/MM/YYYY',
            outputFormat: 'YYYY-MM-DD',
            outputType: "object",
            startOfWeek: 0
        };
        this.fechaInicio = moment().format('YYYY-MM-') + '01';
        this.fechaFin = moment().endOf('month').format('YYYY-MM-DD');
        this.changeService.changes = [];
        this.loading = false;
        this.loadingService.changeLoading(this.loading);

        this.httpSubscription = this.changeService.fetchData(this.currentPage, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
            res => {
                this.changes = res.data;
                this.total = res.total;
                this.changeService.addChanges(this.changes);
            },
            err => console.error(err),
            () => {
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );

        this.localSubscription = this.changeService.changesChanged.subscribe(
            (changes: Change[]) => {
                this.changes = changes;
                this.onCancelClicked();
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );

        this.initForm();
    }

    private initForm() {
        this.changeForm = new FormGroup({
            'sistema': new FormControl(''),
            'version': new FormControl(''),
            'impacto': new FormControl(''),
            'estado': new FormControl(''),
            'responsable': new FormControl(''),
            'descripcion': new FormControl(''),
            'fechaInicio': new FormControl(''),
            'fechaFin': new FormControl(),
            'observaciones': new FormControl('')
        });
    }

    onAddChangeClicked() {
        this.modalChange = new Change;
        this.editMode = false;

        this.modalActions.emit({
            action: "modal",
            params: ["open"]
        });
    }

    onEditChangeClicked(change: Change, index: number) {
        this.modalChange = change;
        this.editMode = true;
        this.editIndex = index;
        this.editId = change.cambioId;

        var fechaInicioF = null;
        if (change.fechaInicio != null) {
            const str = change.fechaInicio.split('/');

            const year1 = Number(str[2]);
            const month1 = Number(str[1]) - 1;
            const day1 = Number(str[0]);

            fechaInicioF = new Date(year1, month1, day1);
        }

        var fechaFinF = null;
        if (change.fechaFin != null) {
            const str1 = change.fechaFin.split('/');

            const year2 = Number(str1[2]);
            const month2 = Number(str1[1]) - 1;
            const day2 = Number(str1[0]);

            fechaFinF = new Date(year2, month2, day2);
        }

        this.changeForm.patchValue({
            sistema: change.sistema,
            version: change.version,
            impacto: change.impacto,
            estado: change.estado,
            responsable: change.usuarioSolicitante,
            descripcion: change.descripcion,
            observaciones: change.observaciones,
            fechaInicio: fechaInicioF,
            fechaFin: fechaFinF
        });
        Materialize.updateTextFields();
        this.modalActions.emit({
            action: "modal",
            params: ["open"]
        });
    }

    onSubmit() {
        let change: Change = new Change;
        change.sistema = this.changeForm.value.sistema;
        change.version = this.changeForm.value.version;
        change.impacto = this.changeForm.value.impacto;
        change.estado = this.changeForm.value.estado;
        change.usuarioSolicitante = this.changeForm.value.responsable;
        change.descripcion = this.changeForm.value.descripcion;
        change.observaciones = this.changeForm.value.observaciones;
        change.fechaInicio = this.changeForm.value.fechaInicio==null?null:moment(new Date(this.changeForm.value.fechaInicio)).format('DD/MM/YYYY').toString();
        change.fechaFin = this.changeForm.value.fechaFin==null?null:moment(new Date(this.changeForm.value.fechaFin)).format('DD/MM/YYYY').toString();
        change.usuarioId = this.user.usuarioId;        

        this.loading = true;
        this.loadingService.changeLoading(this.loading);

        if (this.editMode) {
            change.cambioId = this.editId;
            this.changeService.updateChange(this.editIndex, change);
        } else {
            change.fechaRegistro = moment().format('DD/MM/YYYY').toString();
            this.changeService.addChange(change);
        }
    }

    onCancelClicked() {
        this.modalActions.emit({
            action: "modal",
            params: ["close"]
        });
        this.changeForm.reset();
        this.editMode = false;
        this.editIndex = -1;
    }

    onPageChange(page: number) {
        this.changes = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.changeService.fetchData(page, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
          res => {
            this.changes = res.data;
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
        this.changeService.changes = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.changeService.fetchData(1, this.pageSize, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
            res => {
                this.changes = res.data;
                this.total = res.total;
                this.currentPage = 1;
                this.changeService.addChanges(this.changes);
            },
            err => console.error(err),
            () => {
                this.loading = false;
                this.loadingService.changeLoading(this.loading);
            }
        );
    }

    exportData() {
        this.exportChanges = [];
        this.loading = true;
        this.loadingService.changeLoading(this.loading);
        this.httpSubscription = this.changeService.fetchData(null, null, this.filtro, this.fechaInicio, this.fechaFin).subscribe(
          res => {
            this.exportChanges = res.data;
            const temp = this.exportChanges.map(el => ({
              "No. Referencia": el.cambioId ? el.cambioId : '',
              "Sistema": el.sistema,
              "Versión": el.version,              
              "Estado": this.estado.transform(el.estado),
              "Responsable": el.usuarioSolicitante,
              "Impacto": this.impacto.transform(el.impacto),
              "Fecha Programada Inicio": el.fechaInicio,
              "Fecha Programada Fin": el.fechaFin,
              "Descripcion": el.descripcion,
              "Observaciones": el.observaciones                
            }));
            this.excelService.export(temp, 'Control de Cambios');
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