//package com.cnsunrun.common.util;
//
//import android.content.Context;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.services.geocoder.GeocodeQuery;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.sunrun.sunrunframwork.uiutils.UIUtils;
//import com.sunrun.sunrunframwork.utils.log.Logger;
//
///**
// * Created by WQ on 2017/3/20.
// */
//
//public class LocationUtil {
//    AMapLocationClient mlocationClient;
//    AMapLocationClientOption mLocationOption;
//    AMapLocation aMapLocation;
//    Context mContext;
//
//    public LocationUtil(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public void initOnceLocation(AMapLocationListener listener){
//        mlocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        // 设置定位监听
//        mlocationClient.setLocationListener(listener);
//        // 设置为高精度定位模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        mLocationOption.setOnceLocation(true);
//        mLocationOption.setNeedAddress(true);
//        // 设置定位参数
//        mlocationClient.setLocationOption(mLocationOption);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用onDestroy()方法
//        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//    }
//    public void initLocation(int Interval,AMapLocationListener listener){
//        mlocationClient = new AMapLocationClient(mContext);
//        mLocationOption = new AMapLocationClientOption();
//        // 设置定位监听
//        mlocationClient.setLocationListener(listener);
//        // 设置为高精度定位模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        mLocationOption.setOnceLocation(false);
//        mLocationOption.setNeedAddress(true);
//        mLocationOption.setInterval(Interval);
//        // 设置定位参数
//        mlocationClient.setLocationOption(mLocationOption);
//        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//        // 在定位结束后，在合适的生命周期调用onDestroy()方法
//        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//    }
//    public void getLocation(boolean hasDialog){
//        if(hasDialog) {
//            UIUtils.showLoadDialog(mContext, "正在获取位置信息");
//        }
//        mlocationClient.startLocation();
//    }
//    public void stopLocation(boolean hasDialog){
//        mlocationClient.stopLocation();
//    }
//
//    /**
//     * 根据地址解析经纬度
//     * @param context
//     * @param city
//     * @param address
//     * @param listener
//     */
//    public static void getLatlngForAddress(Context context,String city,String address,GeocodeSearch.OnGeocodeSearchListener listener){
//        GeocodeQuery geocodeQuery=new GeocodeQuery(address, city);
//        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
//        geocodeSearch.setOnGeocodeSearchListener(listener);
//        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);
//    }
//    public static abstract class LocationChange implements AMapLocationListener {
//        @Override
//       final public void onLocationChanged(AMapLocation amapLocation) {
//
//            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
//                locationChanged(amapLocation);
//            } else {
//                if(amapLocation!=null) {
//                    Logger.D("locatin:%s  errCode:%s",amapLocation.getErrorInfo() , amapLocation.getErrorCode());
//                    if ( amapLocation.getErrorCode() == 12) {
//                        UIUtils.shortM("请检查位置权限是否正常开启");
//                    }
//                }
//                locationChanged(null);
//            }
//            UIUtils.cancelLoadDialog();
//        }
//        public void  locationChanged(AMapLocation amapLocation){
//
//        }
//    }
//}
