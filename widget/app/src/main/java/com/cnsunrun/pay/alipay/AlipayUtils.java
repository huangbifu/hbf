package com.cnsunrun.pay.alipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

import static com.cnsunrun.pay.alipay.PayConfig.SDK_PAY_FLAG;


/**
 * @阿里支付帮助类
 */
public class AlipayUtils {
	Activity mContext;
	Handler handler;
	public AlipayUtils(Activity mContext) {
		this.mContext=mContext;
		handler=new Handler();
	}
	public void requestPay(){
		
	}
	public void requestPay(String orderNo, String total, final Callback callback){
		AlipayData biz_content = new AlipayData();
		biz_content.setBody("订单支付");
		biz_content.setOut_trade_no(orderNo);
		biz_content.setProduct_code("QUICK_MSECURITY_PAY");
		biz_content.setSeller_id(PayConfig.SELLER);
		biz_content.setSubject("支付订单:" + orderNo);
		biz_content.setTimeout_express("30m");
		biz_content.setTotal_amount(total);

		boolean rsa2 = (PayConfig.RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(PayConfig.APPID, biz_content, rsa2);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? PayConfig.RSA2_PRIVATE : PayConfig.RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(mContext);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());

				final Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.handleMessage(msg);
					}
				});
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	/**
	 * 检测是否安装支付宝     * @param context     * @return
	 */
	public static boolean checkAliPayInstalled(Context context) {
		Uri uri = Uri.parse("alipays://platformapi/startApp");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		ComponentName componentName = intent.resolveActivity(context.getPackageManager());
		return componentName != null;
	}
}
