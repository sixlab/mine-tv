package tech.minesoft.minetv.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.databinding.ActivityHomeBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ScrollViewUtils;
import tech.minesoft.minetv.widget.ImageBlock;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private List<MineMovieInfo> infoList;
    private boolean star = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            star = bundle.getBoolean(Const.ACTIVITY_STAR, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (star) {
            binding.toolbar.setTitle(R.string.title_star);
            binding.toolbar.focusStar();
            infoList = DaoHelper.loadStar();
        } else {
            binding.toolbar.setTitle(R.string.title_main);
            infoList = DaoHelper.loadAll();
        }

        LinearLayout vodList = binding.content.vodList;
        vodList.removeAllViews();
        ScrollViewUtils.addBlock(this, vodList, infoList, this::onKey);
    }

    private void onKey(ImageBlock block, MineMovieInfo info) {
        block.setOnKeyListener((view, keycode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keycode == KeyEvent.KEYCODE_MENU) {
                if (star) {
                    new AlertDialog.Builder(this)
                            .setMessage("是否取消收藏？")
                            .setNegativeButton("确定", (dialog, id) -> {
                                DaoHelper.changeStar(info.getId());
                                loadData();
                            })
                            .setPositiveButton("取消", null)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("是否删除记录？")
                            .setNegativeButton("确定", (dialog, id) -> {
                                DaoHelper.delInfo(info.getId());
                                loadData();
                            })
                            .setPositiveButton("取消", null)
                            .show();
                }
                return true;
            }
            return false;
        });
    }
}