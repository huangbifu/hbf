package com.cnsunrun.common.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;

import com.sunrun.sunrunframwork.http.cache.NetSession;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Intent跳转代理类
 * Created by WQ on 2017/8/24.
 */

public class IntentPoxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.startsWith("start")) {//移除方法前缀
            methodName = methodName.replace("start", "");
        }
        int defaultResultCode = 110;
        ResultCode resultCodeAnno = method.getAnnotation(ResultCode.class);
        TargetActivity targetAnno = method.getAnnotation(TargetActivity.class);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Activity sourceAct = (Activity) args[0];
        String targetActivity;
        if (targetAnno == null) {
            targetActivity = readActivityRealName(sourceAct, methodName);
        } else {
            targetActivity = targetAnno.value().getName();
        }
        if (targetActivity == null) {
            throw new RuntimeException(methodName + " 没有在清单文件中进行配置");
        }
        Class<? extends Activity> clazz = (Class<? extends Activity>) getClass().getClassLoader().loadClass(targetActivity);
        Intent intent = new Intent(sourceAct, clazz);
        for (int i = 1; i < parameterAnnotations.length; i++) {
            //读取方法参数注解.进行参数放置
            Key paramsAnnotation = (Key) searchParamsAnnotation(parameterAnnotations[i], Key.class);
            Object params = args[i];
            //参数放置
            if (paramsAnnotation != null) {
//              boolean result=  putParamsToIntent(intent,paramsAnnotation.value(),  params);
                if (params instanceof String) {
                    intent.putExtra(paramsAnnotation.value(), (String) params);
                } else if (params instanceof CharSequence) {
                    intent.putExtra(paramsAnnotation.value(), (CharSequence) params);
                } else if (params instanceof Integer) {
                    intent.putExtra(paramsAnnotation.value(), (Integer) params);
                } else if (params instanceof Float) {
                    intent.putExtra(paramsAnnotation.value(), (Float) params);
                } else if (params instanceof Double) {
                    intent.putExtra(paramsAnnotation.value(), (Double) params);
                } else if (params instanceof Byte) {
                    intent.putExtra(paramsAnnotation.value(), (Byte) params);
                } else if (params instanceof Short) {
                    intent.putExtra(paramsAnnotation.value(), (Short) params);
                } else if (params instanceof Long) {
                    intent.putExtra(paramsAnnotation.value(), (Long) params);
                } else if (params instanceof Character) {
                    intent.putExtra(paramsAnnotation.value(), (Character) params);
                } else if (params instanceof Boolean) {
                    intent.putExtra(paramsAnnotation.value(), (Boolean) params);
                } else if (params instanceof char[]) {
                    intent.putExtra(paramsAnnotation.value(), (char[]) params);
                } else if (params instanceof int[]) {
                    intent.putExtra(paramsAnnotation.value(), (int[]) params);
                } else if (params instanceof double[]) {
                    intent.putExtra(paramsAnnotation.value(), (double[]) params);
                } else if (params instanceof float[]) {
                    intent.putExtra(paramsAnnotation.value(), (float[]) params);
                } else if (params instanceof short[]) {
                    intent.putExtra(paramsAnnotation.value(), (short[]) params);
                } else if (params instanceof byte[]) {
                    intent.putExtra(paramsAnnotation.value(), (byte[]) params);
                } else if (params instanceof boolean[]) {
                    intent.putExtra(paramsAnnotation.value(), (boolean[]) params);
                } else if (params instanceof String[]) {
                    intent.putExtra(paramsAnnotation.value(), (String[]) params);
                } else if (params instanceof Parcelable[]) {
                    intent.putExtra(paramsAnnotation.value(), (Parcelable[]) params);
                } else if (params instanceof CharSequence[]) {
                    intent.putExtra(paramsAnnotation.value(), (CharSequence[]) params);
                } else if (params instanceof Intent) {
                    intent.putExtras((Intent) params);
                } else if (params instanceof Bundle) {
                    intent.putExtras((Bundle) params);
                } else if (params instanceof ArrayList) {
                    intent.putCharSequenceArrayListExtra(paramsAnnotation.value(), (ArrayList) params);
                } else if (params instanceof Serializable) {
                    intent.putExtra(paramsAnnotation.value(), (Serializable) params);
                } else if (params instanceof Parcelable) {
                    intent.putExtra(paramsAnnotation.value(), (Parcelable) params);
                } else {
                    //特殊类型,使用DataStorage,进行存储
                    put(paramsAnnotation.value(), params);
                }
            } else {
                //参数不存在key注解时,尝试当做Intent类型以及 Bundle类型处理
                if (params instanceof Intent) {
                    intent.putExtras((Intent) params);
                } else if (params instanceof Bundle) {
                    intent.putExtras((Bundle) params);
                } else {
                    //使用参数类型类名作为key将参数序列化到DataStorage中
                    put(params.getClass().getName(), params);
                }
            }
        }
        if (resultCodeAnno != null) {
            defaultResultCode = resultCodeAnno.value();
        }
        sourceAct.startActivityForResult(intent, defaultResultCode);
        return null;
    }

    /**
     * 查找参数注解
     *
     * @param annotations
     * @param clazz
     * @return
     */
    private Annotation searchParamsAnnotation(Annotation[] annotations, Class<?> clazz) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == clazz) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 读取方法对应的Activity的真实名称
     *
     * @param act
     * @param methodName
     * @return
     */
    private String readActivityRealName(Context act, String methodName) {

        try {
            if (activitys.isEmpty()) {
                PackageInfo packageInfo = act.getPackageManager().getPackageInfo(act.getPackageName(),
                        PackageManager.GET_ACTIVITIES);
                String targetAcitivyName = null;
                for (ActivityInfo activity : packageInfo.activities) {
                    if (activity.name.contains(methodName)) targetAcitivyName = activity.name;
                    activitys.add(activity.name);
                }
                return targetAcitivyName;
            } else {
                for (String activity : activitys) {
                    if (activity.contains(methodName)) return activity;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置额外的数据存储器,用于存放传递的特殊类型的数据
     *
     * @param dataStorage
     */
    public static void setDataStorage(DataStorage dataStorage) {
        DATA_STORAGE = dataStorage;
    }

    private void put(String key, Object value){
        if(DATA_STORAGE==null)throw new RuntimeException("没有设置DataStorage,不支持:"+value+"该对象的传递");
        DATA_STORAGE.put(key,value);
    }

    static DataStorage DATA_STORAGE;
    /**
     * 清单文件中配置的Activity缓存
     */
    private Set<String> activitys = new HashSet<>();
    /**
     * 代理实例缓存,避免重复创建
     */
    private static Map<Class, Object> INTENT_PROXYS = new HashMap<>();
    /**
     * 获取代理实例
     *
     * @return
     */
    public static <T> T getProxyInstance(Class<? extends T> interfaceClazz) {
        Object obj = INTENT_PROXYS.get(interfaceClazz);
        if (obj == null) {
            synchronized (IntentPoxy.class) {
                obj = Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                        new Class[]{interfaceClazz},
                        new IntentPoxy());
                INTENT_PROXYS.put(interfaceClazz, obj);
            }
        }
        return (T) obj;
    }

}
