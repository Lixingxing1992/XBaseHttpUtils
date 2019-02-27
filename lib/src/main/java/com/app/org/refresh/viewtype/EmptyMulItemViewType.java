package com.app.org.refresh.viewtype;

import com.app.org.R;
import com.app.org.refresh.superadapter.IMulItemViewType;
import com.app.org.refresh.superadapter.SuperAdapter;

/**
 * Convenient class for RecyclerView.Adapter.
 * <p>
 * Created by Cheney on 16/4/5.
 *
 * 使用该类的时候，adapter中需要设置
 *  public int getItemCount() {
 return super.getItemCount() == 0 ? 1 : super.getItemCount();
 }
 */
public class EmptyMulItemViewType<T> implements IMulItemViewType<T> {
    int layoutId;
    public EmptyMulItemViewType(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public int getItemViewType(int position, T t) {
        if(t == null){
            return SuperAdapter.emptyViewType;
        }else{
            return 0;
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        return viewType == 0 ? layoutId : R.layout.default_empty;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
