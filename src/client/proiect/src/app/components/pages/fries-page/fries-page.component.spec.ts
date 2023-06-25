import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriesPageComponent } from './fries-page.component';

describe('FriesPageComponent', () => {
  let component: FriesPageComponent;
  let fixture: ComponentFixture<FriesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriesPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FriesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
