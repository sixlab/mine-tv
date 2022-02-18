package tech.minesoft.minetv.v5app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import tech.minesoft.minetv.v5app.greendao.DaoMaster;
import tech.minesoft.minetv.v5app.greendao.DaoOpenHelper;
import tech.minesoft.minetv.v5app.utils.Const;
import tech.minesoft.minetv.v5app.utils.CrashHandler;
import tech.minesoft.minetv.v5app.utils.Holder;

public class MineApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        setupDatabase();

        Init.init(context);

        // Stetho.initializeWithDefaults(this);
        //
        // DTInstaller.install(this)
        //         .setBlockCanary(new BlockCanaryContext(this))
        //         .setOkHttpClient(RequestHelper.client)
        //         // .setInjector("your.package.injector.ContentInjector")
        //         // .setPackageName("your.package")
        //         .enable()
        //         .run();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(context);
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库
        DaoOpenHelper helper = new DaoOpenHelper(this, Const.DATABASE_NAME);

        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();

        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);

        //获取Dao对象管理者
        Holder.daoSession = daoMaster.newSession();
    }

}
