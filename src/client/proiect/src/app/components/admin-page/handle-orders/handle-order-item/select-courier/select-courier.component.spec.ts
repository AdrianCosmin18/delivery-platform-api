import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectCourierComponent } from './select-courier.component';

describe('SelectCourierComponent', () => {
  let component: SelectCourierComponent;
  let fixture: ComponentFixture<SelectCourierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectCourierComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SelectCourierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
