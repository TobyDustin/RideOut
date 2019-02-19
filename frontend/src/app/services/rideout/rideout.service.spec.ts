import { TestBed } from '@angular/core/testing';

import { RideOutService } from './ride-out.service';

describe('RideOutService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RideOutService = TestBed.get(RideOutService);
    expect(service).toBeTruthy();
  });
});
