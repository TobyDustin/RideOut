import {Vehicle} from "./vehicle";
import {Payment} from "./payment";

export class RiderInformation {
  emergencyContactNumber: string;
  isInsured: boolean;
  license: string;
  vehicles: Vehicle[];
  payments: Payment[];
}
