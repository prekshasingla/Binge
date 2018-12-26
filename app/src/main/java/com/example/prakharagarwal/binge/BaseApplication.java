package com.example.prakharagarwal.binge;

import android.app.Application;

import com.example.prakharagarwal.binge.cart.AppEnvironment;
import com.onesignal.OneSignal;

/**
 * Created by prakharagarwal on 29/07/18.
 */

public class BaseApplication extends Application {

    AppEnvironment appEnvironment;
    boolean cartFlag=true;

    public boolean isCartFlag() {
        return cartFlag;
    }

    public void setCartFlag(boolean cartFlag) {
        this.cartFlag = cartFlag;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appEnvironment = AppEnvironment.SANDBOX;
        if(!Config.isDevelopmentMode) {
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();
        }
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}
