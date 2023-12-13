import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { NgbDateStruct, NgbCalendar, NgbDate } from '@ng-bootstrap/ng-bootstrap';

const equals = (one: NgbDateStruct, two: NgbDateStruct) =>
  one && two && two.year === one.year && two.month === one.month && two.day === one.day;

const before = (one: NgbDateStruct, two: NgbDateStruct) =>
  !one || !two
    ? false
    : one.year === two.year
    ? one.month === two.month
      ? one.day === two.day
        ? false
        : one.day < two.day
      : one.month < two.month
    : one.year < two.year;

const after = (one: NgbDateStruct, two: NgbDateStruct) =>
  !one || !two
    ? false
    : one.year === two.year
    ? one.month === two.month
      ? one.day === two.day
        ? false
        : one.day > two.day
      : one.month > two.month
    : one.year > two.year;

@Component({
  selector: 'ngb-multi-date-picker',
  templateUrl: 'multi-date-picker.html',
  styles: [
    `
      .custom-day {
        margin: 0 1px;
        width: 40px !important;
        height: 40px !important;
        color: #004671;
        background: transparent;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50% !important;
        font-size: 14px;
      }

      .custom-day.range,
      .custom-day:hover {
        background-color: #f2f3f4;
        outline: 1px solid #dadce0;
      }

      .custom-day.faded {
        background-color: #4285f4 !important;
        opacity: 0.5;
      }

      .custom-day.selected {
        background-color: #4285f4;
        color: white !important;
      }
    `,
  ],
})
export class MultiDatePicker implements OnInit {
  hoveredDate: any;

  @Input() fromDateReq: any;
  @Input() toDateReq: any;
  @Output() datesSelectedChange = new EventEmitter<NgbDateStruct[]>();
  @Output() unSelected = new EventEmitter<boolean>();

  fromDate: any;
  toDate: any;

  _datesSelected: NgbDateStruct[] = [];
  datesDisabled: NgbDateStruct[] = [];
  monthsDisabled: number[] = [];
  // onlyMonths: number[] = [];

  ngOnInit() {}

  @Input()
  set datesSelected(value: NgbDateStruct[]) {
    this._datesSelected = value;
  }

  @Input()
  set dateUnSelected(requestUnSelected: any) {
    if (requestUnSelected) {
      const { indexUnSelected, itemUnSelected, itemDisabled, onlyMonths } = requestUnSelected;
      if (indexUnSelected && indexUnSelected.length > 0) {
        if (indexUnSelected.length === 1) {
          this._datesSelected.splice(indexUnSelected[0], 1);
        } else {
          for (let i = 0; i < indexUnSelected.length; i++) {
            this._datesSelected.splice(0, 1);
          }
        }
      }
      if (itemUnSelected && itemUnSelected.length > 0) {
        for (let item of itemUnSelected) {
          const index = this.datesSelected.findIndex(find => find.day === item.day && find.month === item.month && find.year === item.year);
          if (index >= 0) {
            this.datesSelected.splice(index, 1);
          }
        }
      }
      if (itemDisabled && itemDisabled.length > 0) {
        itemDisabled.forEach(item => this.monthsDisabled.push(item.month));
        this.datesDisabled = itemDisabled;
      } else {
        this.monthsDisabled = [];
      }
      // if (onlyMonths && onlyMonths.length > 0) {
      //   this.onlyMonths = onlyMonths;
      // }
    }
  }

  isDisabledDate = (date: NgbDateStruct): boolean => {
    // if (this.onlyMonths && this.onlyMonths.length > 0) {
    //   return !this.onlyMonths.includes(date.month);
    // }
    return !!this.datesDisabled.find(x => new NgbDate(x.year, x.month, x.day).equals(date));
  };

  get datesSelected(): NgbDateStruct[] {
    return this._datesSelected ? this._datesSelected : [];
  }

  constructor(calendar: NgbCalendar) {}

  onDateSelection(event: any, date: NgbDateStruct) {
    event.target.parentElement.blur(); //make that not appear the outline
    if (!this.fromDate && !this.toDate) {
      if (event.ctrlKey == true)
        //If is CrtlKey pressed
        this.fromDate = date;
      else this.addDate(date);

      this.datesSelectedChange.emit(this.datesSelected);
    } else if (this.fromDate && !this.toDate && after(date, this.fromDate)) {
      this.toDate = date;
      this.addRangeDate(this.fromDate, this.toDate);
      this.fromDate = null;
      this.toDate = null;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  addDate(date: NgbDateStruct) {
    let index = this.datesSelected.findIndex(f => f.day == date.day && f.month == date.month && f.year == date.year);
    if (index >= 0)
      //If exist, remove the date
      this.datesSelected.splice(index, 1);
    //a simple push
    else this.datesSelected.push(date);
  }

  addRangeDate(fromDate: NgbDateStruct, toDate: NgbDateStruct) {
    //We get the getTime() of the dates from and to
    let from = new Date(fromDate.year + '-' + fromDate.month + '-' + fromDate.day).getTime();
    let to = new Date(toDate.year + '-' + toDate.month + '-' + toDate.day).getTime();
    for (
      let time = from;
      time <= to;
      time += 24 * 60 * 60 * 1000 //add one day
    ) {
      let date = new Date(time);
      //javascript getMonth give 0 to January, 1, to February...
      this.addDate({ year: date.getFullYear(), month: date.getMonth() + 1, day: date.getDate() });
    }
    this.datesSelectedChange.emit(this.datesSelected);
  }

  //return true if is selected
  isDateSelected(date: NgbDateStruct) {
    return this.datesSelected.findIndex(f => f.day == date.day && f.month == date.month && f.year == date.year) >= 0;
  }

  isHovered = date => this.fromDate && !this.toDate && this.hoveredDate && after(date, this.fromDate) && before(date, this.hoveredDate);
  isInside = date => after(date, this.fromDate) && before(date, this.toDate);
  isFrom = date => equals(date, this.fromDate);
  isTo = date => equals(date, this.toDate);
}
