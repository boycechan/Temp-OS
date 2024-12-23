package com.contrarywind.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * @author chenxiangbo
 * @company TouchGFX
 * @date 2021/6/7 10:55
 * @desc NoLabel
 */
public class NoneLabel implements IPickerViewData {
    private String text;

    public NoneLabel(String text) {
        this.text = text;
    }

    @Override
    public String getPickerViewText() {
        return text;
    }

    @Override
    public String toString() {
        return text != null ? text : "";
    }
}
