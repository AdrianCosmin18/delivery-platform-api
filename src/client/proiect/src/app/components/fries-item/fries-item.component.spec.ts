import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriesItemComponent } from './fries-item.component';

describe('FriesItemComponent', () => {
  let component: FriesItemComponent;
  let fixture: ComponentFixture<FriesItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriesItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FriesItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
