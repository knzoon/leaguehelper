import { Component, OnInit } from '@angular/core';
import {GraphData} from "./graph-data";
import {GraphService} from "./graph.service";
import {GraphDataset} from "./graph-dataset";
import {League} from "./league";
import {LeagueToCreate} from "./league-to-create";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Leaguehelper';

  dataDaily: any;
  dataCumulative?: GraphData;

  optionsCumulative: any;

  // checkboxes
  selectedUsers: GraphDataset[] = [];
  users: GraphDataset[] = [];

  displayChooseLeagueDialogNEW: boolean = false;
  displaySaveLeagueDialogNEW: boolean = false;

  selectedLeague?: League;

  leagueToCreate: LeagueToCreate = {round: 0, division: undefined, group: undefined};

  constructor(private graphService: GraphService) {}


  ngOnInit(): void {
    this.dataDaily = this.getDataForGraph();
    this.dataCumulative = this.getDataForGraph();
    this.optionsCumulative = {
      parsing: {
        xAxisKey: 'day',
        yAxisKey: 'points'
      },
      plugins: {
        title: {
          display: true,
          text: 'Summerad poäng'
        },
        tooltip: {
          callbacks: {
            title: function(context: any) {
              return 'pph: ' + context[0].raw.pph;
            }
          }
        }
      }
    };
  }

  getDataForGraph(): GraphData {
    return {
      datasets: []
    };
  }

  updateGraphDaily(): void {
    this.graphService.getGraphPerDay().subscribe((graphData: GraphData) => {
      this.dataDaily = graphData;
    });
  }

  updateGraphCumulative(): void {
    console.info("selected users are: ", this.selectedUsers);
    this.dataCumulative = {
      datasets: this.selectedUsers
    };
  }

  fetchLeague(): void {
    // this.users = this.graphService.mockGraphData().datasets;
    if (this.selectedLeague) {
      this.graphService.getGraphCumulative(this.selectedLeague.id).subscribe((graphData: GraphData) => {
        this.users = graphData.datasets;
        this.selectedUsers = [...this.users];
      });
    }
  }

  showChooseLeagueDialogNEW() {
    this.selectedLeague = undefined;
    this.users = [];
    this.selectedUsers = [];
    this.dataCumulative = {
      datasets: []
    };
    this.displayChooseLeagueDialogNEW = true;
  }

  setSelectedLeague(selectedLeagueChange: League) {
    this.selectedLeague = selectedLeagueChange;
  }

  handleCreateLeague(leagueToCreate : LeagueToCreate) {
    console.info("league to create: ",  leagueToCreate);
    this.leagueToCreate = leagueToCreate;
    this.displaySaveLeagueDialogNEW = true;
  }
}
