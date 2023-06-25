import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomTipsComponent } from './custom-tips.component';

describe('CustomTipsComponent', () => {
  let component: CustomTipsComponent;
  let fixture: ComponentFixture<CustomTipsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomTipsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CustomTipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
