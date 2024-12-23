package com.touchgfx.calendarview;

import java.util.Calendar;

public interface DatePickerController {

    Calendar getMinDay();

    int getMaxYear();

    void onDayOfMonthSelected(int year, int month, int day);

    void onDateRangeSelected(final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);

}