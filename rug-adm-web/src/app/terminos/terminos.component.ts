import { Component, OnInit, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Term } from '../shared/term.model';
import { MaterializeAction } from 'angular2-materialize';
import { Subscription } from 'rxjs';
import { TermsService } from '../shared/terms.service';
import { LoadingService } from '../shared/loading.service';
import { CategoriesService } from '../shared/categories.service';
import { Category } from '../shared/category.model';

declare var Materialize:any;

@Component({
  selector: 'app-terminos',
  templateUrl: './terminos.component.html',
  styleUrls: ['./terminos.component.css']
})
export class TerminosComponent implements OnInit {
  public loading = false;
  termForm: FormGroup;
  terms: Term[];
  categories: Category[];
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalTerm: Term;
  httpSubscription: Subscription;
  categoriesHttpSubscription: Subscription;
  localSubscription: Subscription;
  editMode = false;
  deleteMode = false;
  editIndex: number;
  editId: number;
  pageSize: number = 10;
  currentPage: number = 1;
  total: number;
  incluirSinClasificar: boolean = false;
  @ViewChild('categoriaInput') categoriaInput: ElementRef;
  categoria: number;

  constructor(private termsService: TermsService,
    private categoriesService: CategoriesService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.termsService.terms = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.termsService.fetchData(this.currentPage, this.pageSize, this.incluirSinClasificar).subscribe(
      data => {
        this.terms = data.data;
        this.total = data.total;
        this.termsService.addTerms(this.terms);
        let outerthis = this;
        this.categoriesHttpSubscription = this.categoriesService.fetchData().subscribe(
          data => {
            outerthis.categories = data;
            console.log(outerthis.categories);
          },
          err => console.error(err),
          () => {
            this.loading = false;
            this.loadingService.changeLoading(this.loading);
          }
        );
      }
    );

    this.localSubscription = this.termsService.termsChanged.subscribe(
      (terms: Term[]) => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        let message = '';
        if (this.deleteMode) {
          message = 'La clasificacion fue eliminada exitosamente.';
        } else if (this.editMode) {
          message = 'La clasificacion fue asignada exitosamente.';
          // this.terms = terms;
        } else {
          message = 'La clasificacion fue asignada exitosamente.';
          // this.onPageChange(1);
        }
        Materialize.toast(message, 4000);
        this.onPageChange(1);
        // this.onCancelClicked();
      }
    );
  }

  onEditTermClicked(term: Term, index: number) {
    this.modalTerm = term;
    this.editMode = true;
    this.editIndex = index;
    this.editId = term.clasificacionTerminoId;
    this.termForm.patchValue({
      nombre: term.termino
    });
    Materialize.updateTextFields();
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  categoriaChanged(categoriaInformacionId: number, clasificacionTerminoId: number, index: number) {
    let term: Term = this.terms[index];
    term.categoriaId = categoriaInformacionId;
    console.log(term);
    // actualizar la clasificacion
    this.termsService.updateTerm(index, term);
    // refrescar los datos
  }

  /*onSubmit() {
    let term: Term = new Term;
    term.nombre = this.termForm.value.nombre;
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    if (this.editMode) {
      term.id = this.editId;
      this.termsService.updateTerm(this.editIndex, term);
    } else {
      this.termsService.addTerm(term);
    }
  }*/

  onSinClasificarChanged(event) {
    // cambiar rango
    this.refreshData();
  }

  refreshData() {
    this.terms = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.termsService.fetchData(1, this.pageSize, this.incluirSinClasificar).subscribe(
      res => {
        this.terms = res.data;
        this.total = res.total;
        this.currentPage = 1;
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

  onPageChange(page: number) {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.terms = [];
    this.httpSubscription = this.termsService.fetchData(page, this.pageSize, this.incluirSinClasificar).subscribe(
      data => {
        this.terms = data.data;
        this.currentPage = page;
        this.termsService.addTerms(this.terms);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }
}
