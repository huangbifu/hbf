package com.cnsunrun.common.util;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.sunrun.sunrunframwork.utils.EmptyDeal.empty;
import static com.sunrun.sunrunframwork.utils.EmptyDeal.size;

/**
 * 分页代理类
 * Created by WQ on 2017/5/26.
 */

public class PageLimitDelegate<T> {
    public int page = 1;//当前页
    DataProvider provider;//数据提供者,主要实现loadData 方法即可
    BaseQuickAdapter<T,?> quickAdapter;//适配器
    SwipeRefreshLayout refreshLayout;//下拉刷新控件
    AtomicBoolean isLoadMore=new AtomicBoolean(false);//是否处于加载状态
    int maxPageSize=10;//每页最大值
    public PageLimitDelegate(DataProvider provider) {
        this.provider
                = provider;
    }
    boolean isAttach=false;
    boolean pageEnable=true;
    /**
     * 附加列表信息,设置加载刷新信息
     * @param refreshLayout
     * @param recyclerView
     * @param quickAdapter
     */
    public void attach(SwipeRefreshLayout refreshLayout, RecyclerView recyclerView, BaseQuickAdapter<T,?> quickAdapter) {
        this.quickAdapter=quickAdapter;
        this.refreshLayout=refreshLayout;
        refreshLayout.setEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               refreshPage();
            }
        });
        quickAdapter.setEnableLoadMore(true);
        quickAdapter.disableLoadMoreIfNotFullPage(recyclerView);
        quickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore.set(true);
                page++;
                provider.loadData(page);
            }
        }, recyclerView);
        provider.loadData(page);
        isAttach=true;
    }

    /**
     * 刷新
     */
    public void refreshPage() {
        page = 1;
        isLoadMore.set(false);
        provider.loadData(page);
    }

    /**
     * 设置数据,
     * @param data
     */
    public  void setData(final List<T> data){
        maxPageSize=Math.max(maxPageSize,size(data));
        if((isLoadMore.get() ||page!=1)&&pageEnable) {
            if(empty(data)){
                page--;
                quickAdapter.setEnableLoadMore(false);
                quickAdapter.loadMoreEnd();
            }else {
                quickAdapter.addData(data);
            }
            loadComplete();
        }else {
            loadComplete();
            refreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    quickAdapter.setNewData(data);
                    quickAdapter.setEnableLoadMore(pageEnable&&(size(data)>=maxPageSize));
                }
            },200);

        }
    }
    public void autoStopAnim(){
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        },20);
    }


    /**
     * 加载/刷新完成
     */
    public void loadComplete(){
        isLoadMore.set(false);
        refreshLayout.setRefreshing(false);
        quickAdapter.loadMoreComplete();

    }

    /**
     * 数据提供者接口,用于进行加载动作
     */
    public interface DataProvider {
        public void loadData(int page);
    }


    public static void addData2List(List src, List data) {
        if(data != null) {
            Iterator var3 = data.iterator();

            while(true) {
                while(var3.hasNext()) {
                    Object object = var3.next();
                    int index = src.indexOf(object);
                    if(index != -1) {
                        src.set(index, object);
                    } else {
                        src.add(object);
                    }
                }

                return;
            }
        }
    }

    public boolean isAttach() {
        return isAttach;
    }

    public void setPageEnable(boolean pageEnable) {
        this.pageEnable = pageEnable;
    }
}
