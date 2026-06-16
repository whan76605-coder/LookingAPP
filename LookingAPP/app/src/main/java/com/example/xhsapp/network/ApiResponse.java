package com.example.xhsapp.network;

/**
 * API 响应基础包装。
 * 后端就绪后只需修改 BASE_URL 和响应字段映射即可。
 */
public class ApiResponse<T> {
    public int code;
    public String message;
    public T data;

    public boolean isSuccess() {
        return code == 200;
    }
}
