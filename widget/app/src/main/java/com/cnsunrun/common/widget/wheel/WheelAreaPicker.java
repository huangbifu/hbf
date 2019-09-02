package com.cnsunrun.common.widget.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.cnsunrun.common.model.AreaEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * WheelAreaPicker
 * Created by Administrator on 2016/9/14 0014.
 */
public class WheelAreaPicker extends LinearLayout implements IWheelAreaPicker {
    private static final float ITEM_TEXT_SIZE = 14;
    private static final String SELECTED_ITEM_COLOR = "#353535";
    private static final int PROVINCE_INITIAL_INDEX = 0;

    private Context mContext;

    private LayoutParams mLayoutParams;

    private WheelPicker mWPProvince, mWPCity, mWPArea;

    DataCreater dataCreater;//数据创建器

    public void setDataCreater(DataCreater dataCreater) {
        this.dataCreater = dataCreater;
        obtainProvinceData();
    }


    /**
     * 数据创建器,由外部指定生成对应的省市区数组
     */
    public interface DataCreater {
        List<AreaEntity> getProvinces();

        List<AreaEntity.ChildBeanX> getCitys(int provincesPosition);

        List<AreaEntity.ChildBeanX.ChildBean> getAreas(int provincesPosition, int cityPosition);
    }

    public WheelAreaPicker(Context context) {
        super(context);
        init(context);
    }

    public WheelAreaPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WheelAreaPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        initLayoutParams();

        initView(context);

        addListenerToWheelPicker();
    }


    private void initLayoutParams() {
        mLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(5, 5, 5, 5);
        mLayoutParams.width = 0;
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        mContext = context;

        mWPProvince = new WheelPicker(context);
        mWPCity = new WheelPicker(context);
        mWPArea = new WheelPicker(context);

        initWheelPicker(mWPProvince, 1);
        initWheelPicker(mWPCity, 2f);
        initWheelPicker(mWPArea, 1.5f);
    }

    private void initWheelPicker(WheelPicker wheelPicker, float weight) {
        mLayoutParams.weight = weight;
        wheelPicker.setItemTextSize(dip2px(mContext, ITEM_TEXT_SIZE));
        wheelPicker.setSelectedItemTextColor(Color.parseColor(SELECTED_ITEM_COLOR));
        wheelPicker.setIndicator(true);
        wheelPicker.setIndicatorColor(Color.parseColor("#dddddd"));
        wheelPicker.setIndicatorSize(2);
        wheelPicker.setCurved(false);
        wheelPicker.setLayoutParams(mLayoutParams);
        addView(wheelPicker);
    }


    public void obtainProvinceData() {
        mWPProvince.setData(dataCreater.getProvinces());
        setCityAndAreaData(PROVINCE_INITIAL_INDEX);
    }

    private void addListenerToWheelPicker() {
        //监听省份的滑轮,根据省份的滑轮滑动的数据来设置市跟地区的滑轮数据
        mWPProvince.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                setCityAndAreaData(position);
            }
        });

        mWPCity.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                //获取城市对应的城区的名字
                if(dataCreater.getAreas(mWPProvince.getCurrentItemPosition(), mWPCity.getCurrentItemPosition()) != null){
                    mWPArea.setData(dataCreater.getAreas(mWPProvince.getCurrentItemPosition(), mWPCity.getCurrentItemPosition()));
                    mWPArea.setSelectedItemPosition(0);
                }else{
                    mWPArea.setData(new ArrayList());
                }
            }
        });
    }

    public void setProvinceCityArea(String province, String city, String area) {
        List<AreaEntity> provinces = dataCreater.getProvinces();
        List<String> provincesStr = new ArrayList<>();
        for (AreaEntity areaEntity : provinces) {
            provincesStr.add(areaEntity.getTitle());
        }
        int provinceIndex = provincesStr.indexOf(province);
        if (provinceIndex != -1) {
            mWPProvince.setSelectedItemPosition(provinceIndex);
            List<AreaEntity.ChildBeanX> citys = dataCreater.getCitys(provinceIndex);
            List<String> citysStr = new ArrayList<>();
            for (AreaEntity.ChildBeanX childBeanX : citys) {
                citysStr.add(childBeanX.getTitle());
            }
            mWPCity.setData(citys);
            int cityIndex = citysStr.indexOf(city);
            if (cityIndex != -1) {
                mWPCity.setSelectedItemPosition(cityIndex);
                if(dataCreater.getAreas(provinceIndex, cityIndex) != null && dataCreater.getAreas(provinceIndex, cityIndex).size() > 0){
                    List<AreaEntity.ChildBeanX.ChildBean> areas = dataCreater.getAreas(provinceIndex, cityIndex);
                    mWPArea.setData(areas);
                    List<String> areasStr = new ArrayList<>();
                    for (AreaEntity.ChildBeanX.ChildBean childBean : areas) {
                        areasStr.add(childBean.getTitle());
                    }
                    int areaIndex = areasStr.indexOf(area);
                    if (areaIndex != -1) {
                        mWPArea.setSelectedItemPosition(areaIndex);
                    }
                }else{
                    mWPArea.setData(new ArrayList());
                }
            }
        }
    }


    private void setCityAndAreaData(int position) {
        //重置先前的城市集合数据
        mWPCity.setData(dataCreater.getCitys(position));
        mWPCity.setSelectedItemPosition(0);
        //获取第一个城市对应的城区的名字
        mWPArea.setData(dataCreater.getAreas(position, 0));
        mWPArea.setSelectedItemPosition(0);

    }

    @Override
    public Object getProvince() {
        return dataCreater.getProvinces().get(mWPProvince.getCurrentItemPosition());
    }

    @Override
    public Object getCity() {
        return dataCreater.getCitys(mWPProvince.getCurrentItemPosition()).get(mWPCity.getCurrentItemPosition());
    }

    @Override
    public Object getArea() {
        List areas = dataCreater.getAreas(mWPProvince.getCurrentItemPosition(), mWPCity.getCurrentItemPosition());
        if (areas == null || areas.size() == 0) {
            //return new AreaEntity();
            return "";
        } else {
            return areas.get(mWPArea.getCurrentItemPosition());
        }
    }

    @Override
    public void hideArea() {
        this.removeViewAt(2);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
