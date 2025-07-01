import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Barrio } from './barrio';

describe('Barrio', () => {
  let component: Barrio;
  let fixture: ComponentFixture<Barrio>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Barrio]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Barrio);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
