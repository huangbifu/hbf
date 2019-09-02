//package com.uuzuche.lib_zxing.decoding;
//
//import android.graphics.Rect;
//import android.hardware.Camera;
//
//import com.google.zxing.ResultPoint;
//import com.google.zxing.common.DetectorResult;
//import com.google.zxing.common.DetectorResultCallback;
//import com.uuzuche.lib_zxing.camera.CameraManager;
//
///**
// * 在这个回调类中实现二维码定位，并调整摄像头的焦距，放大二维码
// *
// * @author jjr
// * @date 2018/3/16
// */
//public class DetectorResultCallbackImp implements DetectorResultCallback {
//
//    /**
//     * 当要扫的二维码处于扫描框中时，获取该二维码在扫描框中的宽度，与扫描框的宽度进行对比，
//     * 小于扫描框宽度的1/4，则认为二维码在扫描框中较小（镜头较远），则需要放大摄像头焦距，
//     * 而不需要移动手机来调整。
//     *
//     * @param detectorResult 从中可以获取二维码的定位符
//     * @return 返回true表示已调整焦距，结束此次decode。
//     */
//    @Override
//    public boolean callback(DetectorResult detectorResult) {
//        CameraManager cameraManager = CameraManager.get();
//        ResultPoint[] p = detectorResult.getPoints();
//        //计算扫描框中的二维码的宽度，两点间距离公式
//        float point1X = p[0].getX();
//        float point1Y = p[0].getY();
//        float point2X = p[1].getX();
//        float point2Y = p[1].getY();
//        int len = (int) Math.sqrt(Math.abs(point1X - point2X) * Math.abs(point1X - point2X) + Math.abs(point1Y - point2Y) * Math.abs(point1Y - point2Y));
//        Rect frameRect = cameraManager.getFramingRect();
//        if (frameRect != null) {
//            int frameWidth = frameRect.right - frameRect.left;
//            Camera camera = cameraManager.getCamera();
//            Camera.Parameters parameters = camera.getParameters();
//            int maxZoom = parameters.getMaxZoom();
//            int zoom = parameters.getZoom();
//            if (parameters.isZoomSupported()) {
//                //二维码在扫描框中的宽度小于扫描框的1/4，放大镜头
//                if (len <= frameWidth / 4) {
//                    if (zoom == 0) {
//                        zoom = maxZoom / 2;
//                    } else if (zoom <= maxZoom - 10) {
//                        zoom = zoom + 10;
//                    } else {
//                        zoom = maxZoom;
//                    }
//                    parameters.setZoom(zoom);
//                    camera.setParameters(parameters);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//}
