package tech.minesoft.minetv.v3app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.v3app.bean.MineSiteInfo;
import tech.minesoft.minetv.v3app.greendao.V3DaoHelper;
import tech.minesoft.minetv.v3app.widget.ScaleConstraintLayout;


public class SiteFragment extends Fragment {

    private EditText codeText;
    private EditText siteText;
    private ScaleConstraintLayout addSite;
    private TextView siteDefault;
    private TableLayout siteContainer;

    public static Fragment newInstance() {
        return new SiteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site, container, false);

        codeText = view.findViewById(R.id.et_code);
        siteText = view.findViewById(R.id.et_site);
        addSite = view.findViewById(R.id.btn_add_site);
        siteDefault = view.findViewById(R.id.tv_site_default);
        siteContainer = view.findViewById(R.id.urls_container);

        addSite.setOnClickListener(v -> {
            String code = codeText.getText().toString();
            String site = siteText.getText().toString();

            MineSiteInfo siteInfo = V3DaoHelper.getSite(code);

            if (null == siteInfo) {
                siteInfo = new MineSiteInfo();
                siteInfo.setName(code);
                siteInfo.setCode(code);
                siteInfo.setPrimary(0);
                siteInfo.setStatus(1);
            }

            siteInfo.setUrl(site);
            V3DaoHelper.saveSiteInfo(siteInfo);

            codeText.setText("");
            siteText.setText("");

            renderUi();
        });

        renderUi();

        return view;
    }

    private void renderUi(){
        MineSiteInfo primarySite = V3DaoHelper.getPrimarySite();
        if (primarySite == null) {
            siteDefault.setText(getString(R.string.text_default_text_null));
        } else {
            siteDefault.setText(getString(R.string.text_default_text, primarySite.getCode()));
        }

        siteContainer.removeAllViews();

        List<MineSiteInfo> activeSites = V3DaoHelper.getActiveSites();
        for (MineSiteInfo site : activeSites) {
            TableRow tableRow = new TableRow(getContext());

            // 列，删除按钮
            ScaleConstraintLayout deleteBtn = new ScaleConstraintLayout(getContext());
            TextView tvDelete = new TextView(getContext());
            tvDelete.setText(getString(R.string.tv_site_delete));
            tvDelete.setTextColor(getResources().getColor(R.color.bl_red));
            deleteBtn.addView(tvDelete);
            deleteBtn.setOnClickListener(v -> {
                V3DaoHelper.delSite(site.getId());
                renderUi();
            });
            tableRow.addView(deleteBtn);

            // 列，默认按钮
            ScaleConstraintLayout primaryBtn = new ScaleConstraintLayout(getContext());
            TextView primaryInfo = new TextView(getContext());
            if(site.getPrimary() == 1){
                primaryInfo.setText("取消默认");
                primaryInfo.setTextColor(getResources().getColor(R.color.split_line));
            }else{
                primaryInfo.setText("设置默认");
                primaryInfo.setTextColor(getResources().getColor(R.color.bl_pink));
            }
            primaryBtn.addView(primaryInfo);
            primaryBtn.setOnClickListener(v -> {
                V3DaoHelper.updatePrimary(site.getId());
                renderUi();
            });

            // //layoutParams 设置margin值
            // ScaleConstraintLayout.LayoutParams layoutParams = new ScaleConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            //         ViewGroup.LayoutParams.WRAP_CONTENT);
            // int space = (int) (getResources().getDimension(R.dimen.btn_row_margin) + 0.5f);
            // layoutParams.setMargins(space, 0, space, 0);
            // primaryBtn.setLayoutParams(layoutParams);

            tableRow.addView(primaryBtn);

            // 列，网址
            TextView tvInfo = new TextView(getContext());
            tvInfo.setText("【" + site.getCode() + "】" + site.getUrl());
            tvInfo.setTextColor(getResources().getColor(R.color.colorWhite));
            tableRow.addView(tvInfo);

            siteContainer.addView(tableRow);
        }
    }


    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

}
