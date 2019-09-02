package com.cnsunrun.common.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.cnsunrun.R;
import com.cnsunrun.common.model.AreaEntity;
import com.cnsunrun.common.model.ChooseEntity;
import com.cnsunrun.common.widget.CustomWheelDatePicker;
import com.cnsunrun.common.widget.wheel.WheelAreaPicker;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.http.utils.DateUtil;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sunrun.sunrunframwork.utils.EmptyDeal.empty;


/**
 * 选择器帮助类
 */
public class ChooserHelper {
    Dialog dialog;
    public WheelDatePicker datePicker;
    public WheelAreaPicker areaPicker;
    public WheelPicker valPicker;

    public OnselectValListener getOnselectValListener() {
        return onselectValListener;
    }

    public void setOnselectValListener(OnselectValListener onselectValListener) {
        this.onselectValListener = onselectValListener;
    }

    public OnselectValListener onselectValListener;

    public OnselectListener onselectListener;

    public void setOnselectListener(OnselectListener onselectListener) {
        this.onselectListener = onselectListener;
    }

    //回调省市区
    public interface OnselectListener {
        void onSelectListener(String provinceid, String cityid, String area);
    }

    /**
     * 显示日期的选择器
     *
     * @param context
     * @param textView
     * @param listener
     * @return
     */
    public ChooserHelper showDateChooser(Context context, final TextView textView, final CustomWheelDatePicker.OnDateSelectedListener listener) {
        bulidDateDialog(context, textView, listener);
        return this;
    }

    /**
     * 创建日期选择器
     *
     * @param context
     * @param textView
     * @param listener
     * @return
     */
    Dialog bulidDateDialog(Context context, final TextView textView, final CustomWheelDatePicker.OnDateSelectedListener listener) {
        if (dialog == null) {
            final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_select_time, null);
            dialog = UIUtils.createDialog(context, dialogView, true);
            final CustomWheelDatePicker wheelPicker = (CustomWheelDatePicker) dialogView.findViewById(R.id.wheel_date_picker);
            Date tmpDate = (Date) textView.getTag();
            if (tmpDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tmpDate);
                wheelPicker.setYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                wheelPicker.setSelectedDay(calendar.get(Calendar.DAY_OF_MONTH));
            }
            wheelPicker.setVisibleItemCount(5);
            wheelPicker.setItemTextColor(context.getResources().getColor(R.color.text4));
            wheelPicker.setSelectedItemTextColor(context.getResources().getColor(R.color.text2));
            wheelPicker.setIndicator(true);
            wheelPicker.setOnDateSelectedListener(new WheelDatePicker.OnDateSelectedListener() {
                @Override
                public void onDateSelected(WheelDatePicker picker, Date date) {
                    if (date.getTime() > System.currentTimeMillis()) {
                        Calendar calendar = Calendar.getInstance();
                        picker.setYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                        picker.setSelectedDay(calendar.get(Calendar.DAY_OF_MONTH));
                    }
                }
            });
        } else {
            if (!empty(textView.getTag())) {
                final WheelDatePicker wheelPicker = (WheelDatePicker) dialog.findViewById(R.id.wheel_date_picker);
                Date tmpDate = (Date) textView.getTag();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tmpDate);
                wheelPicker.setYearAndMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
                wheelPicker.setSelectedDay(calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
        final WheelDatePicker wheelPicker = (WheelDatePicker) dialog.findViewById(R.id.wheel_date_picker);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = wheelPicker;
                Date tmpDate = wheelPicker.getCurrentDate();
                String birthday = DateUtil.getStringByFormat(tmpDate, "yyyy-MM-dd");
                textView.setText(birthday);
                listener.onDateSelected(wheelPicker, tmpDate);
                textView.setTag(tmpDate);
                wheelPicker.setTag(tmpDate);
                dialog.dismiss();
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }


    /**
     * 显示区域的选择器
     *
     * @param context
     * @param textView
     * @return
     */
    public ChooserHelper showAreaChooser(Context context, final TextView textView) {
        bulidAreaDialog(context, textView);
        return this;
    }


    /**
     * 获取选择的值
     *
     * @return
     */
    public String getChooseVal() {
        if (valPicker != null) {
            ChooseEntity chooseEntity = (ChooseEntity) valPicker.getData().get(valPicker.getCurrentItemPosition());
            return chooseEntity.value;
        }
        return null;
    }

    public String getChooseVal(String defVal) {
        if (valPicker != null) {
            ChooseEntity chooseEntity = (ChooseEntity) valPicker.getData().get(valPicker.getCurrentItemPosition());
            return chooseEntity.value;
        }
        return defVal;
    }

    /**
     * 创建区域对话框
     *
     * @param context
     * @param textView
     * @return
     */
    Dialog bulidAreaDialog(Context context, final TextView textView) {
        if (dialog == null) {
            final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_area_wheel, null);
            dialog = UIUtils.createDialog(context, dialogView, 0, R.style.bottomInWindowAnim, true);
            Window dialogWindow = dialog.getWindow();
            applyCompat(dialogWindow);
            //修复状态栏变黑的问题
            int dialogHeight = 0;//screenHeight - statusBarHeight;
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
            final WheelAreaPicker wheelPicker = (WheelAreaPicker) dialogView.findViewById(R.id.wheel_area);
            final List<AreaEntity> data = generateAreas();
            wheelPicker.hideArea();
            wheelPicker.setDataCreater(new WheelAreaPicker.DataCreater() {
                @Override
                public List<AreaEntity> getProvinces() {
                    return data;
                }

                @Override
                public List<AreaEntity.ChildBeanX> getCitys(int provincesPosition) {
                    return data.get(provincesPosition).get_child();
                }

                @Override
                public List<AreaEntity.ChildBeanX.ChildBean> getAreas(int provincesPosition, int cityPosition) {
                    return data.get(provincesPosition).get_child().get(cityPosition).get_child();
                }
            });
            if (textView.getTag() != null) {
                String[] strs = (String[]) textView.getTag();
                wheelPicker.setProvinceCityArea(strs[0], strs[1], strs[2]);
            }
        } else {
            if (!empty(textView.getTag())) {
                final WheelAreaPicker wheelPicker = (WheelAreaPicker) dialog.findViewById(R.id.wheel_area);
            }
        }
        final WheelAreaPicker wheelPicker = (WheelAreaPicker) dialog.findViewById(R.id.wheel_area);
        dialog.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaPicker = wheelPicker;
                String address;
                if (wheelPicker.getArea() instanceof String) {
                    address = wheelPicker.getProvince() + " " + wheelPicker.getCity();
                } else {
                    address = wheelPicker.getProvince() + " " + wheelPicker.getCity() + " " + wheelPicker.getArea();
                }
                textView.setText(address);
                if (onselectValListener != null) {
                    onselectValListener.onSelectValListener(wheelPicker.getProvince() + " " + wheelPicker.getCity() + " " + wheelPicker.getArea(), 1);
                }
                if (onselectListener != null) {
                    onselectListener.onSelectListener(wheelPicker.getProvince()+"",wheelPicker.getCity()+"",wheelPicker.getArea()+"");
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }
    private void applyCompat(Window dialogWindow) {
        if (Build.VERSION.SDK_INT <= 19) {
            return;
        }
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogWindow.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    List<AreaEntity> Area;

    public List<AreaEntity> generateAreas() {
        String s = readAssetsJson();
        return Area = JsonDeal.json2Object(s, new TypeToken<List<AreaEntity>>() {
        });

    }

    public interface OnselectValListener {
        void onSelectValListener(String text, int index);
    }

    /**
     * 读取assets下的json数据
     */
    private String readAssetsJson() {
        AssetManager assetManager = dialog.getContext().getAssets();
        try {
            InputStream is = assetManager.open("area.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
