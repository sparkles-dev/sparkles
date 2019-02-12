import { ParsedUrl } from '../libs/reframed.interfaces';

export enum MessageTypes {
  LAUNCH = 'LAUNCH',
  FINISH = 'FINISH',
  CANCEL = 'CANCEL'
}

export interface Message {
  type: string;
  payload: any;
}

export interface LaunchMessage {
  type: MessageTypes.LAUNCH;
  payload: ParsedUrl;
}

export interface CancelMessage {
  type: MessageTypes.CANCEL;
  payload: ParsedUrl;
}

export interface FinishMessage {
  type: MessageTypes.FINISH;
  payload: ParsedUrl;
}
