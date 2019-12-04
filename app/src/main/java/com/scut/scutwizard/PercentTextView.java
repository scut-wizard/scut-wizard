package com.scut.scutwizard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;

public final class PercentTextView extends TextView {
    private static int baseScreenHeight = 1920;
    private float mTextSizePercent = 1f;

    public PercentTextView(Context context) {
        super(context);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    public PercentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        setDefaultPercent(context);
        setTextSize(getTextSize());
    }

    /**
     * 获取当前设备屏幕的宽高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    @Override
    public void setTextSize(int unit, float size) {
        size = (int) (size * mTextSizePercent);
        super.setTextSize(unit, size);
    }

    @Override
    public void setPaintFlags(int flags) {
        super.setPaintFlags(flags);
    }

    @Override
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public float getTextSizePercent() {
        return mTextSizePercent;
    }

    public void setTextSizePercent(float textSizePercent) {
        mTextSizePercent = textSizePercent;
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
    }

    public void setTextSizePercent(int unit, float textSizePercent) {
        mTextSizePercent = textSizePercent;
        setTextSize(unit, getTextSize());
    }

    /**
     * 得到自定义的属性值
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PercentTextView);
        baseScreenHeight = ta.getInt(R.styleable.PercentTextView_baseScreenHeight, baseScreenHeight);
        ta.recycle();
    }

    /**
     * 设置默认的百分比
     *
     * @param context
     */
    private void setDefaultPercent(Context context) {
        float screenHeight = getScreenHeight(context);
        mTextSizePercent = screenHeight / baseScreenHeight;
    }
}
