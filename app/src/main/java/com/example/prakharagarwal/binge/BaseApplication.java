package com.example.prakharagarwal.binge;

import android.app.Application;

import com.example.prakharagarwal.binge.cart.AppEnvironment;

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


    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}
