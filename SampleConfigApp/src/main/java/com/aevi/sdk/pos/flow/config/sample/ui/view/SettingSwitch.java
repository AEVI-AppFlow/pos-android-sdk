/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aevi.sdk.pos.flow.config.sample.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.config.sample.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class SettingSwitch extends LinearLayout {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.summary)
    TextView summary;

    @BindView(R.id.value)
    SwitchCompat value;

    private static final long DELAY = 500;

    private Timer timer = new Timer();
    private PublishSubject<Boolean> changedSubject;

    public SettingSwitch(Context context) {
        super(context);
        init(context, null);
    }

    public SettingSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SettingSwitch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SettingSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        changedSubject = PublishSubject.create();
        View v = View.inflate(context, R.layout.setting_switch, this);
        ButterKnife.bind(this, v);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingSwitch, 0, 0);
            title.setText(a.getString(R.styleable.SettingSwitch_switchTitle));
            summary.setText(a.getString(R.styleable.SettingSwitch_switchSummary));
            value.setChecked(a.getBoolean(R.styleable.SettingSwitch_switchDefaultValue, false));
            a.recycle();
        }

        // use parent as clickable for toggle
        v.setClickable(true);
        v.setOnClickListener(v1 -> {
            value.toggle();
            notifyChange();
        });
        value.setClickable(false);
    }

    public void setChecked(boolean checked) {
        value.setChecked(checked);
    }

    private void notifyChange() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                changedSubject.onNext(value.isChecked());
            }
        }, DELAY);
    }

    public Observable<Boolean> subscribeToValueChanges() {
        return changedSubject;
    }
}