package com.huotu.mall.wenslimall.partnermall.helper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class Helper {
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {     //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));
    }

    //递归删除文件及文件夹
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getPrimaryClip().getItemAt(0).getText().toString().trim();
    }

    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }

        String regex = "^[a-zA-Z0-9]+$";
        return isDigit && isLetter && str.matches(regex);
    }

    /**
     * 获取系统时
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }


    // 设置过滤字符函数(过滤掉我们不需要的字符)

    public static String stringFilter(String str) throws PatternSyntaxException {

        String regEx = "[/\\:*?<>|\"\n\t]";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);

        return m.replaceAll("");

    }


    public static boolean momentDataAdd(String momentData, String listData) {
        return (dataNum(listData) > dataNum(momentData)) || listData.equals("0");
    }

    private static long dataNum(String data) {
        data = data.replaceAll("-", "");
        data = data.replaceAll(":", "");
        data = data.replaceAll("T", "");
        data = data.indexOf(".") > 0 ? data.substring(0, data.indexOf(".")) : data;
        return Long.valueOf(data);
    }


    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取状态栏 度
     *
     * @param resources
     * @return
     */
    public static int getStatusBarHeight(Resources resources) {
        int result = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

/*
    public static String saveCroppedImage(Bitmap bmp, Context context, String member_id) {
        String user_id = CommonDataUtils.getUserId(context);
        File directory = new File("/sdcard/MLFolder");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File("/sdcard/MLFolder/" + user_id);
        if (!file.exists())
            file.mkdirs();

        file = new File("/sdcard/" + member_id + ".jpg".trim());
        String fileName = file.getName();
        String newFilePath = "/sdcard/MLFolder/" + user_id + "/" + fileName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newFilePath;
    }
*/

    public static String isExist(String path) {
        File file = new File(path);
//判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    //判断路径是否有文件地址，有返回地址,无返回空
    public static String isExistImg(String path) {
        if (path != null) {
            File file = new File(path);
            if (!file.exists()) {
                return "";
            } else {
                return file.getAbsolutePath();
            }
        } else {
            return "";
        }
    }


    //删除文件的方法
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean fileIsExists(String path) {
        File f = new File(path);
        return f.exists();
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

/*
    public static DisplayImageOptions circle = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new CircleBitmapDisplayer())
            .build();
*/

    public static Bitmap drawable2Bitmap(Context context, int rsid, int x, int y) {
        //通过openRawResource获取一个inputStream对象
        InputStream inputStream = context.getResources().openRawResource(rsid);
        //通过一个InputStream创建一个BitmapDrawable对象
        BitmapDrawable drawable = new BitmapDrawable(inputStream);
        //通过BitmapDrawable对象获得Bitmap对象
        Bitmap bitmap = drawable.getBitmap();
        //利用Bitmap对象创建缩略图
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, x, y);
        return bitmap;

    }

    public static Bitmap fullBitmap(Bitmap b, int sw, int sh) {
        int iw = b.getWidth();
        int ih = b.getHeight();

        float scaleWidth = ((float) sw) / iw;    //水平放大比例
        float scaleHeight = ((float) sh) / ih;   //竖直放大比例

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        if (scaleHeight > scaleWidth)  //水平放大较多
        {
            matrix.postScale(scaleHeight, scaleHeight);
            float deltwidth = (iw * scaleHeight - sw) / 2;
            return Bitmap.createBitmap(b, (int) deltwidth, 0, iw, ih, matrix, true);
        } else {
            matrix.postScale(scaleWidth, scaleWidth);
            return Bitmap.createBitmap(b, 0, 0, iw, ih, matrix, true);

        }
    }

    //dp转px
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    //将字符串转换为md5
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 should be supported", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 should be supported", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static boolean isPhoneNumber(String mobiles) {
        // Pattern p = Pattern.compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    public static boolean isLegalPassword(String password) {
        //长度大于6且只能包含数字，字母，下划线
        // Pattern p = Pattern.compile("^[0-9a-zA-z_]{8,}$");
        Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,18}$");

        Matcher m = p.matcher(password);

        return m.matches();
    }

    public static boolean isPassword(String password) {
        //长度大于6且只能包含数字，字母，下划线
        // Pattern p = Pattern.compile("^[0-9a-zA-z_]{8,}$");
        Pattern p = Pattern.compile("^(?=.*[0-9a-zA-Z]).{6,16}$");

        Matcher m = p.matcher(password);

        return m.matches();
    }

    //检测字符串是否为空
    public static boolean isNotNullString(String str) {
        return !(str == null || str.length() == 0);
    }

    //获取时间字符串
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    //获取时间字符串
    public static String newDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getStringTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, min, second);
    }

    //获取时间字符串
    public static String getNewDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd号");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    public static String getCoolPlayDate(int duration) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date curDate = new Date(duration);
        return format.format(curDate);
    }

    //生成时间戳
    public static String dateToStr(Date date, String pattern) {
        if (date == null || date.equals(""))
            return null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }


    //检查数据库是否包含tableName
    public static boolean checkDataBase(String tableName, SQLiteOpenHelper helper) {
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            assert db != null;
            Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
            c.close();
            return false;  //未抛出异常，表明表存在
        } catch (SQLiteException e) {
            return true;   //这里的异常一般为no such table，故返回true(不含该表),未找到其他更合适的方法
        }
    }

    //根据服务器返回值生成提示信息，成功为null
    public static String responseInfo(String status) {
        if (status.charAt(0) == '2') {
            //2 开头表示成功
            return null;
        }
        if (status.equals("0")) return "连接失败";
        if (status.equals("400")) return "错误请求";
        if (status.equals("406")) return "账户号或密码错误";
        if (status.equals("404")) return "错误请求";
        if (status.equals("409")) return "该账户已注册";
        return "连接服务器出错，错误代码:" + status;
    }

    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return ("未获取");
        }
    }


    /**
     * 动态设置ListView的 度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) * 2;
        listView.setLayoutParams(params);
    }


}
