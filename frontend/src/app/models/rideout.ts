import {Checkpoint} from "./checkpoint";
import {Rider} from "./rider";
import {Booking} from "./booking";

export class RideOut {
  id: string;
  rideoutType: string;
  name: string;
  dateStart: Date;
  dateEnd: Date;
  maxRiders: number;
  leadRider: Rider;
  route: string;
  isPublished: boolean;
  minCancellationDate: Date;
  checkpoints: Checkpoint[];
  riders: Rider[];
  travelBookings: Booking[]
  accommodationBookings: Booking[];
  restaurantBookings: Booking[];
}
