package com.custom.dictionaries;

/**
 * ResultObject 响应体
 *
 * @Author Sunset
 * @Date 2021-11-08 17:14
 * @Version 1.8
 */
public class ResultObject {
    /**
     * 响应编号
     */
    private String code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数值
     */
    private Object data;

    private ResultObject() {
    }

    private ResultObject(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }



    public static class Build{

        /**
         * 默认响应
         * @return 响应对象
         */
        public static ResultObject defaultResult(){
            return new ResultObject("0","default",null);
        }

        /**
         * 成功响应
         * @return 响应对象
         */
        public static ResultObject successResult(){
            return new ResultObject("200","Success",null);
        }

        /**
         * 警告响应
         * @return 响应对象
         */
        public static ResultObject warningResult(){
            return new ResultObject("500" , "Warning",null);
        }

        /**
         * 异常响应
         * @return 响应对象
         */
        public static ResultObject errorResult(){
            return new ResultObject("600" , "Error",null);
        }

        /**
         * 信息响应
         * @param code 响应编号
         * @param message 响应信息
         * @return 响应对象
         */
        public static ResultObject messageResult(String code , String message){
            return new ResultObject(code,message,null);
        }

        /**
         * 数据响应
         * @param code 响应编号
         * @param message 响应信息
         * @param data 响应数据
         * @return 响应对象
         */
        public static ResultObject dataResult(String code , String message , Object data){
            return new ResultObject(code,message,data);
        }

    }


}
