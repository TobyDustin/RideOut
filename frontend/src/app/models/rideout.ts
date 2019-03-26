import {Checkpoint} from "./checkpoint";
import {Booking} from "./booking";
import {User} from "./user";

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
  riders: User[];
  travelBookings: Booking[];
  accommodationBookings: Booking[];
  restaurantBookings: Booking[];
}
