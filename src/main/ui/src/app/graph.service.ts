import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable, of } from "rxjs";
import {GraphData} from "./graph-data";
import {User} from "./user";
import {League} from "./league";
import {Division} from "./division";
import {Round} from "./round";
import {Group} from "./group";


@Injectable({
  providedIn: 'root'
})
export class GraphService {

  private baseURL = '/api/';

  constructor(private http: HttpClient) { }

  getRounds() : Observable<Round[]> {
    return of([{value: 156, name: "156"}, {value: 155, name: "155"}, {value: 154, name: "154"}, {value: 153, name: "153"}, {value: 152, name: "152"}]);
  }

  getDivisions() : Observable<Division[]> {
    return of([{value: 0, name: "Elite"}, {value: 1, name: "1"}, {value: 2, name: "2"}, {value: 3, name: "3"}, {value: 4, name: "4"}, {value: 5, name: "5"}, {value: 6, name: "6"}, {value: 7, name: "7"}, {value: 8, name: "8"}, {value: 9, name: "9"}, {value: 10, name: "10"}, {value: 11, name: "11"}]);
  }

  getGroups() : Observable<Group[]> {
    return of(this.generateGroups());
  }

  private generateGroups(): Group[] {
    let groups: Group[] = [];
    for (let i = 1; i < 513; i++) {
      groups.push({ value: i, name: i.toString()});
    }
    return groups;
  }


  getGraphPerDay(): Observable<GraphData> {
    const url = `${this.baseURL}graphPerDay`;
    return this.http.get<GraphData>(url);
  }

  getGraphCumulative(leagueId: number): Observable<GraphData> {
    const url = `${this.baseURL}graphCumulative` + "?leagueId=" + leagueId;
    return this.http.get<GraphData>(url);
  }

  getUsers(searchStr: string): Observable<User[]> {
    const url = `${this.baseURL}users` + "?searchString=" +  searchStr;
    return this.http.get<User[]>(url);
  }

  saveLeague(league: League): Observable<League> {
    const url = `${this.baseURL}league`;
    return this.http.post<League>(url, league);
  }

  searchLeagues(round?: Round, division?: Division, group?: Group): Observable<League[]> {
    console.info("search params are: ", round, division, group);
    let first: boolean = true;
    let roundPart = '';
    let divisionPart = '';
    let groupPart = '';

    if (round) {
      roundPart = this.prefixParam(first) + "roundId=" + round.value;
      first = false;
    }

    if (division) {
      divisionPart = this.prefixParam(first) + "division=" + division.value;
      first = false;
    }

    if (group) {
      groupPart = this.prefixParam(first) + "group=" + group.value;
    }

    const url = `${this.baseURL}searchLeagues` + roundPart + divisionPart + groupPart;
    return this.http.get<League[]>(url);
  }

  private prefixParam(isFirst: boolean): string {
    return isFirst ? "?" : "&";
  }
}
