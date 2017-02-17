package com.tron.huanxindemo.model;

import android.content.Context;
import android.util.Log;

import com.tron.huanxindemo.model.dao.AccountDao;
import com.tron.huanxindemo.model.db.DBManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tron on 2017/2/15.
 *
 * 全局数据处理类
 *
 */

public class Model {

    /**
     * 单例模式
     */
    // 1.私有化构造器
    private Model(){
    }

    // 2.创建一个静态变量
    private static Model model = new Model();

    // 3.创建一个静态的公共方法返回实例, 只能通过此方法获得Model的实例
    public static Model getInstance(){
        return model;
    }


    private AccountDao accountDao;
    private Context context;
    private DBManager dbManager;

    public void init(Context context){
        this.context = context;

        // 创建AccountDb数据库
        accountDao = new AccountDao(context);

        // 初始化全局监听
        new GlobalListener(context);
    }

    /**
     * 线程池分为四种
     *
     * 1.缓存线程池  有多少可以开启多少
     * 2.定长线程池  固定大小
     * 3.调度线程池  可以延时周期执行
     * 4.单例线程池  单个
     */
    private ExecutorService thread = Executors.newCachedThreadPool();

    public ExecutorService getGlobalThread(){
        return thread;
    }

    public AccountDao getAccountDao(){
        return accountDao;
    }

    public void loginSuccess(String currentUser){

        if (dbManager != null){
            dbManager.close();
        }

        dbManager = new DBManager(context, currentUser + ".db");

        Log.e("TAG", "dbManager=====" + dbManager);
    }

    public DBManager getDBManager(){
        return dbManager;
    }
}
