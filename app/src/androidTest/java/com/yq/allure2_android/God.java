package com.yq.allure2_android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

/**
 * Created by king on 2016/9/5.
 */
public class God {
    private static String TAG = "jlc_"+ God.class.getSimpleName();
    public static Context ctx = InstrumentationRegistry.getContext();
    public static Resources res = ctx.getResources();

    /**
     * get context
     * @return
     */
    public static Context getContext(){
        return ctx;
    }

    /**
     * return resources
     * @return
     */
    public static Resources getResources(){
        return res;
    }

    /**
     * 通过R文件中的string的id 返回该string 在string.xml中当前语言环境中的值
     * @param str
     * @return
     */
    public static String getString (int str){
        return res.getString(str);
    }
    public static String getString (String str){
        return str;
    }
    /**
     * 如果R文件匹配失败则返回默认字符串
     * @param defaultStr
     * @param str
     * @return
     */
    public static String getString (String defaultStr , int str){
        try {
            return res.getString(str);
        } catch (Resources.NotFoundException e) {
            return defaultStr;
        }
    }

    /**
     * 将strings.xml中的占位符进行匹配转换
     * 注: 需将strings.xml 中的自定义占位符替换为 %s (即java通用占位符 )
     * @param defaultStr 默认字符串
     * @param str strings.xml 中的 name
     * @param formatString 需要替换成的字符串
     * @return
     */
    public static String getStringFormat (String defaultStr , int str , String formatString){
        return String.format(getString(defaultStr , str) ,formatString);
    }
    /**
     * get ActivityManager
     * @param context
     * @return
     */
    public static ActivityManager getActivityManager(Context context){
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * get Running Task Info List
     * @param activityManager
     * @return
     */
    public static List<ActivityManager.RunningTaskInfo> getRunningTaskInfoList(ActivityManager activityManager){
        return activityManager.getRunningTasks(Integer.MAX_VALUE);
    }

    /**
     * 获得栈顶activity的相关描述
     * @param RunningTaskInfoList
     * @return 字符串结果,like this:"ComponentInfo{com.trubuzz.trubuzz/com.trubuzz.roy.SplashActivity}"
     */
    public static String getTopActivityNameInfo(List<ActivityManager.RunningTaskInfo> RunningTaskInfoList){
        return RunningTaskInfoList.get(0).toString();
    }

    /**
     * 返回给定全类名的Class对象 (已处理异常)
     * @param className
     * @return
     */
    public static Class<?> getFixedClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActivityTestRule getActivityTestRule(String activityName) {
        return new ActivityTestRule(getFixedClass(activityName));
    }
    /**
     * 获得当前Activity对象
     * @param instrumentation
     * @return
     */
    public static Activity getCurrentActivity(Instrumentation instrumentation) {
        final Activity[] currentActivity = new Activity[1];
        instrumentation.runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    Activity a = (Activity) resumedActivities.iterator().next();
                    Log.i("jlc_resumedActivities",a.toString());
                    currentActivity[0] = a;
                }
            }
        });
        return currentActivity[0];
    }

    /**
     * 获得当前activity的name
     * @param instrumentation
     * @return
     */
    public static String getCurrentActivityName(Instrumentation instrumentation){
        return getCurrentActivity(instrumentation).getLocalClassName();
    }

    /**
     * 获得top activity的全类名
     * @param context
     * @return top activity class name
     */
    public static String getTopActivityName(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        String cmpNameTemp = null;
        if (null != runningTaskInfos)
        {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).toString();
            Log.w("jlc_topActivity",cmpNameTemp);
            Log.w("jlc_2Activity",(runningTaskInfos.get(1).topActivity).toString());
        }

        if (null == cmpNameTemp)
        {
            return "";
        }

        return cmpNameTemp.split(Pattern.quote("/"))[1].split(Pattern.quote("}"))[0];
    }


    public static String getAppName(Activity activity){
        String s = "";
        try {
            PackageManager pm = activity.getPackageManager();
            s = activity.getApplicationInfo().loadLabel(pm).toString();
        } catch (Exception e) {
            Log.e(TAG, "getAppName: ",e );
        }
        Log.i(TAG, String.format("getAppName: current app name = %s", s));
        return s;
    }

    /**
     * 格式化时间
     * @param d Date or long
     * @param format like : "yyyy-MM-dd HH:mm:ss"
     * @param locale 时区
     * @return
     */
    public static String getDateFormat(Object d , String format, Locale locale){
        return new SimpleDateFormat(format, locale).format(d);
    }

    /**
     * 格式化时间
     * @param d Date or long
     * @param format
     * @return
     */
    public static String getDateFormat(Object d , String format){
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 默认使用中国传统格式
     * @param d
     * @return
     */
    public static String getDateFormat(Object d){
        return getDateFormat(d , "yy/MM/dd HH:mm:ss:SSS" , Locale.CHINA);
    }

    public static <T> T[] list2array(Class clz , List<T> list){
        int length = list.size();
        T [] ts = (T[]) Array.newInstance(clz , length);
        return list.toArray(ts);
    }
    public static <T> List<T> array2list(T [] arrays){
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, arrays);
        return list;
    }

    /**
     * 根据起始位置截取字符串
     * @param str
     * @param firstIndex
     * @param endIndex
     * @return 如果字符串小于end index 则返回本身
     */
    public static String getCutString(String str , int firstIndex , int endIndex){
        return str.length()< endIndex ? str : str.substring(firstIndex , endIndex);
    }
    public static String getCutString(String str , int endIndex){
        return getCutString(str , 0 , endIndex);
    }

    /**
     * 字符串收尾置换
     * @param str
     * @return
     */
    public static String getHead2EndString(String str){
        char[] chars = str.toCharArray();
        char tmp = chars[0];
        chars[0] = chars[chars.length-1];
        chars[chars.length-1] = tmp;
        return String.valueOf(chars);
    }

    /**
     * 获得屏幕可见区域的矩形( 去除status bar And action bar )
     * @param view
     * @return
     */

    /**
     * 遍历 view 树 , 从ViewGroup 中获取匹配的 view
     * @param view
     * @param matcher
     * @return
     */
    public static View getMatchedView(View view , org.hamcrest.Matcher<View> matcher) {
        checkNotNull(view);
        if (matcher.matches(view)) {
            return view;
        }
        ViewGroup parent;
        if (!(view instanceof ViewGroup))   return null;

        parent = (ViewGroup) view;
        for(int i=0; i<parent.getChildCount(); i++) {
            View childView = parent.getChildAt(i);
            if (matcher.matches(childView)) {
                return childView;
            }
            View matchedView = getMatchedView(childView, matcher);
            if(matchedView != null) return matchedView;
        }
        return null;
    }



    /**
     * 获取一个随机整数
     * @param max
     * @param min
     * @return
     */
    public static int getRandomInt(int max, int min) {
        if(max == 0){
            return 0;
        }
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    /**
     * 获取一个指定长度的登录密码
     * @param length 长度不小于3 ,小于3 则默认赋值为6
     * @return
     */
    public static String getRandomLoginPwd(int length) {
        if(length < 3)  length = 6;
        char[] res = new char[length];
        res[0] = (char) getRandomInt(112, 97);
        res[1] = (char) getRandomInt(90, 65);
        res[2] = (char) getRandomInt(57, 49);
        for(int i=3; i<length; i++) {
            res[i] = getRandomChar();
        }
        String pwd = new String(res);
        Log.i(TAG, String.format("getRandomLoginPwd: %s", pwd));
        return pwd;
    }

    /**
     * 获取指定长度的随机交易密码
     *      长度不小于4
     * @param length
     * @return
     */
    public static String getRandomTradePwd(int length) {
        if(length < 4)  length = 4;
        char[] res = new char[length];
        for(int i=0; i<length; i++) {
            res[i] = getRandomChar();
        }
        String pwd = new String(res);
        Log.i(TAG, String.format("getRandomTradePwd: %s", pwd));
        return pwd;
    }

    /**
     * 随机获取一个数字/大写字母或小写字母
     *      1 : 数字
     *      2 ; 大写字母
     *      3 : 小写字母
     * @return
     */
    public static char getRandomChar(){
        int i = getRandomInt(3, 1);
        switch (i) {
            case 1:
                return (char) getRandomInt(57, 49);
            case 2:
                return (char) getRandomInt(90, 65);
            default:
                return (char) getRandomInt(112, 97);
        }
    }
    /**
     * 从字符串中获取连续个数的数字
     * @param str
     * @param max
     * @return
     */
    public static String getNumberFromString(String str, int max) {
        String regEx = String.format("\\d{%s}",max);
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "0";
    }

    /**
     * 从字符串中获取连续数字
     * @param str
     * @return
     */
    public static String getNumberFromString(String str) {
        Pattern p = Pattern.compile("[0-9]\\d*");
        Matcher matcher = p.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return "0";
    }

    public static String getIntervalString(String str, char start, char stop) {
        char[] chars = str.toCharArray();
        StringBuffer sbf = new StringBuffer();
        boolean canStart = false;
        for (char ch : chars) {
            if (ch == start) {
                canStart = true;
                continue;
            }
            if (ch == stop) {
                return new String(sbf);
            }
            if (canStart) {
                sbf.append(ch);
            }
        }
        return new String(sbf);
    }
}
