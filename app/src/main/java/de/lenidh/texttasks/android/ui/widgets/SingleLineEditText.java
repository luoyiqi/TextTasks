package de.lenidh.texttasks.android.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

public class SingleLineEditText extends EditText {

    public SingleLineEditText(Context context) {
        super(context);
    }

    public SingleLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SingleLineEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        if(filters == null) {
            throw new IllegalArgumentException(); // parent behaviour
        }
        // append NewlineFilter
        InputFilter[] extended = new InputFilter[filters.length + 1];
        System.arraycopy(filters, 0, extended, 0, filters.length);
        extended[filters.length] = NewlineFilter.INSTANCE;
        super.setFilters(extended);
    }

    private enum NewlineFilter implements InputFilter {
        INSTANCE;

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return source.toString().replaceAll("\\r\\n|\\r|\\n", " ");
        }
    }
}
