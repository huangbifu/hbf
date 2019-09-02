package com.cnsunrun.common.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 集合工具
 * Created by WQ on 2017/12/1.
 */

public class ListToolLogic {

    /**
     * 指定数字区间的list
     */
    public static class NumberList extends LinkedList<String> {
        protected int begin, end;
        protected int headNumber,footNumber;
        protected String formatStr="%d";

        public NumberList(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public void setFormatStr(String formatStr) {
            this.formatStr = formatStr;
        }



        public NumberList addHead(String...headStr){
            for (String s : headStr) {
                addHeadInner(s);
            }
            return this;
        }
        private NumberList addHeadInner(String headStr){
            if(super.size()==0){
                add(headStr);
            }else {
                add(0, headStr);
            }
            headNumber++;
            return this;
        }
        public NumberList addFoot(String...footStr){
            for (String s : footStr) {
                add(s);
                footNumber++;
            }

            return this;
        }

        @Override
        public String get(int index) {
            if(index>=super.size()){
                String indexContent = String.format(formatStr, index - headNumber + begin);
                add(indexContent);
//                if(super.size()==0){
//
//                }else {
//                    add(footNumber, indexContent);
//                }
            }
            return super.get(index);
        }

        @Override
        public int size() {
            return (end-begin)+headNumber+footNumber;
        }
    }
}
