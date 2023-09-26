import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import {Round} from "../round";
import {GraphService} from "../graph.service";
import {League} from "../league";
import {Division} from "../division";
import {Group} from "../group";
import {LeagueToCreate} from "../league-to-create";

@Component({
  selector: 'app-choose-league-dialog',
  templateUrl: './choose-league-dialog.component.html',
  styleUrls: ['./choose-league-dialog.component.css']
})
export class ChooseLeagueDialogComponent implements OnInit{
  @Input() displayDialog: boolean = false;
  @Output()  displayDialogChange = new EventEmitter<boolean>();
  @Output() selectedLeagueChange = new EventEmitter<League>();
  @Output() leagueToCreate = new EventEmitter<LeagueToCreate>();

  leagues: League[] = [];
  selectedLeague?: League;

  rounds: Round[] = [];
  selectedRound?: Round;

  divisions: Division[] = [];
  selectedDivision?: Division;

  groups: Group[] = [];
  selectedGroup?: Group;


  constructor(private graphService: GraphService) {}

  ngOnInit(): void {
        this.graphService.getRounds().subscribe((rounds: Round[]) => {
          this.rounds = rounds;
        });

        this.graphService.getDivisions().subscribe((divisions: Division[]) => {
          this.divisions = divisions;
        });

        this.graphService.getGroups().subscribe((groups: Group[]) => {
          this.groups = groups;
        });
    }

  searchLeagues(): void {
    this.graphService.searchLeagues(this.selectedRound, this.selectedDivision, this.selectedGroup).subscribe((leagues: League[]) => {
      this.leagues = leagues;
    });
  }

  closeDialog() : void {
    this.displayDialogChange.emit(false);
  }

  chooseLeague(): void {
    this.displayDialog = false;
    this.selectedLeagueChange.emit(this.selectedLeague);
  }

  createNewLeague(): void {
    console.info("selectedLeague: " + this.selectedLeague)
    this.selectedLeague = undefined;
    this.displayDialog = false;
    if (this.selectedRound) {
      this.leagueToCreate.emit({
        round: this.selectedRound.value,
        division: this.selectedDivision ? this.selectedDivision.value : undefined,
        group: this.selectedGroup ? this.selectedGroup.value : undefined
      });
    }
  }


  dontAllowChooseLeague() : boolean {
    if (this.selectedLeague) {
      return false;
    } else {
      return true;
    }
  }

  dontAllowCreateLeague() : boolean {
    return this.leagues.length > 0;
  }


}
