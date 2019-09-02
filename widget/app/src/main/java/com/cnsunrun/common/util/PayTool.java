package com.cnsunrun.common.util;//package com.cnsunrun.common.util;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.alipay.sdk.app.PayTask;
//import com.cnsunrun.diaiwan.BuildConfig;
//import com.sunrun.sunrunframwork.pay.alipay.PayResult;
//import com.sunrun.sunrunframwork.uiutils.ToastUtils;
//
//import java.util.Map;
//
///**
// * Created by WQ on 2017/6/21.
// */
//
//public class PayTool {
//    private String order_id, OrderNumber, taskMoney, OrderName, nofity_url = BuildConfig.API_ADDRESS + "Api/Pay/Callback/ali_recharge_callback";
//    //     * 支付宝支付业务：入参app_id
//    public static final String APPID = ConstantValue.APPID;
//    //     * 支付宝账户登录授权业务：入参pid值
//    public static final String PID = "";
//    //     * 支付宝账户登录授权业务：入参target_id值
//    public static final String TARGET_ID = "";
//    //     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个
//    public static final String RSA2_PRIVATE = ConstantValue.RSA2_PRIVATE;
//    public static final String RSA_PRIVATE = "";
//    private static final int SDK_PAY_FLAG = 1;
//    private static final int SDK_AUTH_FLAG = 2;
//    Activity activity;
//    onResultListener onResultListener;
//
//    public PayTool(Activity activity) {
//        this.activity = activity;
//    }
//
//    public PayTool setOrderInfo(String order_id, String OrderNumber, String OrderName, String taskMoney) {
//        this.order_id = order_id;
//        this.OrderNumber = OrderNumber;
//        this.taskMoney = taskMoney;
//        this.OrderName = OrderName;
//        return this;
//    }
//
//    public PayTool setNofityUrl(
//            String nofityUrl
//    ) {
//        this.nofity_url = nofityUrl;
//        return this;
//    }
//
//    public PayTool setOnResultListener(PayTool.onResultListener onResultListener) {
//        this.onResultListener = onResultListener;
//        return this;
//    }
//
//    /**
//     * 支付宝支付
//     */
//    public PayTool payOrderFromAlipay() {
//        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
//            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            activity.finish();
//                        }
//                    }).show();
//            return this;
//        }
//        /**
//         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
//         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
//         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
//         * orderInfo的获取必须来自服务端；
//         */
//        AlipayData biz_content = new AlipayData();
//        biz_content.setBody("订单支付");
//        biz_content.setOut_trade_no(OrderNumber);
//        biz_content.setProduct_code("QUICK_MSECURITY_PAY");
//        biz_content.setSeller_id("2088221726008512");
//        biz_content.setSubject(OrderName == null ? ("支付订单:" + OrderNumber) : OrderName);
//        biz_content.setTimeout_express("30m");
//        biz_content.setTotal_amount(taskMoney);
//        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, biz_content, rsa2);
//        if (nofity_url != null) {
//            params.put("notify_url", nofity_url);
//        }
//        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//        final String orderInfo = orderParam + "&" + sign;
//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//                PayTask alipay = new PayTask(activity);
//                String result = alipay.pay(orderInfo, true);
//                Log.i("msp", result.toString());
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
//            }
//        };
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//        return this;
//    }
//
//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//        @SuppressWarnings("unused")
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case SDK_PAY_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    PayResult payResult = new PayResult(msg.obj.toString());
////                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为9000则代表支付成功
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        onResultListener.onResult(true, resultStatus);
//                        ToastUtils.longToast("支付成功!");
//                        //finish();
//                    } else {
//                        onResultListener.onResult(false, resultStatus);
//                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        ToastUtils.longToast("支付失败!");
//                    }
//                    break;
//                }
//                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        ToastUtils.longToast("授权成功!");
//                    } else {
//                        // 其他状态值则为授权失败
//                        ToastUtils.longToast("授权失败!");
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };
//
//    public interface onResultListener {
//        public void onResult(boolean isSuccess, String resultStatus);
//    }
//}
