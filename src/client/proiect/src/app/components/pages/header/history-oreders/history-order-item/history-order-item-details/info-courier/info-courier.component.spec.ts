import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoCourierComponent } from './info-courier.component';

describe('InfoCourierComponent', () => {
  let component: InfoCourierComponent;
  let fixture: ComponentFixture<InfoCourierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InfoCourierComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InfoCourierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
