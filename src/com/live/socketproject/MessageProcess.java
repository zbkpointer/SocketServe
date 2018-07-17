package com.live.socketproject;

/**
 * Created by Administrator on 2018/6/15.
 */

public class MessageProcess {

    public static String tranCategoryMessage(String str){
            String alertMessage =null ;
            if (str==null||str.equals("")) {
                alertMessage="正常";
            }
            if (Integer.valueOf(str)==1){
                alertMessage="气体报警";
            }else if (Integer.valueOf(str)==2){
                alertMessage="火警";
            }else if (Integer.valueOf(str)==3){
                alertMessage="入侵报警";
            }


        return alertMessage;
    }
    public static String tranLevelMessage(String str){
            String alertLevel =null;
            if (str==null||str.equals("")) {
                alertLevel = "正常";
            }
            if (Integer.valueOf(str) == 1) {
                alertLevel = "低级";
            } else if (Integer.valueOf(str) == 2) {
                alertLevel = "中级";
            } else if (Integer.valueOf(str) == 3) {
                alertLevel = "高级";
            }

            return alertLevel;
    }

}
