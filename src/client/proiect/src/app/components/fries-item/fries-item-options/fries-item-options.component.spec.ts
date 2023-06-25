import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriesItemOptionsComponent } from './fries-item-options.component';

describe('FriesItemOptionsComponent', () => {
  let component: FriesItemOptionsComponent;
  let fixture: ComponentFixture<FriesItemOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriesItemOptionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FriesItemOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
