package com.cnsunrun.common.util;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.Utils;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imkit.tools.CharacterParser;

/**
 * 智能工具类
 * Created by WQ on 2017/11/3.
 */

public class TestTool {

//    public <T> T[] asArray(Object ... obj){
//        Object[]targetArr=new Object[10];
//        for (Object o : obj) {
//            boolean array = o.getClass().isArray();
//
//        }
//    }

    public static   String[] convertArray(String[]...strings){
        List<String> aimList=new ArrayList<>();
        for (String[] string : strings) {
            aimList.addAll(Arrays.asList(string));
        }
       return  aimList.toArray(new String[aimList.size()]);
    }

//    public static   Object[] convertArray(Object...strings){
//        List<Object> aimList=new ArrayList<>();
//
//        for (Object string : strings) {
//            if(string.getClass().isArray()){
//                Object[] innerArr = (Object[]) string;
//                aimList.addAll(Arrays.asList(innerArr));
//            }else {
//                aimList.add(string);
//            }
//        }
//        return  aimList.toArray(new String[aimList.size()]);
//    }
    /**
     * 转换数组为可变集合
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> List<T> asArrayList(T... data) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(data));
        return arrayList;
    }

    /**
     */
    public static <T> List<T> safeList(List<T> data) {
        return data == null ? new ArrayList<T>() : data;
    }

    public static <T> List<String> list2StringList(List<T> data) {
        List<String> targetList = new ArrayList<>();
        if (data != null)
            for (T item : data) {
                targetList.add(String.valueOf(item));
            }
        return targetList;
    }

    public static String substring(String str, int len) {
        if (str.length() > len) {
            str = str.substring(0, len);
        }
        return str;
    }

    /**
     * 选取第一个非空值
     *
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> T vaildVal(T... ts) {
        for (T t : ts) {
            if (!EmptyDeal.isEmpy(t)) return t;
        }
        return null;
    }

    public static String showTestCaptcha(BaseBean baseBean) {
        JSONObject jsonObj = JsonDeal.createJsonObj(baseBean.toString());
        String code = jsonObj.optString("code");
        if (!EmptyDeal.isEmpy(code)) {
            UIUtils.shortM(code);
            return code;
        }else {
            UIUtils.shortM(baseBean);
        }
        return "";

    }

   public static boolean  checkSuffix(String path,String...suffixs){
       for (String suffix : suffixs) {
           if (String.valueOf(path).endsWith(suffix))
               return true;
       }
       return false;
   }
    public static boolean  checkImgSuffix(String path){
        return checkSuffix(path,"png", "jpg", "jpeg", "gif","bmp");
    }

    public static SpannableStringBuilder getColoredDisplayName(String filterStr, String displayName) {
        return getColored(filterStr, displayName);
    }


    public static SpannableStringBuilder getColoredName(String filterStr, String name) {
        return getColored(filterStr, name);
    }



    public static SpannableStringBuilder getColored(String filterStr, String name) {
        try {
            String lowerCaseFilterStr = filterStr.toLowerCase();
            String lowerCaseName = name.toLowerCase();
            String lowerCaseNameSpelling = CharacterParser.getInstance().getSelling(name).toLowerCase();
            if (lowerCaseName.contains(lowerCaseFilterStr)) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                int index = lowerCaseName.indexOf(lowerCaseFilterStr);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), index, index + filterStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                return spannableStringBuilder;
            } else if (lowerCaseNameSpelling.startsWith(lowerCaseFilterStr)) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                int nameLength = name.length();
                int showCount = 1;
                for (int i = 0; i < nameLength; i++) {
                    String subName = name.substring(0, i + 1);
                    if (filterStr.length() > CharacterParser.getInstance().getSelling(subName).length()) {
                        showCount++;
                        continue;
                    } else {
                        break;
                    }
                }
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#0099ff")), 0, showCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                return spannableStringBuilder;
            } else {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(name);
                return spannableStringBuilder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SpannableStringBuilder(name);
    }


    public static   boolean isNotEnough(String money,String balance){
        return Utils.valueOf(money,0)>Utils.valueOf(balance,0)||((int)(Utils.valueOf(money,0)*100)==0);
    }
    public static void invokeMethod(Object obj, String method, Object...args) {
        try {
            Class<?> aClass = obj.getClass();
            for (; aClass != Object.class; aClass = aClass.getSuperclass()) {
                Method[] declaredMethods = aClass.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals(method)) {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(obj, args);
                        return;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public static <T> T get(Object obj, String fieldName) {
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    field.setAccessible(true);
                    return (T) field.get(obj);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
