package com.hxsn.town.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hxsn.town.alipay.Result;
import com.hxsn.town.alipay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PayActivity extends FragmentActivity {

	private static final String LOGCAT= "com.snsoft.phone.alipay.PayActivity";
	
	private static final int SUCCESS = 1;
	private static final int FAIL = -1;
	private static final int WAIT = 0;
	
	public static final String PARTNER = "2088511894956651";
	public static final String SELLER = "dahxsn@126.com";
	
	private static final String RSA_PRIVATE = "MIICXAIBAAKBgQDHo0bS6/RvCTEwhW5pHyMLfVN030Y4T3apMGOwzZO1ZSp3UfQX"
	+ "7iZbCq7M6PlfnH410JLKkPoVb5yMpuaRudkTFTeIzs1nx9eD6jMPBS7UzjDzX+jo"
	+ "fA/n2bMTyTLaNi2Q1WyGfWN7vWa9aLfE130Mvht2VpPWTs2kX+O5Nr7txwIDAQAB"
	+ "AoGACY2GlhE0YRvPfsLjxaP3yVT16WHFumwTAYWPP6aWqtdscMk7JOoAa9rXAncj"
	+ "d6/FWk3guFC3ps7wbsqNvetmt61nXCOKm92rF0srF1EEBcCRl0RW2ZucKGTS812P"
	+ "3RX/QvgZf2Ni/EobSBhKKVXlSPCA0i9c2MCGjtolrQeC0gECQQDofoYDzCXRY3O4"
	+ "1vnL/iHbuRdCWKGz0P2J0fd9FuiXOaGB9cM4+lQkn+7KOFmxMIy2wVGvcjtK6FDf"
	+ "auYXwulVAkEA29JauCsaGnT23ZK8uZIbD+jaU0SwYJdnnqBxJ0YtOV+ccHyun6VD"
	+ "Q0K1guKNGqgGFjqTqIQ1Wif9PX12nbDKqwJAUyPLsNf0PiieXV8i/PvoB7bsxmED"
	+ "klYOFJbtNeJXUpzdUKgeI+YA6Qf4UIBOMrr11vTSzTbAVU02fm6v0Zd18QJBAJF/"
	+ "MBDKZqKwXF3YLBZgAI2NQETxKBUm4az64Spvpqtc/3zs6eA9UMIKO34AWgkUwcKg"
	+ "zu8xYubjpRi4HHTPebUCQAG3lUV0teVyD2QnKSlx1b509Q2UP6QO2jiB8o9wrhul"
	+ "VrEm979hOj8fjY2GQE5/OQ4u1qy8G7NodvzXjANt45w=";
	
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	
	private String title = "";
	private String content = "";
	private String orderNum = "";
	private String price = "";
	
	private Bundle bundle ;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.getResultStatus();
				bundle.putString("resultStatus", resultStatus);
				Intent intent = new Intent();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					intent.putExtras(bundle);
					setResult(SUCCESS, intent);
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						intent.putExtras(bundle);
						setResult(WAIT, intent);
						finish();
					} else {
						intent.putExtras(bundle);
						setResult(FAIL, intent);
						finish();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_main);   //无界面activity，如需界面在minifest中去掉@android:style/Theme.NoDisplay
		bundle = getIntent().getBundleExtra("bundle");
		title = bundle.getString("title");
		content = bundle.getString("content");
		orderNum = bundle.getString("orderNum");
		price = bundle.getString("price");
		pay();
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	private void pay(){
		String orderInfo = getOrderInfo(title, content, price, orderNum);
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayActivity.this);
				// 调用支付接口
				String result = alipay.pay(payInfo);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(PayActivity.this);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
		
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price, String orderNum) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNum + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + "商品详情" + "\""; //body不能为空，如服务器端body不为空使用上句

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://xiangwang.info/aliNotifyUrl.shtml"
				+ "\"";
		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
