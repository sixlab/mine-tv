package tech.minesoft.minetv.v4app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tech.minesoft.minetv.v4app.R;
import tech.minesoft.minetv.v4app.bean.MineChannel;
import tech.minesoft.minetv.v4app.bean.MineSiteInfo;
import tech.minesoft.minetv.v4app.databinding.ActivitySettingBinding;
import tech.minesoft.minetv.v4app.greendao.DaoHelper;
import tech.minesoft.minetv.v4app.utils.LayoutUtils;
import tech.minesoft.minetv.v4app.widget.SourceDialog;
import tech.minesoft.minetv.v4app.widget.TextButton;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_setting);

        binding.content.btnAddSource.setOnClickListener(this::clickAddSource);

        binding.content.btnAddSource.requestFocus();
    }

    private void clickAddSource(View view) {
        SourceDialog dialog = new SourceDialog(this);
        dialog.setOnOkListener(view1 -> {
            String code = dialog.inputCode.getText().toString();
            String url = dialog.inputUrl.getText().toString();
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(url)) {
                Toast.makeText(SettingActivity.this, "请输入编号和接口地址", Toast.LENGTH_SHORT).show();
            } else {
                DaoHelper.updateSite(code, url, 0);
                dialog.cancel();
                loadSource();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadSource();

        loadChannel();

        binding.content.btnAddSource.requestFocus();
    }

    private void loadSource() {
        LinearLayout mineSource = binding.content.mineSource;
        int childCount = mineSource.getChildCount();
        if (childCount > 2) {
            mineSource.removeViews(2, childCount - 2);
        }

        List<MineSiteInfo> siteInfoList = DaoHelper.getActiveSites();
        for (MineSiteInfo siteInfo : siteInfoList) {
            TextButton btn = new TextButton(this);
            btn.setLayoutParams(LayoutUtils.btnLayout);
            btn.setText(siteInfo.getCode());
            btn.setOnClickListener(view -> {
                Button dialogBtn = new AlertDialog.Builder(this)
                        .setMessage("请选择操作！")
                        .setNeutralButton("删除", (dialog, id) -> {
                            DaoHelper.delSite(siteInfo.getId());
                            loadSource();
                        })
                        .setNegativeButton("设为默认", (dialog, id) -> {
                            DaoHelper.updatePrimary(siteInfo.getCode());
                            loadSource();
                        })
                        .setPositiveButton("取消", null)
                        .show().getButton(DialogInterface.BUTTON_NEGATIVE);

                if (dialogBtn != null) dialogBtn.requestFocus();
            });
            btn.setOnKeyListener((view, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_MENU) {
                    Toast.makeText(SettingActivity.this, siteInfo.getUrl(), Toast.LENGTH_SHORT).show();
                }

                return false;
            });
            if (1 == siteInfo.getPrimary()) {
                btn.setNormalColor(R.color.mtv_viewed);
            }
            mineSource.addView(btn);
        }

        binding.content.btnAddSource.requestFocus();
    }

    private void loadChannel() {
        loadActive();
        loadExclude();

        binding.content.mineChannel.requestFocus();
    }
    private void loadActive() {
        LinearLayout mineChannel = binding.content.mineChannel;
        int childCount = mineChannel.getChildCount();
        if (childCount > 1) {
            mineChannel.removeViews(1, childCount - 1);
        }

        List<MineChannel> channels = DaoHelper.statusChannels(1);
        for (MineChannel channel : channels) {
            TextButton btn = new TextButton(this);
            btn.setLayoutParams(LayoutUtils.btnLayout);
            btn.setText(channel.getName());

            btn.setOnClickListener(view -> {
                Button dialogBtn = new AlertDialog.Builder(this)
                        .setMessage("请选择操作！")
                        .setNeutralButton("删除", (dialog, id) -> {
                            DaoHelper.delChannel(channel.getId());

                            loadChannel();
                        })
                        .setNegativeButton("隐藏", (dialog, id) -> {
                            channel.setStatus(0);
                            DaoHelper.updateChannel(channel);

                            loadChannel();
                        })
                        .setPositiveButton("取消", null)
                        .show().getButton(DialogInterface.BUTTON_NEGATIVE);

                if (dialogBtn != null) dialogBtn.requestFocus();
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

        List<MineChannel> excludeList = DaoHelper.statusChannels(0);
        for (MineChannel channel : excludeList) {
            TextButton btn = new TextButton(this);
            btn.setLayoutParams(LayoutUtils.btnLayout);
            btn.setText(channel.getName());
            btn.setOnClickListener(view -> {
                Button dialogBtn = new AlertDialog.Builder(this)
                        .setMessage("是否取消隐藏？")
                        .setNegativeButton("确定", (dialog, id) -> {
                            channel.setStatus(1);
                            DaoHelper.updateChannel(channel);

                            loadChannel();
                        })
                        .setPositiveButton("取消", null)
                        .show().getButton(DialogInterface.BUTTON_NEGATIVE);

                if (dialogBtn != null) dialogBtn.requestFocus();
            });
            mineExclude.addView(btn);
        }
    }
}