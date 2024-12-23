package com.touchgfx.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author chenxiangbo
 * @company TouchGFX
 * @date 2021/3/29 17:00
 * @desc TouchElxCalendarView
 */
public class TouchElxCalendarView extends FrameLayout {

    private View mRooView;
    private DayPickerView mDayPickerView;

    public TouchElxCalendarView(Context context) {
        this(context, null);
    }

    public TouchElxCalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchElxCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRooView = inflate(getContext(), R.layout.touchelx_calendarview, this);
        mDayPickerView = mRooView.findViewById(R.id.day_picker);
    }

    public void setController(DatePickerController controller) {
        mDayPickerView.setController(controller);
    }

    public DatePickerController getController() {
        return mDayPickerView.getController();
    }

    public void showCurentDay() {
        mDayPickerView.scrollToPosition(mDayPickerView.getAdapter().getItemCount() - 1);
    }
}
