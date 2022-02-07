package tech.minesoft.minetv.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.databinding.ActivitySettingBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.LayoutUtils;
import tech.minesoft.minetv.widget.TextButton;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    private void loadData() {
        loadSource();

        loadChannel();

        loadExclude();
    }

    private void loadSource() {
        LinearLayout mineSource = binding.content.mineSource;
        int childCount = mineSource.getChildCount();
        if (childCount > 2) {
            mineSource.removeViews(2, childCount - 2);
        }

        List<MineSiteInfo> siteInfoList = DaoHelper.getActiveSites();
        boolean first = true;
        for (MineSiteInfo siteInfo : siteInfoList) {
            TextButton btn = new TextButton(this);
            if (first) {
                first = false;
            } else {
                btn.setLayoutParams(LayoutUtils.btnLayout);
            }
            btn.setText(siteInfo.getCode());
            btn.setOnClickListener(view -> {
                DaoHelper.updatePrimary(siteInfo.getCode());
                loadData();
            });
            btn.setOnFocusChangeListener((view, focus) -> {
                if(focus){
                    Toast.makeText(SettingActivity.this, siteInfo.getUrl(), Toast.LENGTH_SHORT).show();
                }
            });
            if (1 == siteInfo.getPrimary()) {
                btn.setNormalColor(R.color.mtv_viewed);
            }
            mineSource.addView(btn);
        }
    }

    private void loadChannel() {
        LinearLayout mineChannel = binding.content.mineChannel;
        int childCount = mineChannel.getChildCount();
        if (childCount > 1) {
            mineChannel.removeViews(1, childCount - 1);
        }

        String channels = DaoHelper.meta(Const.PLAY_CHANNELS);
        String[] channelList = TextUtils.split(channels, ",");
        boolean first = true;
        for (String channelText : channelList) {
            TextButton btn = new TextButton(this);
            if (first) {
                first = false;
            } else {
                btn.setLayoutParams(LayoutUtils.btnLayout);
            }
            btn.setText(channelText);

            btn.setOnClickListener(view -> {
                new AlertDialog.Builder(this)
                        .setMessage("请选择操作！")
                        .setNeutralButton("删除", (dialog, id) -> {
                            String val = channels.replace("," + channelText + ",", ",");
                            DaoHelper.updateMeta(Const.PLAY_CHANNELS, val);

                            loadData();
                        })
                        .setNegativeButton("排除", (dialog, id) -> {
                            String excludes = DaoHelper.meta(Const.PLAY_EXCLUDES);
                            if(TextUtils.isEmpty(excludes)){
                                excludes = ",";
                            }
                            excludes = excludes + channelText + ",";

                            DaoHelper.updateMeta(Const.PLAY_EXCLUDES, excludes);

                            loadData();
                        })
                        .setPositiveButton("取消", null)
                        .show();
            });

            mineChannel.addView(btn);
        }
    }

    private void loadExclude() {
        LinearLayout mineExclude = binding.content.mineExclude;
        int childCount = mineExclude.getChildCount();
        if (childCount > 1) {
            mineExclude.removeViews(1, childCount - 1);
        }

        String excludes = DaoHelper.meta(Const.PLAY_EXCLUDES);
        String[] excludeList = TextUtils.split(excludes, ",");
        boolean first = true;
        for (String excludeText : excludeList) {
            TextButton btn = new TextButton(this);
            if (first) {
                first = false;
            } else {
                btn.setLayoutParams(LayoutUtils.btnLayout);
            }
            btn.setText(excludeText);
            btn.setOnClickListener(view -> {
                new AlertDialog.Builder(this)
                        .setMessage("是否删除？")
                        .setNegativeButton("确定", (dialog, id) -> {
                            String val = excludes.replace("," + excludeText + ",", ",");
                            DaoHelper.updateMeta(Const.PLAY_EXCLUDES, val);

                            loadData();
                        })
                        .setPositiveButton("取消", null)
                        .show();
            });
            mineExclude.addView(btn);
        }
    }
}