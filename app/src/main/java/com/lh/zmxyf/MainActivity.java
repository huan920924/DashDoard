package com.lh.zmxyf;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.ddv_am_view)
    DashDoardView ddvAmView;
    @InjectView(R.id.sb_am_proportion)
    SeekBar sbAmProportion;
    @InjectView(R.id.sb_am_top_textsize)
    SeekBar sbAmTopTextsize;
    @InjectView(R.id.sb_am_bottom_textsize)
    SeekBar sbAmBottomTextsize;
    @InjectView(R.id.sb_am_progress)
    SeekBar sbAmProgress;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;
    @InjectView(R.id.sb_am_pointer_margin)
    SeekBar sbAmPointerMargin;
    @InjectView(R.id.sb_am_start_angle)
    SeekBar sbAmStartAngle;
    @InjectView(R.id.sb_am_cus_text)
    EditText sbAmCusText;
    @InjectView(R.id.spinner_am_interpolator)
    Spinner spinnerAmInterpolator;
    @InjectView(R.id.sb_am_value1)
    SeekBar sbAmValue1;
    @InjectView(R.id.sb_am_value2)
    SeekBar sbAmValue2;
    @InjectView(R.id.sb_am_top_margin)
    SeekBar sbAmTopMargin;
    @InjectView(R.id.sb_am_bottom_margin)
    SeekBar sbAmBottomMargin;
    // 数据源
    private String[] list = {
            "OvershootInterpolator",
            "BounceInterpolator",
            "AccelerateDecelerateInterpolator",
            "AccelerateInterpolator",
            "AnticipateInterpolator",
            "AnticipateOvershootInterpolator",
            "CycleInterpolator(0.8f)",
            "DecelerateInterpolator",
            "LinearInterpolator",
            "FastOutLinearInInterpolator",
            "FastOutSlowInInterpolator"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initListener();
    }

    private void initListener() {
        sbAmProportion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmProportion((float) (progress * 1.0 / 10f));
                Log.e("progress", "" + progress);
                Log.e("(float)progress", "" + (float) (progress * 1.0 / 10f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmTopTextsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmFractionTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmBottomTextsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setCustomTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.startAnimator(progress, true, 5000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbAmPointerMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmPointorOffset(progress * 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmStartAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmStartAngle(progress * 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmCusText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ddvAmView.setmCustomStr(s.toString());
            }
        });
        spinnerAmInterpolator.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        spinnerAmInterpolator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ddvAmView.setAnimatorStyle(new OvershootInterpolator());
                        break;
                    case 1:
                        ddvAmView.setAnimatorStyle(new BounceInterpolator());
                        break;
                    case 2:
                        ddvAmView.setAnimatorStyle(new AccelerateDecelerateInterpolator());
                        break;
                    case 3:
                        ddvAmView.setAnimatorStyle(new AccelerateInterpolator());
                        break;
                    case 4:
                        ddvAmView.setAnimatorStyle(new AnticipateInterpolator());
                        break;
                    case 5:
                        ddvAmView.setAnimatorStyle(new AnticipateOvershootInterpolator());
                        break;
                    case 6:
                        ddvAmView.setAnimatorStyle(new CycleInterpolator(0.8f));
                        break;
                    case 7:
                        ddvAmView.setAnimatorStyle(new DecelerateInterpolator());
                        break;
                    case 8:
                        ddvAmView.setAnimatorStyle(new LinearInterpolator());
                        break;
                    case 9:
                        ddvAmView.setAnimatorStyle(new FastOutLinearInInterpolator());
                        break;
                    case 10:
                        ddvAmView.setAnimatorStyle(new FastOutSlowInInterpolator());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sbAmValue1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmDashEffectValueOne((float) (progress * 1.0 / 10f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmValue2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmDashEffectValueTwo((float) (progress * 1.0 / 10f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmTopMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                ddvAmView.setmFractionMarginTop(progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAmBottomMargin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ddvAmView.setmCustomMarginTop(progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ;
    }


}
