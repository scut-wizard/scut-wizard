package com.scut.scutwizard.ScoreHelper;


import android.content.Context;
import android.text.format.DateFormat;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lxj.xpopup.core.BottomPopupView;
import com.scut.scutwizard.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.z2wenfa.spinneredittext.SpinnerEditText;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class AddScoreBottomPopup extends BottomPopupView {
    private EditText iDesc, iValue;
    private Spinner                 sCat;
    private Context                 mContext;
    private SpinnerEditText<String> iDet;
    private TextView                iDate;

    private Calendar mEventCalendar;

    public AddScoreBottomPopup(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.add_score_bottom_popup;
    }

    public int fetchTabPos() {
        return 0;
    }

    @Nullable
    public FragmentManager fetchFragManager() {
        return null;
    }

    private void refreshEventDate() {
        iDate.setText(DateFormat.format("yyyy-M-d", mEventCalendar));
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        iDesc = findViewById(R.id.popup_desc_input);
        iValue = findViewById(R.id.popup_value_input);

        sCat = findViewById(R.id.popup_cat_spn);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(mContext,
                                                             android.R.layout.simple_list_item_1,
                                                             new String[]{mContext.getString(R.string.deyu),
                                                                          mContext.getString(R.string.zhiyu),
                                                                          mContext.getString(R.string.wenti)});
        sCat.setAdapter(catAdapter);
        sCat.setSelection(fetchTabPos());
        sCat.setDropDownVerticalOffset(30);

        iDet = findViewById(R.id.popup_det_input);
        List<String> detailExamples = Arrays.asList("班级;院级;校级;区级;市级;省级;国家级;世界级".split(";"));
        iDet.setList(detailExamples);
        iDet.setNeedShowSpinner(true);

        iDate = findViewById(R.id.popup_date_lbl);
        mEventCalendar = Calendar.getInstance();
        refreshEventDate();
        iDate.setOnClickListener(view -> {
            if (fetchFragManager() != null) {
                // hide ime
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);


                int mYear = mEventCalendar.get(Calendar.YEAR);
                int mMonth = mEventCalendar.get(Calendar.MONTH);
                int mDay = mEventCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = DatePickerDialog.newInstance((DatePickerDialog view1, int year, int monthOfYear, int dayOfMonth) -> {
                    mEventCalendar.set(year, monthOfYear, dayOfMonth);
                    refreshEventDate();
                }, mYear, mMonth, mDay);
                dpd.setLocale(Locale.CHINA);
                dpd.setCancelText("👋算了");
                dpd.setOkText("🉑以了");
                dpd.setFirstDayOfWeek(Calendar.MONDAY);

                Calendar mCa = Calendar.getInstance(); //now
                mCa.add(Calendar.MONTH, 2);
                dpd.setMaxDate(mCa);
                mCa = Calendar.getInstance();
                mCa.add(Calendar.YEAR, -1);
                dpd.setMinDate(mCa);

                dpd.show(Objects.requireNonNull(fetchFragManager()), "DatePickerDialog");
            }
        });

    }


    @Override
    protected void onShow() {
        super.onShow();
    }
}
