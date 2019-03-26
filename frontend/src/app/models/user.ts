import {RiderInformation} from "./rider";

export class User {
  id: string;
  username: string;
  password: string;
  role: string = "rider";
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  contactNumber: string;
  riderInformation: RiderInformation;
}
