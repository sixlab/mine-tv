package tech.minesoft.minetv.presenter;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import tech.minesoft.minetv.v2.bean.Footer;
import tech.minesoft.minetv.widget.FooterPresenter;
import tech.minesoft.minetv.widget.ListRowPresenter;

public class MinePresenterSelector extends PresenterSelector {
    private final ListRowPresenter listRowPresenterOne = new ListRowPresenter();
    private final FooterPresenter footerPresenter = new FooterPresenter();

    public MinePresenterSelector() {
        listRowPresenterOne.setShadowEnabled(false);
        listRowPresenterOne.setSelectEffectEnabled(false);
        listRowPresenterOne.setKeepChildForeground(false);
    }

    @Override
    public Presenter getPresenter(Object item) {
        if(item instanceof Footer){
            return footerPresenter;
        }
        return listRowPresenterOne;
    }

    @Override
    public Presenter[] getPresenters() {
        return super.getPresenters();
    }
}
