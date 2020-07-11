package tech.minesoft.minetv;

import android.app.Application;

import tech.minesoft.minetv.data.DbHelper;

public class MineApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DbHelper.init(getApplicationContext());

        // Stetho.initializeWithDefaults(this);

        // DTInstaller.install(this)
        //         .setBlockCanary(new AppBlockCanaryContext(this))
        //         .setOkHttpClient(RequestHelper.client)
        //         // .setInjector("your.package.injector.ContentInjector")
        //         // .setPackageName("your.package")
        //         .enable()
        //         .run();
    }
}
