package com.scut.scutwizard.ScoreHelper;


import android.content.Context;
import android.os.Build;
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
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.rey.material.widget.Button;
import com.scut.scutwizard.R;
import com.scut.scutwizard.ScoreHelper.ScoreImage.LocalMediaDbUtil;
import com.scut.scutwizard.ScoreHelper.ScoreImage.PhotoFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.z2wenfa.spinneredittext.SpinnerEditText;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.StringJoiner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    @NotEmpty(message = "必填")
    @Length(max = 20, message = "不能超过20字!", trim = true)
    @Order(1)
    private              EditText     iDesc;
    @NotEmpty(message = "必填", sequence = 1)
    @DecimalMax(value = 21., message = "加太多啦", sequence = 2)
    @DecimalMin(value = -60., message = "扣太多啦", sequence = 3)
    @Order(2)
    private              EditText     iValue;

    //    @NotEmpty(message = "必选")
    private Spinner sCat;// 归属

    @NotEmpty(message = "必填")
    @Order(3)
    private SpinnerEditText<String> iDet;// 细类

    private EditText      iPs;//备注
    private TextView      iDate;
    private PhotoFragment fPhoto;

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
                dpd.setLocale(Locale.CHINA);
                dpd.setCancelText("👋算了");
                dpd.setOkText("🉑以了");
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
            fPhoto = (PhotoFragment) fm.findFragmentById(R.id.popup_img_frag);
        }

        iPs = findViewById(R.id.popup_ps_input);
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.registerAdapter(SpinnerEditText.class, SpinnerEditText::getValue);
//        validator.registerAdapter(Spinner.class, view -> view.getSelectedItem().toString());
        bConfirm = findViewById(R.id.popup_confirm_btn);
        bConfirm.setOnClickListener(view -> validator.validate());
    }

    abstract protected void insertScores(ArrayList<Score> scores);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onValidationSucceeded() {
        String description = iDesc.getText().toString().trim();
        int category = sCat.getSelectedItemPosition();
        double value = Double.valueOf(iValue.getText().toString());
        Date now = new Date();
        Date eventDate = mEventCalendar.getTime();
        String specificCategory = iDet.getValue();
        String comment = iPs.getText().toString().trim();
        int subtable = fetchSubtable();

        List<LocalMedia> images = fPhoto.getSelectList();
        StringJoiner sj = new StringJoiner(";");
        LocalMediaDbUtil lmHelper = new LocalMediaDbUtil(mContext);
        final File DATA_DIR = lmHelper.getDataDir();
        for (LocalMedia img : images) {
            try {
//                File f = new File(img.getCompressPath());
                File tmp = File.createTempFile("", ".jpeg", DATA_DIR);
                final String TMP_PATH = tmp.getPath();
                Files.copy(Paths.get(img.getCompressPath()), Paths.get(TMP_PATH));
                final String TMP_FILENAME = tmp.getName();
                sj.add(TMP_FILENAME);
//                long size = f.length();
//                if (size > Integer.MAX_VALUE) {
//                    Toast.makeText(mContext, "文件过大!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                FileInputStream fs = new FileInputStream(f);
//                byte[] buffer = new byte[(int) size];
//                int offset = 0;
//                int numRead;
//                while (offset < buffer.length
//                       && (numRead = fs.read(buffer, offset, buffer.length - offset)) >= 0)
//                    offset += numRead;
//                if (offset != buffer.length)
//                    throw new IOException();
//                fs.close();
            } catch (IOException e) {
                Toast.makeText(mContext, "读取文件出现问题，请大侠重新来过", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }
        }
        final String filenames = sj.toString();

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
        score.setImagePaths(filenames);

        ArrayList<Score> tmpArr = new ArrayList<>();
        tmpArr.add(score);
        insertScores(tmpArr);
        destroyMyself();
    }


    public void destroyMyself() {
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
