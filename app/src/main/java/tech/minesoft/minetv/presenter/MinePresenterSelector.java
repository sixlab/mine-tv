package tech.minesoft.minetv.presenter;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import tech.minesoft.minetv.fragment.SearchFragment;
import tech.minesoft.minetv.bean.Footer;

public class MinePresenterSelector extends PresenterSelector {
    private final ListRowPresenter listRowPresenterOne = new ListRowPresenter();
    private final FooterPresenter footerPresenter = new FooterPresenter();
    private final SearchPresenter searchPresenter = new SearchPresenter();

    public MinePresenterSelector() {
        listRowPresenterOne.setShadowEnabled(false);
        listRowPresenterOne.setSelectEffectEnabled(false);
        listRowPresenterOne.setKeepChildForeground(false);
    }

    @Override
    public Presenter getPresenter(Object item) {
        if(item instanceof Footer){
            return footerPresenter;
        }else if (item instanceof SearchFragment) {
            return searchPresenter;
        }
        return listRowPresenterOne;
    }

    @Override
    public Presenter[] getPresenters() {
        return super.getPresenters();
    }

}
