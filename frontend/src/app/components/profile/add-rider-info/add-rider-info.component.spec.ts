import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRiderInfoComponent } from './add-rider-info.component';

describe('AddRiderInfoComponent', () => {
  let component: AddRiderInfoComponent;
  let fixture: ComponentFixture<AddRiderInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRiderInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRiderInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
