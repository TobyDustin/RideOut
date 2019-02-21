import {Vehicle} from "./vehicle";
import {Payment} from "./payment";
import {User} from "./user";

export class Rider extends User {
  emergencyContactNumber: string;
  isInsured: boolean;
  isLead: boolean;
  license: string;
  vehicles: Vehicle[];
  payments: Payment[];
}
