package com.touchgfx.mvvm.base.widget.segmented

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class SelectedItemState : View.BaseSavedState, Parcelable {

    var selectedItem = 0

    constructor(superState: Parcel) : super(superState) {
        selectedItem = superState.readInt()
    }

    constructor(source: Parcelable?) : super(source)

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(selectedItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SelectedItemState> {
        override fun createFromParcel(parcel: Parcel): SelectedItemState {
            return SelectedItemState(parcel)
        }

        override fun newArray(size: Int): Array<SelectedItemState?> {
            return arrayOfNulls(size)
        }
    }
}