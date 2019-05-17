package com.kunminx.linkagelistview.ui;
/*
 * Copyright (c) 2018-2019. KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;
import com.kunminx.linkagelistview.R;
import com.kunminx.linkagelistview.bean.ElemeGroupedItem;
import com.kunminx.linkagelistview.databinding.FragmentYoumiBinding;

import java.util.List;

/**
 * Create by KunMinX at 19/5/8
 */
public class YoumiStoreSampleFragment extends Fragment {

    private static final int SPAN_COUNT_FOR_GRID_MODE = 2;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;
    private FragmentYoumiBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youmi, container, false);
        mBinding = FragmentYoumiBinding.bind(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLinkageDatas(mBinding.linkage);
        mBinding.btnPreview.setOnClickListener(v -> {
            mBinding.linkage.setGridMode(!mBinding.linkage.isGridMode());
        });
    }

    private void initLinkageDatas(LinkageRecyclerView linkage) {
        Gson gson = new Gson();
        List<ElemeGroupedItem> items = gson.fromJson(getString(R.string.eleme_json),
                new TypeToken<List<ElemeGroupedItem>>() {
                }.getType());

        linkage.init(items, new ILinkagePrimaryAdapterConfig() {

            private Context mContext;

            public void setContext(Context context) {
                mContext = context;
            }

            @Override
            public int getLayoutId() {
                return com.kunminx.linkage.R.layout.default_adapter_linkage_level_primary;
            }

            @Override
            public int getGroupTitleViewId() {
                return com.kunminx.linkage.R.id.tv_group;
            }

            @Override
            public int getRootViewId() {
                return com.kunminx.linkage.R.id.layout_group;
            }

            @Override
            public void onBindViewHolder(LinkagePrimaryViewHolder holder, String title, int position) {
                ((TextView) holder.mGroupTitle).setText(title);
            }

            @Override
            public void onItemSelected(boolean selected, View itemView) {
                TextView textView = (TextView) itemView;
                textView.setBackgroundColor(mContext.getResources().getColor(
                        selected ? com.kunminx.linkage.R.color.colorPurple : com.kunminx.linkage.R.color.colorWhite));
                textView.setTextColor(ContextCompat.getColor(mContext,
                        selected ? com.kunminx.linkage.R.color.colorWhite : com.kunminx.linkage.R.color.colorGray));
                textView.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
                textView.setFocusable(selected);
                textView.setFocusableInTouchMode(selected);
                textView.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
            }

        }, new ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo>() {

            private Context mContext;

            public void setContext(Context context) {
                mContext = context;
            }

            @Override
            public int getGridLayoutId() {
                return R.layout.adapter_eleme_secondary_grid;
            }

            @Override
            public int getLinearLayoutId() {
                return R.layout.adapter_eleme_secondary_linear;
            }

            @Override
            public int getHeaderLayoutId() {
                return com.kunminx.linkage.R.layout.default_adapter_linkage_level_secondary_header;
            }

            @Override
            public int getSpanCountOfGridMode() {
                return SPAN_COUNT_FOR_GRID_MODE;
            }

            @Override
            public void onBindViewHolder(LinkageSecondaryViewHolder holder,
                                         BaseGroupedItem<ElemeGroupedItem.ItemInfo> item, int position) {

                ((TextView) holder.getView(R.id.iv_goods_name)).setText(item.info.getTitle());
                Glide.with(mContext).load(item.info.getImgUrl()).into((ImageView) holder.getView(R.id.iv_goods_img));
                holder.getView(R.id.iv_goods_item).setOnClickListener(v -> {
                    //TODO
                });

                holder.getView(R.id.iv_goods_add).setOnClickListener(v -> {
                    //TODO
                });
            }

            @Override
            public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                               BaseGroupedItem<ElemeGroupedItem.ItemInfo> item, int position) {

                ((TextView) holder.getView(R.id.level_2_header)).setText(item.header);
            }
        });
    }
}
