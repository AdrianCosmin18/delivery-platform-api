import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressUpdateFormComponent } from './address-update-form.component';

describe('AddressUpdateFormComponent', () => {
  let component: AddressUpdateFormComponent;
  let fixture: ComponentFixture<AddressUpdateFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressUpdateFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddressUpdateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
