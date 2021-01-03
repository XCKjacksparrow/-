package cn.bestrivenlf.electronadmin.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: liufan
 * @Date: 2019/4/17 10:36
 * @Description:
 */
@Component
public class MyUtil {
//    @Value("${file.uploadAbsolutePath}")
//    private String absolutePath;
    /**
     * 给定pattern格式化日期
     * @param date
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String FormatDate(Date date, String pattern)  {
        SimpleDateFormat sdf =new SimpleDateFormat( pattern );
        Date d= new Date();
        String str = sdf.format(d);
        return str;
    }

    /**
     * 默认格式化日期
     * @param date
     * @return
     */
    public static Date FormatDate(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf =new SimpleDateFormat( pattern );
        String str = sdf.format(date);
        Date d = null;
        try {
            d = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * JSON对象转Map对象
     * @param jsonObject
     * @return
     */
    public static Map<String,Object> parseToMap (JSONObject jsonObject){
        Map<String,Object> map = new HashMap<>();
        for(String key:jsonObject.keySet()){
            map.put(key,jsonObject.get(key));
        }
        return map;
    }

    /**
     * 获得一个随机的uuid 32位
     * @return
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * 获取文件的md5值
     * @param upload
     * @return
     * @throws Exception
     */
    public static String getMd5(MultipartFile upload) throws Exception {
        byte[] uploadBytes = upload.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(uploadBytes);
        String hashString = new BigInteger(1, digest).toString(16);
        return hashString.toUpperCase();
    }

    /**
     * 保存文件
     * @param fileEntities
     * @return
     */
//    public static Boolean saveFile(List<FileEntity> fileEntities) {
//        for (FileEntity file : fileEntities) {
//            InputStream inputStream = file.getFileStream();
//            OutputStream outputStream = null;
//            if (null != inputStream) {
//                try {
//                    //1、创建空文件
//                    File tempFile = new File(file.getAbsolutePath());
//                    if (!tempFile.exists()) {
//                        tempFile.createNewFile();
//                    }
//                    //2、获取输出流
//                    outputStream = new FileOutputStream(tempFile);
//                    //3、字节流转byte
//                    byte[] temp = new byte[1024];
//                    int length = 0;
//                    while ((length = inputStream.read(temp)) != -1) {
//                        // 目标文件写入一部分内容
//                        outputStream.write(temp, 0, length);
//                    }
//
//                    CompressImgUtil.generateCompressImg(0.8f,file.getAbsolutePath().split(file.getRandomName())[0],file
//                                    .getRandomName()
//                                    .split(file
//                                    .getResourceType())
//                                    [0],
//                            file.getAbsolutePath());
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                } finally {
//                    try {
//                        outputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return false;
//                    } catch (NullPointerException e){
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//
//            }
//        }
//        return true;
//    }

    /**
     * 生成随机码
     * @param n 位数
     * @return
     */
    public static long getRandom(long n) {
        long min = 1,max = 9;
        for (int i = 1; i < n; i++) {
        min *= 10;
        max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
        return rangeLong;
    }

    public static List<Field> getDifferenceField(Field[] a, Field[] b){
        List<Field> difference = new ArrayList<>();
        Field[] first = a.length>b.length?a:b;
        Field[] second = a.length<b.length?a:b;
        //长度长的作为外循环
        for(Field field:first){
            boolean equal = false;
            for(Field f :second){
                if(field.getName().equals(f.getName())){
                    equal = true;
                }
            }
            if(!equal){
                //没有相同的，说明这是不属于交集的部分
                difference.add(field);
            }
        }
        return difference;
    }

    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static Date parseStringToDate(String str,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(str);
        return date;
    }

    public static Date parseStringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(str);
        return date;
    }

    public static String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
    * 将字节数组转换为十六进制字符串
    *
    * @param byteArray
    * @return
    */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
          strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
    * 将字节转换为十六进制字符串
    *
    * @param
    * @return
    */
    public static String byteToHexStr(byte bytes) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(bytes >>> 4) & 0X0F];
        tempArr[1] = Digit[bytes & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 获取ip地址
    * @param request
     * @return
      */
    public static String getIpAddr(HttpServletRequest request) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return request.getRemoteAddr();
        }
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
          }
           ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }

    /**
     * 获取时间戳
     * @return
     */
    public static String getTimestamp(){
        long timestamp = new Date().getTime();
        return Long.toString(timestamp);
    }


    /**
     * 保存文件
     * @param fileEntities
     * @return
     */
    public static Boolean saveFile(List<cn.bestrivenlf.electronadmin.entity.File> fileEntities) {
        for (cn.bestrivenlf.electronadmin.entity.File file : fileEntities) {
            InputStream inputStream = file.getInputStream();
            OutputStream outputStream = null;
            if (null != inputStream) {
                try {
                    //1、创建空文件
                    File tempFile = new File(file.getDiskAddress());
                    if (!tempFile.exists()) {
                        tempFile.createNewFile();
                    }
                    //2、获取输出流
                    outputStream = new FileOutputStream(tempFile);
                    //3、字节流转byte
                    byte[] temp = new byte[1024];
                    int length = 0;
                    while ((length = inputStream.read(temp)) != -1) {
                        // 目标文件写入一部分内容
                        outputStream.write(temp, 0, length);
                    }
                    //写入数据库
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    } catch (NullPointerException e){
                        e.printStackTrace();
                        return false;
                    }
                }

            }
        }
        return true;
    }
}
