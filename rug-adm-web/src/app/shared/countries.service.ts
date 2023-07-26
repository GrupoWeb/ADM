import { Injectable } from "@angular/core";
import { Country } from "./country.model";
import { HttpClient } from "@angular/common/http";
import { ResponseRs } from "./response.model";
import { environment } from "../../environments/environment";

@Injectable()
export class CountriesService {
  constructor(private http: HttpClient) {}

  fetchData(status: string) {
    return this.http.get<ResponseRs>(environment.api_url + '/nacionalidades?status=' + status);
  }
}
