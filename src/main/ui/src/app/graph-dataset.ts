import {GraphDatapoint} from "./graph-datapoint";

export interface GraphDataset {
  label: string;
  data: GraphDatapoint[];
  borderColor: string;
  country: string;
  currentPoints: number;
  currentPph: number;
}
