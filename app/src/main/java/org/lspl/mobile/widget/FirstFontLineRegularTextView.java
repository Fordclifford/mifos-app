package org.lspl.mobile.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* renamed from: com.murongtech.ui.widget.FirstFontLineRegularTextView */
public class FirstFontLineRegularTextView extends FirstFontRegularTextView {
    public FirstFontLineRegularTextView(@NonNull Context context) {
        super(context);
        if (!isInEditMode()) {
            getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public FirstFontLineRegularTextView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public FirstFontLineRegularTextView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        if (!isInEditMode()) {
            getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}
