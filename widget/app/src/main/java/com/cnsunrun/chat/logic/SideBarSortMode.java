package com.cnsunrun.chat.logic;

import android.text.TextUtils;
import android.util.SparseArray;

import com.sunrun.sunrunframwork.view.sidebar.CharacterParser;
import com.sunrun.sunrunframwork.view.sidebar.PinyinComparator;
import com.sunrun.sunrunframwork.view.sidebar.SideBarUtils;
import com.sunrun.sunrunframwork.view.sidebar.SortModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * SideBarSortMode
 * List条目快速选择组件的辅助类
 * Created by WQ on 2017/9/13.
 */
public class SideBarSortMode {
    private List<? extends SortModel> sourceDateList;//数据源
    private List<? extends SortModel> currentDateList;//当前使用的数据源
    /**
     * 首字母与首字母首次在数据源中出现的位置的映射
     */
    private SparseArray<String> sparseArray=new SparseArray();
    /**
     * 拼音排序的比较器
     */
   private static PinyinComparator pinyinComparator = new PinyinComparator();
    /**
     * 实例化汉字转拼音类
     */
    private static CharacterParser  characterParser = CharacterParser.getInstance();
    public SideBarSortMode() {
    }

    /**
     * 设置数据源,外部列表数据源更新时,需要再次调用
     * @param sourceDateList 数据源
     */
    public void setSourceDateList(List<? extends SortModel> sourceDateList) {
        this.sourceDateList=sourceDateList;
        SideBarUtils.filledData(sourceDateList);
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList, pinyinComparator);
        updateSourceData(sourceDateList);
    }

    /**
     * 更新数据源,内部用于搜索的时候
     * @param sourceDateList 数据源
     */
    private void updateSourceData(List<? extends SortModel> sourceDateList) {
        this.currentDateList = sourceDateList;
        String chart="";
        sparseArray.clear();
        for (int i = 0; i < sourceDateList.size(); i++) {
            String sortLetters = sourceDateList.get(i).getSortLetters();
            if(!chart.equals(sortLetters)){
                chart= sortLetters;
                sparseArray.put(i,sortLetters);
            }
        }
    }

    /**
     * 获取指定索引的拼音首字母
     * @param position 索引位置
     * @return
     */
    public String getSortLetterTitle(int position){
        return sparseArray.get(position);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 过滤文本(搜索关键字)
     */
    public <T extends SortModel> List<T > getData(String filterStr) {
        List<?extends SortModel > filterDateList =getData(sourceDateList,filterStr);
        updateSourceData(filterDateList);
        return (List<T>) filterDateList;
    }

    public static  <T extends SortModel> List<T > getData( List<T > sourceDateList,String filterStr){
        List<T > filterDateList = new ArrayList<T>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList.addAll((Collection<? extends T>) sourceDateList);
        } else {
            filterDateList.clear();
            if(sourceDateList!=null)
                for (SortModel sortModel : sourceDateList) {
                    String name = String.valueOf(sortModel);
                    if (name.toLowerCase().contains(filterStr.toString().toLowerCase()) || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add((T) sortModel);
                    }
                }
        }
        SideBarUtils.filledData(filterDateList);
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        return filterDateList;
    }

    /**
     * 获取指定数据源中指定索引的实体类
     * @param position
     * @param <T>
     * @return
     */
    public <T extends SortModel> T getItem(int position){
        if(currentDateList!=null && position<currentDateList.size() && position>=0)
        return (T) currentDateList.get(position);
        return null;
    }

    /**
     * 获取section对应的第一个列表项
     * 快速定位的首字母
     * @param section 快速定位组件中触摸到的字母所在的索引位置
     * @return
     */
    public int getPositionForSection(int section) {
        for(int i = 0; i < currentDateList.size(); ++i) {
            String sortStr = ((SortModel)currentDateList.get(i)).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if(firstChar == section) {
                return i;
            }
        }

        return -1;
    }
    /**
     * 获取position所在的section位置
     * 快速定位的首字母
     * @return
     */
    public int getSectionForPosition(int position) {
        return ((SortModel)currentDateList.get(position)).getSortLetters().charAt(0);
    }

    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        return sortStr.matches("[A-Z]")?sortStr:"#";
    }
}
