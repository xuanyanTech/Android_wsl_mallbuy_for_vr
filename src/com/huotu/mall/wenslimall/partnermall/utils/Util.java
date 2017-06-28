package com.huotu.mall.wenslimall.partnermall.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.huotu.mall.wenslimall.partnermall.config.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by Administrator on 2015/9/19.
 */
public class Util {


    /***
     * 检索手机号码是否合法
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(phone);
        //logger.info(m.matches()+"---");
        return m.matches();
    }

    public static String opeDouble(double num) {
        String num2 = String.valueOf(num);
        int last = num2.lastIndexOf(".") + 3;
        last = last > num2.length() ? num2.length() : last;
        String result = subZeroAndDot(num2.substring(0, last));
        return TextUtils.isEmpty(result) ? "0" : result;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        return bitmap2Bytes(bm, false);
    }

    public static byte[] bitmap2Bytes(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] result = baos.toByteArray();

        if (needRecycle) {
            bmp.recycle();
        }

        try {
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean classIsTop(Activity activity) {

        String packageName = activity.getLocalClassName();
        Log.i( "error" , ">>>>" + packageName);
        ActivityManager activityManager = (ActivityManager) (activity
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        for (ActivityManager.RunningTaskInfo item : rti) {
            if (item.topActivity.getClassName().endsWith(packageName))
                return true;
        }
        return false;

    }

    public static boolean isAppRunning(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo item : rti) {
            if (item.topActivity.getClassName().startsWith(packageName))
                return true;
        }
        return false;
    }

    public static boolean isTopActivity(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        if (rti != null && rti.size() > 0) {
            System.out.println("----------包名---------" + rti.get(0).topActivity.getPackageName());
            //应用程序位于堆栈的顶层
            if (packageName.equals(rti.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopActivityByName(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
        if (rti != null && rti.size() > 0) {
            System.out.println("----------activityname---------" + rti.get(0).topActivity.getClassName());
            //应用程序位于堆栈的顶层
            if (className.equals(rti.get(0).topActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityLoaded(Context context) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo item : rti) {
            if (item.baseActivity.getClassName().startsWith(
                    packageName + ".LoadingActivity")
                    || item.baseActivity.getClassName().startsWith(
                    packageName + ".MainActivity")
                    || item.topActivity.getClassName().startsWith(
                    packageName + ".MainActivity"))
                return true;
        }
        return false;
    }

    public static boolean isAppLoaded(Context context, String className) {
        String packageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(30);
        for (ActivityManager.RunningTaskInfo item : rti) {
            if (item.baseActivity.getClassName().startsWith(className)
                    || item.topActivity.getClassName().startsWith(className))
                return true;
        }
        return false;
    }

    /**
     * 返回指定范围的随机数(m-n之间)的公式[1] ：Math.random()*(n-m)+m；
     */
    public static int random(int m, int n) {
        return (int) (Math.random() * (n - m) + m);
    }

    public static String getNumber(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static byte[] InputStreamToByte(InputStream is) {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        byte[] imgdata = null;
        try {
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            imgdata = bytestream.toByteArray();
            bytestream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return imgdata;
    }

    // public static boolean isEmptyMd5(String content){
    // if(TextUtils.isEmpty(content)){
    // return true;
    // }
    // if(content.equals(SecurityUtil.MD5Encryption(""))){
    // return true;
    //
    // }
    // return TextUtils.isEmpty(content) ||
    // content.equals(SecurityUtil.MD5Encryption(""));
    // }

    // a weak key

    private static String encoding = "UTF-8";

    // 密钥
    private String sKey = "testtest";

    private static byte[] iv =
            {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 加密字符串
     */
    // public String ebotongEncrypto(String str) throws Exception {
    // IvParameterSpec zeroIv = new IvParameterSpec(iv);
    //
    // SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "DES");
    //
    // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    //
    // cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
    //
    // byte[] encryptedData = cipher.doFinal(str.getBytes());
    //
    // return Base64.encode(encryptedData);
    // }

    // public String decryptDES(String decryptString)
    // throws Exception {
    // byte[] byteMi = Base64.decode(decryptString);
    // IvParameterSpec zeroIv = new IvParameterSpec(iv);
    // SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "DES");
    // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    // cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
    // byte decryptedData[] = cipher.doFinal(byteMi);
    // return new String(decryptedData);
    // }

    /**
     * 解密字符串
     */
    // public String ebotongDecrypto(String str) {
    // String result = str;
    // if (str != null && str.length() > 0) {
    // try {
    // byte[] encodeByte = Base64.decode(str);
    //
    // byte[] decoder = symmetricDecrypto(encodeByte);
    // result = new String(decoder, encoding);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return result;
    // }

    /**
     * 对称解密字节数组并返回
     *
     * @param byteSource 需要解密的数据
     * @return 经过解密的数据
     * @throws Exception
     */
    public byte[] symmetricDecrypto(byte[] byteSource) throws Exception {
        try {
            int mode = Cipher.DECRYPT_MODE;
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            byte[] keyData = sKey.getBytes();
            DESKeySpec keySpec = new DESKeySpec(keyData);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(mode, key);
            byte[] result = cipher.doFinal(byteSource);
            return result;
        } catch (Exception e) {
            throw e;
        } finally {

        }
    }

    /**
     * 对称加密字节数组并返回
     *
     * @param byteSource 需要加密的数据
     * @return 经过加密的数据
     * @throws Exception
     */
    public byte[] symmetricEncrypto(byte[] byteSource) throws Exception {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            byte[] keyData = sKey.getBytes();
            DESKeySpec keySpec = new DESKeySpec(keyData);
            Key key = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] result = cipher.doFinal(byteSource);

            return result;
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    // public static byte[] readFromFileByWX(String path){
    // File file = new File(path);
    // if(!file.exists())
    // return null;
    // //限制内容大小不超过32KB
    // if(file.length() > 32 * 1024){//压缩图片
    // return compressImageBypath(path);
    //
    // }else{
    // return readFromFile(path, 0, (int)file.length());
    // }
    //
    //
    //
    // }
    // private static byte[] compressImageBypath(String path) {
    // Bitmap bitmap = ImageUtil.readBitmapByPath(path);
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // bitmap.compress(Bitmap.CompressFormat.PNG, 100,
    // baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    // int options = 100;
    // while ( baos.toByteArray().length / 1024>32) {
    // //循环判断如果压缩后图片是否大于32kb,大于继续压缩
    // baos.reset();//重置baos即清空baos
    // bitmap.compress(Bitmap.CompressFormat.PNG, options,
    // baos);//这里压缩options%，把压缩后的数据存放到baos中
    // options -= 10;//每次都减少10
    // }
    // // ByteArrayInputStream isBm = new
    // ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
    // // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
    // null);//把ByteArrayInputStream数据生成图片
    // return baos.toByteArray();
    //
    // }

    /**
     * 微信压缩图片
     *
     * @param srcPath
     */
    public static void compressImage(String srcPath) {
        File file = new File(srcPath);
        if (file.length() < 32 * 1024)
            return;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;// 只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 104f;//
        float ww = 104f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        compressBmp(srcPath, bitmap);

    }

    private static void compressBmp(String path, Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > 30) {
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // ByteArrayInputStream isBm = new
        // ByteArrayInputStream(baos.toByteArray());
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 微信读图
     *
     * @param fileName
     * @param offset
     * @param len
     * @return
     */
    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        if (len == -1) {
            len = (int) file.length();
        }

        if (offset < 0) {
            return null;
        }
        if (len <= 0) {
            return null;
        }
        if (offset + len > (int) file.length()) {
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean isChinese(String userName) {
        // 纯中文
        String chinese = "^[\\u4E00-\\u9FA5\\uF900-\\uFA2D]+$";
        /** 这个正则表达式用来判断是否为中文 **/
        Pattern pattern = Pattern.compile(chinese);
        return pattern.matcher(userName).matches();
    }

    public static String userPwdIsLegal(String pwd) {
        if (TextUtils.isEmpty(pwd))
            return "不能为空";
        if (pwd.length() < 6 || pwd.length() > 12) {// 6-12
            return "长度为6-12";
        }
        return "success";
    }

    public static String userNameIsLegal(String userName) {
        if (TextUtils.isEmpty(userName))
            return "不能为空";
        if (userName.matches("[0-9]+"))// 纯数字
            return "不能为纯数字";
        // if(userName.matches("^[a-zA-Z]\\w{3,11}[a-zA-Z\\d\\_\\@\\.]$"))//字母和数字
        // _ -组合 3-20字母开头
        // return true;
        String head = userName.charAt(0) + "";
        if (!head.matches("[a-zA-Z]"))// 字母和数字 _ -组合 3-20字母开头
            return "请以字母开头";
        char[] chars = userName.toCharArray();
        int size = chars.length;
        for (int i = 0; i < size; i++) {
            String item = chars[i] + "";
            if (!item.matches("[a-zA-Z\\d\\_\\@\\.]$")) {
                return "不能包含特殊字符\"" + item + "\"";
            }

        }
        if (size < 3 || size > 20) {
            return "长度为3－20";
        }
        // if(!userName.matches("[a-zA-Z\\d\\_\\@\\.]$"))//字母和数字 _ -组合 3-20字母开头
        // return "不能包含特殊字符";
        return "success";

    }

    public static String MoneyFormat(int money) {
        // 如果要小数点前面保留0则DecimalFormat bf = new DecimalFormat( "###,##0.##" );
        DecimalFormat bf = new DecimalFormat("###,###.##");
        return bf.format(money);
    }

    public static boolean isSameDay(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        try {
            Date today = sdf.parse(time1);
            Date otherDay = sdf.parse(time2);
            int temp = Integer.parseInt(sdf.format(today))
                    - Integer.parseInt(sdf.format(otherDay));
            return temp == 0 ? true : false;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param time2
     * @return
     */
    public static String DateFormatFull(String time2) {
        // H 一天中的小时数（0-23） Number 0
        // k 一天中的小时数（1-24） Number 24
        // K am/pm 中的小时数（0-11） Number 0
        // h am/pm 中的小时数（1-12） Number 12
        if (TextUtils.isEmpty(time2))
            return "未知";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
                Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        try {
            Date date = sdf.parse(time2);
            String day = sdf2.format(date);
            return day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    /**
     * yyyy-MM-dd
     *
     * @param time2
     * @return
     */
    public static String DateFormat(String time2) {
        if (TextUtils.isEmpty(time2))
            return "未知";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date otherDay = sdf.parse(time2);
            String date = sdf.format(otherDay);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    public static int getDayCount(String time2) {
        if (TextUtils.isEmpty(time2)) {
            return -1;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        try {
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = sdf.parse(time2);
            int temp = Integer.parseInt(sdf2.format(today))
                    - Integer.parseInt(sdf2.format(otherDay));
            return temp;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return -1;
    }

    public static String getDayDisDes(String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
                Locale.CHINA);
        try {
            Date otherDay = sdf.parse(time2);
            long dis = (System.currentTimeMillis() - otherDay.getTime()) / 1000;// 除以1000是为了转换成秒
            long day = dis / (24 * 3600);
            if (day != 0)
                return day + "天前";
            long hour = dis % (24 * 3600) / 3600;
            if (hour != 0)
                return hour + "小时前";
            long minute = dis % 3600 / 60;
            if (minute != 0)
                return minute + "分钟前";
            long second = dis % 60 / 60;
            return second + "秒前";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "未知";
    }

    @SuppressWarnings("deprecation")
    public static String getDay(String timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.CHINA);
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));

        switch (temp) {
            case 0:
                result = "今天";
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "前天";
                break;

            default:
                result = temp + "天前";
                break;
        }

        return result;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            // int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists())// 文件不存在时
                return false;
            inStream = new FileInputStream(oldPath); // 读入原文件
            outStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            // int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                // bytesum += byteread; // 字节数 文件大小
                // System.out.println(bytesum);
                outStream.write(buffer, 0, byteread);
                outStream.flush();
            }
            return true;
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static boolean WxAttention(final Context context, final String openId) {
        if (context == null)
            return false;
        IWXAPI api = WXAPIFactory.createWXAPI(
                context,
                Constants.WXPAY_ID, true
        );
        if (!api.isWXAppInstalled()) {
            return false;
        }
        try {
            Intent localIntent2 = Intent
                    .parseUri(
                            "#Intent;action="
                                    + openId
                                    + ";launchFlags=0x4000000;component=com.tencent.mm/.ui.LauncherUI;B.LauncherUI_From_Biz_Shortcut=true;end",
                            0);
            localIntent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(localIntent2);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static int getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Calendar calendar = new GregorianCalendar();
        return Integer.valueOf(sdf.format(calendar.getTime()));
    }

    public static int getIntTime(String time) {
        int result = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            result = Integer.valueOf(sdf1.format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;

    }

    public static boolean isPhoneNum(String phone) {
        if (TextUtils.isEmpty(phone))
            return false;
        if (phone.length() != 11)
            return false;
        return true;

    }

    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected() && info.isAvailable()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Log.v("error", e.toString());
        }
        return false;
    }

    public static Bitmap readBitmapByPath(String path) {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inInputShareable = true;
        bfOptions.inTempStorage = new byte[32 * 1024];

        File file = new File(path);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
            if (fs != null)
                return BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
                        bfOptions);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * @throws
     * @方法描述：浮点数精确，5.0精确成5，5.12 精确成5.1
     * @方法名：decimalFloat
     * @参数：@param value
     * @返回：void
     */
    public static Number decimalFloat(float value, String precision) {
        Float fv = new Float(value);
        int i = fv.intValue();
        boolean isDigital = (value == i);
        if (isDigital) {
            // 返回整形
            return i;
        } else {
            DecimalFormat decimalFormat = new DecimalFormat(precision);//构造方法的字符格式
            // 精确1位小数
            BigDecimal d = new BigDecimal(fv);
            d.setScale(3, BigDecimal.ROUND_DOWN);
            String str = decimalFormat.format(d.doubleValue());//format 返回的是字符串
            return Float.parseFloat(str);
        }
    }

    /**
     * @throws
     * @方法描述：判断是否为MB还是GB
     * @方法名：isM
     * @参数：@param m
     * @参数：@return
     * @返回：boolean
     */
    public static boolean isM(String m) {
        float f = Float.parseFloat(m);
        if (f < 1000) {
            return true;
        } else if (f >= 1000) {
            return false;
        }
        return false;
    }

    public static boolean isM1(BigDecimal m) {
        BigDecimal standard = BigDecimal.valueOf(1000);
        if (-1 == m.compareTo(standard)) {
            return true;
        } else if (0 == m.compareTo(standard) || 1 == m.compareTo(standard)) {
            return false;
        }
        return false;

    }

    /**
     * @throws
     * @方法描述：获取奖励信息
     * @方法名：getReward
     * @参数：@param str
     * @参数：@return
     * @返回：String
     */
    public static String getReward(String str) {
        str = str.trim();
        StringBuilder builder = new StringBuilder();
        if (str != null && !"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    builder.append(str.charAt(i));
                }
            }
        }

        return builder.toString();
    }


    /**
     * 计算签到信息
     */
    public static int calSignIn(int signInfo) {
        // 判断今天是星期几
        Calendar mCalendar = Calendar.getInstance();
        int curWeekOfDay = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        curWeekOfDay = curWeekOfDay == 0 ? 7 : curWeekOfDay;
        int weekIndex = curWeekOfDay - 1;
        int week = curWeekOfDay;
        int sign = 0x1;
        int status = 0;// signInfo >> (6 - weekIndex) & sign;
        int signCount = 0;

//        curWeekOfDay= 1;
//        week=1;
//        weekIndex = 0;

        for (int i = 0; i <= weekIndex; i++) {
            status = signInfo >> (6 - weekIndex + i) & sign;
            if (status == 0) break;
            signCount++;
        }
        if (signCount == week) {
            for (int i = 0; i < (6 - weekIndex); i++) {
                status = signInfo >> (i) & sign;
                if (status == 0) break;
                signCount++;
            }
        }
        return signCount;
    }


    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
//每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
//解析出键值
            if (arrSplitEqual.length > 1) {
//正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
//只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    public static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url请求的路径，包括页面
     *
     * @param strURL url地址
     * @return url路径
     */
    public static String UrlPage(String strURL) {
        String strPage = null;
        String[] arrSplit = null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 0) {
            if (arrSplit.length > 1) {
                if (arrSplit[0] != null) {
                    strPage = arrSplit[0];
                }
            }
        }
        return strPage;
    }
}
