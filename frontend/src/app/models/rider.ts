import {Vehicle} from "./vehicle";
import {Payment} from "./payment";

export interface Rider {
  id: string;
  username: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  contactNumber: string;
  emergencyContactNumber: string;
  isInsured: boolean;
  isLead: boolean;
  license: string;
  vehicles: Vehicle[];
  payments: Payment[];
}
