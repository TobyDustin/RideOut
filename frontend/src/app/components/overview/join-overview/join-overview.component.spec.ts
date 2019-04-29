import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinOverviewComponent } from './join-overview.component';

describe('JoinOverviewComponent', () => {
  let component: JoinOverviewComponent;
  let fixture: ComponentFixture<JoinOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JoinOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JoinOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
