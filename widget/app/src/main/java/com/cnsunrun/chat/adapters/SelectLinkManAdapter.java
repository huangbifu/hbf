package com.cnsunrun.chat.adapters;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.util.selecthelper.Selectable;
import com.cnsunrun.common.util.selecthelper.SelectableLBaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WQ
 * 联系人列表
 */
public class SelectLinkManAdapter extends SelectableLBaseAdapter<LinkManInfo,BaseViewHolder> {
    private SideBarSortMode sideBarSortMode;
    private boolean isSingle;
    public SelectLinkManAdapter(SideBarSortMode sideBarSortMode,boolean isSingle) {
        super(R.layout.item_chat_select_linkman);
        this.sideBarSortMode=sideBarSortMode;
        this.isSingle=isSingle;
        selectMode(isSingle?RADIO:MULTISELECT);
//        selectMode(MULTISELECT);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setSelectPosition(position);
                onItemSelected(position,getAllCheckData());
            }
        });
    }


    @Override
    protected void convert(BaseViewHolder helper, LinkManInfo item) {
        final int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.avatar);
        String sortLetterTitle = getSortLetterTitle(layoutPosition);
        helper.setText(R.id.txtGroupTitle,sortLetterTitle);
        helper.setText(R.id.txtUserName,item.toString());
        helper.setVisible(R.id.txtGroupTitle,sortLetterTitle!=null);
        helper.setChecked(R.id.cbSelect,isSelected(layoutPosition));
        helper.setVisible(R.id.cbSelect,!isSingle);
        helper.setOnClickListener(R.id.cbSelect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectPosition(layoutPosition);
                onItemSelected(layoutPosition,getAllCheckData());
            }
        });
    }

    private String getSortLetterTitle(int layoutPosition) {
        if(sideBarSortMode==null)return null;
        return sideBarSortMode.getSortLetterTitle(layoutPosition);
    }

    public void   onItemSelected(int position, List<LinkManInfo>selectedData){

   }

    public List<LinkManInfo> getAllCheckData(List<LinkManInfo> sourceData) {
        List<LinkManInfo> tmpData = new ArrayList<>();
        if (sourceData != null) {
            for (int i = 0; i < sourceData.size(); i++) {
                LinkManInfo item = sourceData.get(i);
                if (isSelected(item)) {
                    tmpData.add(item);
                }
            }
        }
        return tmpData;
    }
}