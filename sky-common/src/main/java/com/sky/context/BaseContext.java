package com.sky.context;

public class BaseContext {

    // 静态ThreadLocal变量，存储线程独立的Long类型值
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 设置当前线程的ID
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    // 获取当前线程的ID
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    // 移除当前线程的ID
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
