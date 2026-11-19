package com.xny.common.utils;

import java.util.concurrent.ExecutorService;

/**
 * 公共工具类
 */
public class CommonUtils {

    /**
     * 关闭线程池
     */
    public static void closeExecutor(ExecutorService cachePool){
        cachePool.shutdown();
        while (true){
            if(cachePool.isTerminated()){
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
