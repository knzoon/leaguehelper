import {User} from "./user";

export interface League {
  id: number;
  round: number;
  division: number;
  group: number;
  users: User[];
}
