package tech.minesoft.minetv.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.databinding.ActivityHomeBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ScrollViewUtils;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private List<MineMovieInfo> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        boolean star = false;

        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            star = bundle.getBoolean(Const.ACTIVITY_STAR, false);
        }

        if (star) {
            binding.toolbar.setTitle(R.string.title_star);
            binding.toolbar.focusStar();
            infoList = DaoHelper.loadStar();
        } else {
            binding.toolbar.setTitle(R.string.title_main);
            infoList = DaoHelper.loadAll();
        }

        HomeActivity mContext = this;

        ScrollViewUtils.addBlock(this, binding.content.vodList, infoList, info -> view -> {
            long infoId = DaoHelper.saveInfo(info);

            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Const.SELECT_MOVIE_ID, infoId);
            intent.putExtra(Const.SELECT_MOVIE_NAME, info.getVod_name());
            mContext.startActivity(intent);
        });
    }

}