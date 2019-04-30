import {Checkpoint} from "./checkpoint";
import {Booking} from "./booking";
import {User} from "./user";
import {Vehicle} from "./vehicle";

export class RideOut {
  id: string;
  rideoutType: string;
  name: string;
  dateStart: Date;
  dateEnd: Date;
  maxRiders: number;
  leadRider: User;
  route: string;
  published: boolean;
  minCancellationDate: Date;
  checkpoints: Checkpoint[];
  riders: [{
    id: string,
    firstName: string,
    lastName: string,
    vehicle: Vehicle
  }];
  travelBookings: Booking[];
  accommodationBookings: Booking[];
  restaurantBookings: Booking[];
}
