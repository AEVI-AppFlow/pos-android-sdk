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
package com.aevi.sdk.pos.flow.config.sample.flowapps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.aevi.sdk.app.scanning.AppInfoProvider;
import com.aevi.sdk.app.scanning.model.AppInfoModel;
import com.aevi.sdk.pos.flow.config.sample.model.AppType;
import com.aevi.sdk.flow.model.config.FlowApp;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfo;
import com.aevi.sdk.pos.flow.model.PaymentFlowServiceInfoBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.aevi.sdk.flow.constants.FlowStages.TRANSACTION_PROCESSING;

@Singleton
public class AppDatabase implements AppInfoProvider {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private BehaviorSubject<List<AppInfoModel>> modelStream = BehaviorSubject.create();

    private List<AppInfoModel> inMemModels = new ArrayList<>();

    private final Context context;

    @Inject
    public AppDatabase(Context context) {
        this.context = context;
    }

    @NonNull
    public List<AppInfoModel> getAll() {
        return inMemModels;
    }

    @NonNull
    public List<AppInfoModel> filterByType(AppType filterAppType) {
        List<AppInfoModel> filtered = new ArrayList<>();
        for (AppInfoModel model : inMemModels) {
            boolean isTxnProcessing = model.getStages().contains(TRANSACTION_PROCESSING);
            if ((isTxnProcessing && filterAppType == AppType.PAYMENT || filterAppType == AppType.VAA && !isTxnProcessing)) {
                filtered.add(model);
            }
        }
        return filtered;
    }

    public void save(AppInfoModel appEntity, boolean notify) {
        Log.d(TAG, "Saving app: " + appEntity.getPaymentFlowServiceInfo().getPackageName());
        AppInfoModel currentEntity = findApp(appEntity.getPaymentFlowServiceInfo().getPackageName());
        if (currentEntity != null) {
            inMemModels.remove(currentEntity);
        }
        inMemModels.add(appEntity);
        if (notify) {
            notifySubscribers();
        }
    }

    public void save(AppInfoModel appEntity) {
        save(appEntity, true);
    }

    public void clearApps() {
        AppInfoModel[] models = inMemModels.toArray(new AppInfoModel[inMemModels.size()]);
        for (AppInfoModel model : models) {
            inMemModels.remove(model);
        }
    }

    public void notifySubscribers() {
        modelStream.onNext(inMemModels);
    }

    public Observable<List<AppInfoModel>> subscribeToAppUpdates() {
        return modelStream;
    }

    public List<AppInfoModel> getAppInfoModelsForFlowApps(List<FlowApp> flowApps) {
        List<AppInfoModel> filtered = new ArrayList<>();
        for (FlowApp app : flowApps) {
            AppInfoModel model = findApp(app.getId());
            if (model != null) {
                filtered.add(model);
            } else {
                PaymentFlowServiceInfo fsi = new PaymentFlowServiceInfoBuilder()
                        .withVendor("Unknown")
                        .withDisplayName(app.getId())
                        .build(context);
                filtered.add(new AppInfoModel(new HashMap<>(), fsi));
            }
        }
        return filtered;
    }

    private AppInfoModel findApp(String id) {
        for (AppInfoModel model : inMemModels) {
            if (model.getPaymentFlowServiceInfo().getPackageName().equals(id)) {
                return model;
            }
        }
        return null;
    }

    @Override
    public boolean isKnownApp(String packageName) {
        return findApp(packageName) != null;
    }
}
