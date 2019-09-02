package com.cnsunrun.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.LoginUtil;
import com.sunrun.sunrunframwork.adapter.ImagePagerAdapter;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.weight.AutoScrollViewPager;
import com.unistrong.yang.zb_permission.ZbPermission;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.USER_OAUTH_TOKEN_CODE;


/**
 * 启动页(引导页)
 */
public class SplashActivity extends LBaseActivity {

    private static final String TAG = "SplashActivity";
    int startNum = 0;
    //开始的时候是2000    改成1s了
    long time = 500;
    static SplashActivity act;
    AHandler.Task task;
    @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;
    @BindView(R.id.start)
    View start;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        act = this;
        startNum = Integer.parseInt(Config.getConfigInfo(this, Config.START_NUM, "0"));// 启动次数
        Config.putConfigInfo(this, Config.START_NUM, String.valueOf(startNum + 1));// 启动次数加1
        if (startNum <= 0 && hasStartImg()) {
            try {
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
                String[] bgs = getAssets().list(Config.START_IMG);
                for (int i = 0; i < bgs.length; i++) {
                    bgs[i] = Config.START_IMG + "/" + bgs[i];
                }
                viewPager.setAdapter(new ImagePagerAdapter<String>(this, Arrays.asList(bgs)) {
                            @Override
                            protected void loadImage(@NonNull ImageView imageView, @NonNull String s) {
                            }

                            @Override
                            public View getView(int index, View view, ViewGroup container) {
                                View view1 = super.getView(index, view, container);
                                ImageView imageView = (ImageView) view1.findViewById(R.id.img);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                return view1;
                            }
                        }.setInfiniteLoop(false)
                                .setOnBannerClickListener(new ImagePagerAdapter.OnBannerClickListener() {
                                    @Override
                                    public void onBannerClick(int position, Object t) {
                                        if (viewPager.getAdapter() != null && viewPager.getCurrentItem() != viewPager.getAdapter().getCount() - 1) {
                                            return;
                                        }
                                        startNavigatorActivity(null);
                                        //startLogin(null);
                                    }
                                })
                )
                ;
            } catch (IOException e) {
                e.printStackTrace();
            }
            AHandler.runTask(task = new AHandler.Task() {
                @Override
                public void update() {
//                    startLogin(null);
                    requestToken();
                }
            }, time);
        } else {

            AHandler.runTask(task = new AHandler.Task() {
                @Override
                public void update() {
//                    startLogin(null);
                    requestToken();
                }
            }, time);
        }
    }




    public static SplashActivity getSplash() {
        return act;
    }

    public static void close() {
        if (act != null) {
            if (act.task == null) {
                act.finish();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        AHandler.cancel(task);
        act = null;
    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
        super.loadFaild(requestCode, bean);
        UIUtils.shortM(bean);
    }

    public void startNavigatorActivity(View view) {
        //request()方法的参数可以有也可以没有，有且不为空，就会回调ZbPermissionCallback的响应的回调方法，没有或为空，则
        //回调响应的注解方法
        ZbPermission.with(that)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                .request(new ZbPermission.ZbPermissionCallback() {
                    @Override
                    public void permissionSuccess(int requestCode) {
//                        startLogin(null);
//                        finish();
                        requestToken();
//                                Toast.makeText(MainActivity.this, "成功授予Contact权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionFail(int requestCode) {
//                                Toast.makeText(MainActivity.this, "成功授予Contact拍照权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void requestToken() {
        if (!Config.getLoginInfo().isValid()) {
            LoginUtil.exitLogin();
            CommonIntent.startLoginActivity();
            return;
        }
        UIUtils.showLoadDialog(that);
        BaseQuestStart.UserOauthToken(this);
    }


    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        if (requestCode == USER_OAUTH_TOKEN_CODE) {
            if (bean.status == 1) {
                LoginInfo.TokenBean tokenBean = bean.Data();//刷新token
                Config.getLoginInfo().setToken(tokenBean);
                startLogin(null);
            } else {
                LoginUtil.exitLogin();
                CommonIntent.startLoginActivity();
            }
        }
    }


    public void startLogin(View view) {
        //已经登录过就直接进入主界面
        if (Config.getLoginInfo().isValid()) {
            CommonIntent.startChatNavigatorActivity(this);
            finish();
        } else {
            CommonIntent.startLoginActivity(this);
            finish();
        }
    }


    @OnClick({R.id.start})
    public void onClick(View view) {
        if (viewPager.getAdapter() != null &&
                viewPager.getCurrentItem() != viewPager.getAdapter().getCount() - 1) {
            return;
        }
        switch (view.getId()) {
            case R.id.start:
                startNavigatorActivity(null);
                break;
        }

    }

    boolean hasStartImg() {
        try {
            String[] bgs = getAssets().list(Config.START_IMG);
            return bgs != null && bgs.length > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
