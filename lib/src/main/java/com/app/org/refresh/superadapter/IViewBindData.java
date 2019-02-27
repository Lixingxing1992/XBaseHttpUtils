package com.app.org.refresh.superadapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create and bind data to item view.
 * <p>
 * Created by Cheney on 16/3/31.
 */
interface IViewBindData<T, VH> {

    /**
     * @param convertView Support by {@link ListSupportAdapter#getView(int, View, ViewGroup)}.
     * @param parent      Target container(ListView, GridView, RecyclerView,Spinner, etc.).
     * @param viewType    Choose the layout resource according to view type.
     * @return Created view holder.
     */
    VH onCreate(@Nullable View convertView, ViewGroup parent, int viewType);

    /**
     * Method for binding data to view.
     *
     * @param helper         ViewHolder
     * @param viewType       {@link RecyclerSupportAdapter#getItemViewType(int)}
     * @param layoutPosition position
     * @param item           data
     */
    void onBind(VH helper, int viewType, int layoutPosition, T item);
}
