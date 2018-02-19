package com.aevi.sdk.pos.flow.model;


import android.content.ComponentName;

import com.aevi.sdk.flow.model.BaseModel;

public abstract class BasePaymentModel extends BaseModel {

    private String targetPaymentAppComponent;
    private String componentName;

    public BasePaymentModel(String id) {
        super(id);
    }

    public ComponentName getTargetPaymentAppComponent() {
        if (targetPaymentAppComponent != null) {
            return ComponentName.unflattenFromString(targetPaymentAppComponent);
        }
        return null;
    }

    public void setTargetPaymentAppComponent(ComponentName targetPaymentAppComponent) {
        if (targetPaymentAppComponent != null) {
            this.targetPaymentAppComponent = targetPaymentAppComponent.flattenToString();
        }
    }

    public String getComponentName() {
        return componentName;
    }

    public ComponentName getComponentNameValue() {
        if (componentName != null) {
            return ComponentName.unflattenFromString(componentName);
        }
        return null;
    }

    public void setComponentName(ComponentName componentName) {
        if (componentName != null) {
            this.componentName = componentName.flattenToString();
        }
    }
}
