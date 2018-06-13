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

package com.aevi.sdk.pos.flow.paymentinitiationsample.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aevi.sdk.pos.flow.paymentinitiationsample.R;
import com.aevi.ui.library.views.JsonPrettyView;

import butterknife.BindView;

public class JsonDisplayFragment extends BaseFragment {

    private static final String TAG = JsonDisplayFragment.class.getSimpleName();
    private static final String ARG_TITLE = "title";
    private static final String ARG_JSON = "json";

    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.json)
    JsonPrettyView jsonPrettyView;

    private String title;
    private String json;

    public static JsonDisplayFragment create(String title, String json) {
        JsonDisplayFragment frag = new JsonDisplayFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TITLE, title);
        b.putString(ARG_JSON, json);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_json_display;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_JSON)) {
            title = args.getString(ARG_TITLE);
            json = args.getString(ARG_JSON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        titleView.setText(title);
        jsonPrettyView.load(json);
        return v;
    }
}
