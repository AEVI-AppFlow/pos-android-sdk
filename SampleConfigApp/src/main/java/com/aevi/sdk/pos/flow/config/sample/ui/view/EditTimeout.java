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
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.config.sample.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class EditTimeout extends LinearLayout {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.summary)
    TextView summary;

    @BindView(R.id.timeout_value)
    EditText timeoutValue;

    @BindView(R.id.value_suffix)
    TextView valueSuffix;

    private Timer timer = new Timer();
    private final long DELAY = 500;
    private PublishSubject<Integer> changedSubject = PublishSubject.create();

    public EditTimeout(Context context) {
        super(context);
        init(context, null);
    }

    public EditTimeout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTimeout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EditTimeout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View v = View.inflate(context, R.layout.edit_timeout, this);
        ButterKnife.bind(this, v);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTimeout, 0, 0);
            title.setText(a.getString(R.styleable.EditTimeout_editTitle));
            summary.setText(a.getString(R.styleable.EditTimeout_editSummary));
            valueSuffix.setText(a.getString(R.styleable.EditTimeout_editSuffix));
            a.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        changedSubject.onComplete();
    }

    public void setMinMax(int timeoutMin, int timeoutMax) {
        timeoutValue.setFilters(new InputFilter[]{new InputFilterMinMax(timeoutMin, timeoutMax)});

        timeoutValue.setMaxEms(String.valueOf(timeoutMax).length());
    }

    public void setInitialValue(int timeout) {
        timeoutValue.setText(String.valueOf(timeout));
        timeoutValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                notifyChange();
            }
        });
    }

    private void notifyChange() {
        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!timeoutValue.getText().toString().isEmpty()) {
                    changedSubject.onNext(Integer.valueOf(timeoutValue.getText().toString()));
                }
            }
        }, DELAY);
    }

    public Observable<Integer> subscribeToValueChanges() {
        return changedSubject;
    }
}
