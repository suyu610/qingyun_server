package com.qdu.qingyun.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName StringUtil
 * @Description 一些字符串相关的工具方法
 * @Author 23580
 * @Date 2021/6/8 14:27
 * @Version 1.0
 **/
public class StringUtil {
    /*
     * @Description 1级分类：AA0000 2级分类 AABB00  3级分类 AABBCC
     * @Date 2021/6/8 14:27
     * @Param id[int]
     * @return 向后补齐0，如果是个位数，前面也要补个0
     **/
    public static String formatCategory(int id, int length, int bodyLength) {
        String str = String.format("%0" + bodyLength + "d", id);
        StringBuffer buffer = new StringBuffer(str);
        if (buffer.length() >= length) {
            return buffer.toString();
        } else {
            while (buffer.length() < length) {
                buffer.append("0");
            }
        }
        return buffer.toString();
    }

    /*
     * @Description 判断链接是否为图片
     * @Date 2021/6/13 2:51
     * @Param Url
     * @return boolean
     **/
    public static Boolean isPicUrl(String url) {
        String reg = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url.toLowerCase());
        return matcher.find();
    }

    /*
     * @Description 判断链接是否为可预览的文档
     * @Date 2021/6/13 2:51
     * @Param Url
     * @return boolean
     **/
    public static Boolean isDocUrl(String url) {
        String reg = ".+(.pptx|.ppt|.pot|.potx|.pps|.ppsx|.dps|.dpt|.pptm|.potm|.ppsm|.doc|.dot|.wps|.wpt|.docx|.dotx|.docm|.dotm|.xls|.xlt|.et|.ett|.xlsx|.xltx|.csv|.xlsb|.xlsm|.xltm|.ets|.pdf|.lrc|.c|.cpp|.h|.asm|.s|.java|.asp|.bat|.bas|.prg|.cmd|.rtf|.txt|.log|.xml|.htm|.html)$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url.toLowerCase());
        return matcher.find();
    }


    /**
     * 获取汉字串拼音首字母，英文字符不变
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {

        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {

            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null && temp.length > 0){
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[0]);
            }
            break;
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    public static void main(String[] args) {
        LogUtil.GetLog().info(StringUtil.isDocUrl("https://www.baidu.com/a.pdf"));

    }
}
