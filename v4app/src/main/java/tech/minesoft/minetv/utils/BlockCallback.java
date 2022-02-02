package tech.minesoft.minetv.utils;

import android.view.View;

public interface BlockCallback<T> {
    View.OnClickListener click(T info);
}
