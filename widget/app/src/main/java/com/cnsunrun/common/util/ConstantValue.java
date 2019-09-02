package com.cnsunrun.common.util;

import static com.cnsunrun.common.quest.NetQuestConfig.HTTP_API;

/**
 * 存放常量信息
 */
public interface ConstantValue {


    //banner的轮播时间
    int VP_RUN_TIME = 5000;
    //拍照的回调
    int REQUEST_IMAGE = 110;
    //刷新数据
    int MSG_UPDATA_CONTACT_LIST = 15;
    //更改密码状态
    int MSG_UPDATA_PASSWORD_STATUS = 16;
    //返回数据给Fragment
    int MSG_TYPE_RETURN_DATA = 18;
    //支付成功
    int TYPE_PAY_ORDER_SUCCESS = 19;

    //获取银行卡信息请求码
    int GET_BANK_CARD_INFO=258;
    int GET_ALI_ACCOUNT_INFO=259;


    // 支付宝密钥
    String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDD1VyXiKcsH8MXDbIN5bZfD4khAafeOxOsqwYhBFDoyKSHXl+WNYrGbxpFp+r6jkrakWGuq051oS5BJEsqeC0JeuN1nB36CHggi5+D25+bWvkW2Fry4EIUKy+6TCeQa/3zUVPhHhmRcXKJw46JdBCBlHwWNxKgCoueKSvuiAnCQuIXoT4y596FtffqubzijdvhrpSTWILy3rV0XUIHInCwIuN/2KQmlxhJvD0PJBw8zN//n1CUHsy3pTNrz9UeAkZR2U0wk76f0GK8Ay9eTV0DoblxQSA/LWgiGDWwZLEiAuIx0CSMrXosPcYIN8De+nQ4RPVlVW5TzffJsMrMhMyDAgMBAAECggEAaY2LBrJFSOclxDtBiXQTRFGfXAcEcCfvhtX3y7wD0DS1WbFoXBIn5qTjyD+ly77+YaiSug7jj0t9HudtDEsSG1X9W9r13VrLRN987xllgdsa/1FGVDrD4xMT+popiRJsIsXnLCOJLHe1gMPDBBtm+jEKYP72LwUAFBOk9kCk4eswHcSe4vOsGPQ/A4aDAGpTkPg+CzNdgN+ifRQMjICVpyOEbph3zHfYy8a8+JxYQTJ7GxkORs646M7t4KsPT4vLaNB70KT2jtsrx9bUFGL5Pat0O1LfrD3LZWnIhF4Qkit9QPSeEKMbX1G4yGgsujoNJxgqXh0FZ+A4EnxUm2zS4QKBgQD5npGytwrODUHWMzSJVbxeeJuFfFvIjlqlGLt2knVceZtkv+41dgA+tvv8M7OhZnzfSAJ539298dYn8O3+L9qd/1zltoLIx1K9oDGNHfKxd88IoPBl74ZH9WQv8YJdhhGPewxJPvIrcbPO7w5pVV2duMneU5ecAeoiCwoKtsnlHwKBgQDI1tWIlostiq9ikQMcQDhmdNhkjYMdchMKjhVGOCOoDs5gNYu1amjzlDQskds11SmA5OvD5DHA8PH8YThmdsGuuW0JHDHluq2D7HYWeR7/10aaYqDUwV1/LTdqJbBI/1+wBo7BnEhlWfC60K69eUWWDC7PXQ8NN/ZUhagPuAcoHQKBgGOCcTa7fZUy98H6W8E76oIGtoj6UGSdS0lxmJnmPN8Go4oAcWfiiKp3q/CYc82t263vOP143x7vn54Aj/+69/3wZQo2PeDohPWPPPGytKNGzcP01KDuYauiCbSLJJs/7RQzy2wPqd7jzvh5mFcpbs6HcgItdMz+fmXf3Lr0r3ulAoGATwWIeveQx1h77XJVIPZ7uoRa6CVt41YQ++QvprNMBEHfHaez0quNo9k6Nhclgybd76MiyK6ujNXAwKaYBGjRiCfZKO55C4TJ5NBTHkCF2/AeDXyk/1Edsn5eAVvW4sHoKmBKJw+QxKAAweB+AHsI0CL3AAwDchYEy/jSyWyFFFkCgYEAyvXCMTkjvNHDYH8K3zyDNqgifTzgeVLShUoq8P5R9kQ2BlUm+QC0CGrylu/1r7kOfnYwWZ4gdpnk83uAt8d+pNhtC1bv46NF6A4KtgNq9a9EGUsytkqtPu48clZMMTPtkzFugjT2GQu/pwvs0we+aSDnqzA36mh2UglBl6xLE6g=";
    String APPID = "2017061607501207"; //支付宝支付的id
    String PAY_SELLER_ID = "2088221726008512";  //支付宝的'partner
    String ALIPAYNOTIFY_URL_CALLBACK=HTTP_API+"/Api/Pay/Callback/ali_callback";


    //微信支付
    String WX_APP_ID = "wx6a40b01968ddd184"; //微信app_id
    String PARTNER_ID = "1486266702";//商户号
    String API_KEY = "KRp2eVui9l6iAzEdI0SNEpQ0hq8XKjMm";
    String WX_ORDER_CALLBACK = HTTP_API + "/Api/Pay/Callback/weixin_callback";
    String WX_RECHARGE_CALLBACK = HTTP_API + "/Api/Pay/Callback/weixin_recharge_callback";
}
