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
package com.aevi.sdk.pos.flow.config.sample.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseFragment extends Fragment {

    public abstract int getLayoutResource();

    private List<Disposable> disposables;
    private List<Subscription> subscriptions;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    protected <T> Flowable<T> observe(Flowable<T> observable) {
        return observable.doOnSubscribe(subscription -> {
            if (subscriptions == null) {
                subscriptions = new ArrayList<>();
            }
            subscriptions.add(subscription);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> Single<T> observe(Single<T> observable) {
        return observable.doOnSubscribe(disposable -> {
            if (disposables == null) {
                disposables = new ArrayList<>();
            }
            disposables.add(disposable);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    protected <T> Observable<T> observe(Observable<T> observable) {
        return observable.doOnSubscribe(disposable -> {
            if (disposables == null) {
                disposables = new ArrayList<>();
            }
            disposables.add(disposable);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    protected boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    protected boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    protected boolean isHighRes() {
        Configuration configuration = getResources().getConfiguration();
        return configuration.screenWidthDp >= 600;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (disposables != null) {
            for (Disposable disposable : disposables) {
                if (disposable != null) {
                    disposable.dispose();
                }
            }
            disposables.clear();
        }

        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                if (subscription != null) {
                    subscription.cancel();
                }
            }
            subscriptions.clear();
        }
    }
}
