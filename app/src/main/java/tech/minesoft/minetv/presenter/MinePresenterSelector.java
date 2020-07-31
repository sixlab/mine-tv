package tech.minesoft.minetv.presenter;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import tech.minesoft.minetv.utils.Const;

public class MinePresenterSelector extends PresenterSelector {
    private final ListRowPresenter listRowPresenter = new ListRowPresenter();
    private final FooterPresenter footerPresenter = new FooterPresenter();
    private final PagePresenter pagePresenter = new PagePresenter();
    private final SearchPresenter searchPresenter = new SearchPresenter();

    public MinePresenterSelector() {
        listRowPresenter.setShadowEnabled(false);
        listRowPresenter.setSelectEffectEnabled(false);
        listRowPresenter.setKeepChildForeground(false);
    }

    @Override
    public Presenter getPresenter(Object item) {
        if (item instanceof Selector) {
            Selector selector = (Selector) item;

            switch (selector.type) {
                case Const.PRESENTER_SEARCH:
                    return searchPresenter;
                case Const.PRESENTER_PAGER:
                    return pagePresenter;
                case Const.PRESENTER_FOOTER:
                    return footerPresenter;

                default:
                    return listRowPresenter;
            }
        } else {
            return listRowPresenter;
        }
    }

    @Override
    public Presenter[] getPresenters() {
        return super.getPresenters();
    }

    public static class Selector {
        private String type;
        private Object item;

        public Selector(String type, Object item) {
            this.type = type;
            this.item = item;
        }

        public static Selector newInstance(String type, Object item) {
            return new Selector(type, item);
        }

        public Object getItem() {
            return item;
        }
    }
}
