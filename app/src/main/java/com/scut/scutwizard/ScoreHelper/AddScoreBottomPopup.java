package com.scut.scutwizard.ScoreHelper;


import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.xpopup.core.BottomPopupView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.annotation.AssertTrue;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rey.material.widget.Button;
import com.scut.scutwizard.Helpers.FileHelper;
import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.ScoreImage.LocalMediaDbUtil;
import com.scut.scutwizard.ScoreHelper.ScoreImage.PhotoSelectFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.z2wenfa.spinneredittext.SpinnerEditText;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author MinutesSneezer
 * Create: 2019/12/7
 * Update: 2019/12/8
 */

public abstract class AddScoreBottomPopup extends BottomPopupView implements
        Validator.ValidationListener {
    private static final String[]     DESC_HINTS  = new String[]{"灾后清扫",
                                                                 "参加了什么竞赛?",
                                                                 "发表一篇核心期刊论文",
                                                                 "试图理解一篇核心期刊论文",
                                                                 "扶老奶奶过马路...?",
                                                                 "设计共享单车",
                                                                 "使用共享单车",
                                                                 "优秀共青团员",
                                                                 "接见瑞典环保大使"};
    private static final List<String> DT_EXAMPLES = Arrays.asList("班级;院级;校级;区级;市级;省级;国家级;世界级".split(
            ";"));

    @NotEmpty(messageResId = R.string.form_necessary, sequence = 1)
    @Length(max = 20, messageResId = R.string.no_more_than_20_words, trim = true, sequence = 2)
    @Order(1)
    private EditText iDesc;

    @NotEmpty(messageResId = R.string.form_necessary, sequence = 1)
    @Digits(integer = 2, sequence = 2, messageResId = R.string.please_input_number)
    @AssertTrue(sequence = 3, messageResId = R.string.cannot_be_zero)
    @DecimalMax(value = 21., messageResId = R.string.score_value_too_high, sequence = 4)
    @DecimalMin(value = -60., messageResId = R.string.score_value_too_negative, sequence = 5)
    @Order(2)
    private EditText iValue;

    //    @NotEmpty(messageResId = R.string.form_necessary)
    private Spinner sCat;// 归属

    @NotEmpty(messageResId = R.string.form_necessary)
    @Order(3)
    private SpinnerEditText<String> iDet;// 细类

    private EditText            iPs;//备注
    private TextView            iDate;
    private PhotoSelectFragment fPhoto;

    private Button    bConfirm;
    private Context   mContext;
    private Validator validator;

    private Calendar mEventCalendar;

    public AddScoreBottomPopup(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.add_score_bottom_popup;
    }

    abstract public int fetchTabPos();

    abstract public FragmentManager fetchFragManager();

    private void refreshEventDate() {
        iDate.setText(DateFormat.format("yyyy-M-d", mEventCalendar));
    }

    abstract public int fetchSubtable();

    @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
    @Override
    protected void onCreate() {
        super.onCreate();
        Random random = new Random();
        iDesc = findViewById(R.id.popup_desc_input);
        iDesc.setHint(DESC_HINTS[random.nextInt(DESC_HINTS.length)]);

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
        iDet.setHint(DT_EXAMPLES.get(random.nextInt(DT_EXAMPLES.size())));
        iDet.setList(DT_EXAMPLES);
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
                dpd.setLocale(Locale.getDefault());
                dpd.setCancelText(mContext.getString(R.string.calendar_nevermind));
                dpd.setOkText(mContext.getString(R.string.calendar_ok));
                dpd.setFirstDayOfWeek(Calendar.MONDAY);

                Calendar mCa = Calendar.getInstance(); //now
                mCa.add(Calendar.MONTH, 2); // max +2month
                dpd.setMaxDate(mCa);
                mCa = Calendar.getInstance();
                mCa.add(Calendar.YEAR, -1); // min -1year
                dpd.setMinDate(mCa);

                dpd.show(Objects.requireNonNull(fetchFragManager()), "DatePickerDialog");
            }
        });

        FragmentManager fm = fetchFragManager();
        if (fm != null) {
            fPhoto = (PhotoSelectFragment) fm.findFragmentById(R.id.popup_img_frag);
        }

        iPs = findViewById(R.id.popup_ps_input);
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(SpinnerEditText.class, new ViewDataAdapter<SpinnerEditText, String>() {
            @Override
            public String getData(SpinnerEditText spinnerEditText) {
                return spinnerEditText.getValue();
            }
        });
//        validator.registerAdapter(Spinner.class, view -> view.getSelectedItem().toString());
        validator.registerAdapter(EditText.class, new ViewDataAdapter<EditText, Boolean>() {
            @Override
            public Boolean getData(EditText view) {
                try {
                    return Double.valueOf(view.getText().toString()) != 0;
                } catch (Exception e) {
                    return true;
                }
            }
        });
        bConfirm = findViewById(R.id.popup_confirm_btn);
        bConfirm.setOnClickListener(view -> validator.validate());
    }

    abstract protected void insertScores(ArrayList<Score> scores);

    @Override
    public void onValidationSucceeded() {
        String description = iDesc.getText().toString().trim();
        int category = sCat.getSelectedItemPosition();
        double value = Double.valueOf(iValue.getText().toString());
        final Date now = new Date();
        Date eventDate = mEventCalendar.getTime();
        String specificCategory = iDet.getValue();
        String comment = iPs.getText().toString().trim();
        int subtable = fetchSubtable();

        String filenameStr = "";
        List<LocalMedia> images = fPhoto.getSelectList();
        if (!images.isEmpty()) {
            ArrayList<String> filenames = new ArrayList<>();
            LocalMediaDbUtil lmHelper = new LocalMediaDbUtil(mContext);
            final File DATA_DIR = lmHelper.getDataDir();
            for (LocalMedia img : images) {
                try {
                    // src file
                    File f = new File(img.getCompressPath());

                    // dest file
                    File tmp = File.createTempFile(String.format(Locale.getDefault(),
                                                                 "%d_",
                                                                 new Date().getTime()),
                                                   ".jpeg",
                                                   DATA_DIR);
//                final String TMP_PATH = tmp.getPath();
                    final String TMP_FILENAME = tmp.getName();

//                Files.copy(Paths.get(img.getCompressPath()), Paths.get(TMP_PATH));
                    if (!FileHelper.copy(f, tmp))
                        throw new IOException();
                    filenames.add(TMP_FILENAME);
                } catch (IOException e) {
                    Toast.makeText(mContext, "读取文件出现问题，请大侠重新来过", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
//            filenameStr = StringHelper.join(";", filenames);
            filenameStr = TextUtils.join(";", filenames);
        }

        Score score = new Score();
        score.setDescription(description);
        score.setCategory(category);
        score.setValue(value);
        score.setCreateDate(now);
        score.setLastModifiedDate(now);
        score.setEventDate(eventDate);
        score.setSpecificCategory(specificCategory);
        score.setComment(comment);
        score.setSubtable(subtable);
        score.setImagePaths(filenameStr);

        ArrayList<Score> tmpArr = new ArrayList<>();
        tmpArr.add(score);
        insertScores(tmpArr);
        destroyPopup();
    }


    public void destroyPopup() {
        this.dismiss();
        if (fPhoto != null) {
            FragmentManager fragmentManager = fetchFragManager();
            if (fragmentManager != null && !fragmentManager.isDestroyed()) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fPhoto).commit();
                fPhoto = null;
            }
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            final View view = error.getView();
            final String message = error.getCollatedErrorMessage(mContext);

            // Display first line of every error message
            if (view instanceof EditText) {
                final EditText et = (EditText) view;
                if (et.getError() == null)
                    et.setError(message.split("\\s")[0]);
            }
        }
    }
}
