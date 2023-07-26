import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import 'rxjs/Rx';
import { Observable } from "rxjs/Observable";
import { Subject } from "rxjs/Subject";

import { Counter } from "./counter.model";
import { environment } from "../../environments/environment";
import { ResponseRs } from "./response.model";
import { StatsFilter } from "./stats-filter.model";
import { SurveyStats } from "./survey-stats.model";
import { SurveyStatsMulti } from "./survey-stats-multi.model";

@Injectable()
export class SurveyService {
  
  constructor(private http: HttpClient) {}

  fetchStatsData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<SurveyStats[]>(environment.api_url + '/encuesta/stats/single?' + param);
  }

  fetchStatsMultiData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<SurveyStatsMulti[]>(environment.api_url + '/encuesta/stats/multiple?' + param);
  }

  fetchStatsTextData(filter: StatsFilter) {
    let param = filter.getFilter();
    return this.http.get<String[]>(environment.api_url + '/encuesta/stats/text?' + param);
  }
  
}
