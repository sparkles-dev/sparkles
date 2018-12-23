export interface Form {
  controls: Control[];
}

export interface Control {
  name: string;
  selector: string;
  value: any;
}
