package com.cnsunrun.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.commonui.widget.edittext.EditView;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_NOTICE_CODE;


/**
 * 咵天-修改群公告
 */
public class ChatModifyGroupNoticeActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.editContent)
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_modify_group_notice);
        editContent.setText(getIntent().getStringExtra("content"));
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doModify();
            }
        });
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_GROUP_NOTICE_CODE:
                if(bean.status==1){
                    finish();
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }
    private void doModify() {
        UIUtils.showLoadDialog(that,"修改中..");
        BaseQuestStart.NeighborhoodImGroupNotice(this,getIntent().getStringExtra("id"),editContent);
    }

}
