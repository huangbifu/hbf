package com.cnsunrun.chat;

import android.os.Bundle;
import android.view.View;


import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.commonui.widget.edittext.EditView;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_REMARK_CODE;


/**
 * 咵天-设置备注信息
 */
public class ChatAddRemarkActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.editRemark)
    EditView editRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_addremark);
        editRemark.setText(getIntent().getStringExtra("remark"));
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showLoadDialog(that,"保存中...");
//                BaseQuestStart.NeighborhoodImFriendRemark(that,getIntent().getStringExtra("uid"),editRemark.getText());

            }
        });
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_FRIEND_REMARK_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

}
