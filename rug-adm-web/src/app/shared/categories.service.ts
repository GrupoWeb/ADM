import { Injectable } from "@angular/core";
import { Category } from "./category.model";
import { Subject } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { ResponseRs } from "./response.model";
import { environment } from "../../environments/environment";

@Injectable()
export class CategoriesService {
  categories: Category[] = [];
  categoriesChanged = new Subject<Category[]>();

  constructor(private http: HttpClient) {}

  getCategories() {
    return this.categories.slice();
  }

  addCategory(category: Category) {
    this.saveData(category).subscribe(
      (response) => {
        let savedCategory: Category = response;
        this.categories.push(savedCategory);
        this.categoriesChanged.next(this.getCategories());
      },
      err => console.error(err)
    );
  }

  addCategories(categories: Category[]) {
    this.categories.push(...categories);
  }

  updateCategory(index: number, newCategory: Category) {
    this.updateData(newCategory).subscribe(
      (response) => {
        let updatedCategory: Category = response;
        this.categories[index] = updatedCategory;
        this.categoriesChanged.next(this.getCategories());
      }
    );
  }

  fetchData() {
    return this.http.get<Category[]>(environment.api_url + '/categorias');
  }

  saveData(category: Category) {
    return this.http.post<Category>(environment.api_url + '/categorias', category);
  }

  updateData(category: Category) {
    return this.http.put<Category>(environment.api_url + '/categorias/' + category.categoriaInformacionId, category);
  }
}
