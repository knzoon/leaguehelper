import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Round} from "../round";
import {Division} from "../division";
import {Group} from "../group";
import {User} from "../user";
import {GraphService} from "../graph.service";
import {League} from "../league";
import {LeagueToCreate} from "../league-to-create";

@Component({
  selector: 'app-save-league-dialog',
  templateUrl: './save-league-dialog.component.html',
  styleUrls: ['./save-league-dialog.component.css']
})
export class SaveLeagueDialogComponent implements OnInit {
  @Input() displayDialog: boolean = false;
  @Output()  displayDialogChange = new EventEmitter<boolean>();

  @Input() leagueToCreate: LeagueToCreate = {round: 0, division: undefined, group: undefined};

  rounds: Round[] = [];
  selectedRound?: Round;

  divisions: Division[] = [];
  selectedDivision?: Division;

  groups: Group[] = [];
  selectedGroup?: Group;


  selectedAddUser?: User;
  suggestedUsers: User[] = [];

  usersToSave: User[] = [];
  selectedSaveUser?: User;

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

  initSelectedValues() : void {
    this.setSelectedRound();
    this.setSelectedDivision();
    this.setSelectedGroup();
  }

  setSelectedRound() : void {
    let found = this.rounds.filter( round => round.value == this.leagueToCreate.round);
    if (found.length > 0) {
      this.selectedRound = found[0];
    } else {
      this.selectedRound = undefined;
    }
  }

  setSelectedDivision() : void {
    if (this.leagueToCreate.division) {
      let found = this.divisions.filter( division => division.value == this.leagueToCreate.division);
      if (found.length > 0) {
        this.selectedDivision = found[0];
      }
    } else {
      this.selectedDivision = undefined;
    }
  }

  setSelectedGroup() : void {
    if (this.leagueToCreate.group) {
      let found = this.groups.filter( group => group.value == this.leagueToCreate.group);
      if (found.length > 0) {
        this.selectedGroup = found[0];
      }
    } else {
      this.selectedGroup = undefined;
    }
  }


  closeDialog() : void {
    this.displayDialogChange.emit(false);
  }

  dontAllowChooseDivision() : boolean {
    if (this.leagueToCreate.division) {
      return true;
    } else {
      return false;
    }
  }

  dontAllowChooseGroup() : boolean {
    if (this.leagueToCreate.group) {
      return true;
    } else {
      return false;
    }
  }

  dontAllowSaveLeague() : boolean {
    return !(this.usersToSave.length === 10);
  }

  searchUsers(event: any) {
    this.graphService.getUsers(event.query).subscribe((users : User[]) => {
      this.suggestedUsers = users;
    });
  }

  userSelected(event: any) {
    if (this.selectedAddUser) {
      this.usersToSave.push(this.selectedAddUser);
      this.usersToSave = this.usersToSave.slice();
      this.selectedAddUser = undefined;
    }
  }

  deleteUser(user: any) {
    let id = this.usersToSave.indexOf(user);
    this.usersToSave.splice(id, 1);
    this.usersToSave = this.usersToSave.slice();
    this.selectedAddUser = undefined;
  }

  saveLeague(): void {
    if (this.selectedRound && this.selectedDivision && this.selectedGroup) {
      let league: League = {
        id: 0,
        round: this.selectedRound.value,
        division: this.selectedDivision.value,
        group: this.selectedGroup.value,
        users: this.usersToSave
      };

      this.graphService.saveLeague(league).subscribe(() => {
        console.info("League saved");
        // this.searchLeagues();
      });
    } else {
      console.info("selected parameters are bad: ", this.selectedRound, this.selectedDivision, this.selectedGroup);
    }

    this.displayDialog = false;
  }


}
