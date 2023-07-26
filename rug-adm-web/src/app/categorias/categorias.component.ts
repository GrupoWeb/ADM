import { Component, OnInit, EventEmitter } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Category } from '../shared/category.model';
import { MaterializeAction } from 'angular2-materialize';
import { Subscription } from 'rxjs';
import { LoadingService } from '../shared/loading.service';
import { CategoriesService } from '../shared/categories.service';

declare var Materialize:any;

@Component({
  selector: 'app-categorias',
  templateUrl: './categorias.component.html',
  styleUrls: ['./categorias.component.css']
})
export class CategoriasComponent implements OnInit {
  public loading = false;
  categoryForm: FormGroup;
  categories: Category[];
  modalActions = new EventEmitter<string|MaterializeAction>();
  modalCategory: Category;
  httpSubscription: Subscription;
  localSubscription: Subscription;
  editMode = false;
  deleteMode = false;
  editIndex: number;
  editId: number;

  constructor(private categoriesService: CategoriesService,
    private loadingService: LoadingService) { }

  ngOnInit() {
    this.categoriesService.categories = [];
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.httpSubscription = this.categoriesService.fetchData().subscribe(
      data => {
        this.categories = data;
        this.categoriesService.addCategories(this.categories);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
    this.localSubscription = this.categoriesService.categoriesChanged.subscribe(
      (categories: Category[]) => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
        let message = '';
        if (this.deleteMode) {
          message = 'La categoria fue eliminada exitosamente.';
        } else if (this.editMode) {
          message = 'La categoria fue modificada exitosamente.';
          this.categories = categories;
        } else {
          message = 'La categoria fue creada exitosamente.';
          // this.onPageChange(1);
        }
        Materialize.toast(message, 4000);
        // this.onPageChange(1);
        this.onCancelClicked();
      }
    );
    this.initForm();
  }

  private initForm() {
    this.categoryForm = new FormGroup({
      'nombre': new FormControl('', Validators.required),
      'excluir': new FormControl('', Validators.required)
    });
  }

  onAddCategoryClicked() {
    this.modalCategory = new Category;
    this.editMode = false;
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onEditCategoryClicked(category: Category, index: number) {
    this.modalCategory = category;
    this.editMode = true;
    this.editIndex = index;
    this.editId = category.categoriaInformacionId;
    this.categoryForm.patchValue({
      nombre: category.nombre,
      excluir: (category.excluir === 1 ? true : false)
    });
    Materialize.updateTextFields();
    this.modalActions.emit({action:"modal", params:["open"]});
  }

  onSubmit() {
    let category: Category = new Category;
    category.nombre = this.categoryForm.value.nombre;
    category.excluir = (this.categoryForm.value.excluir ? 1 : 0);
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    if (this.editMode) {
      category.categoriaInformacionId = this.editId;
      this.categoriesService.updateCategory(this.editIndex, category);
    } else {
      this.categoriesService.addCategory(category);
    }
  }

  onCancelClicked() {
    this.modalActions.emit({action:"modal",params:['close']});
    this.categoryForm.reset();
    this.editMode = false;
    this.deleteMode = false;
    this.editIndex = -1;
  }

  /*onPageChange(page: number) {
    this.loading = true;
    this.loadingService.changeLoading(this.loading);
    this.categories = [];
    this.httpSubscription = this.categoriesService.fetchData(page, this.pageSize).subscribe(
      data => {
        this.categories = data.value;
        this.currentPage = page;
        this.categoriesService.addCategories(this.categories);
      },
      err => console.error(err),
      () => {
        this.loading = false;
        this.loadingService.changeLoading(this.loading);
      }
    );
  }*/
}
