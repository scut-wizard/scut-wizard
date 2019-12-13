package com.scut.scutwizard.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class StringHelper {
    public static String join(CharSequence delimiter, Iterable<String> strings) {
        StringBuilder result = new StringBuilder();
        Iterator<String> it = strings.iterator();
        boolean hn = it.hasNext();
        while (hn) {
            result.append(it.next());
            hn = it.hasNext();
            if (hn)
                result.append(delimiter);
            else
                break;
        }
        return result.toString();
    }
    public String getTodayStrChinese(){
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月dd日");//设置日期时间格式
        String today_str = dateFormat.format(today);
        return today_str;
    }
    public String getTodayStr(){
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期时间格式
        String today_str = dateFormat.format(today);
        return today_str;
    }
    public String getYi(Context context){
        String yi;
        int t;

        String today_str = getTodayStr();
        SharedPreferences pref = context.getSharedPreferences("yi_data",Context.MODE_PRIVATE);
        String temp_str = pref.getString("yi_data","");
        SharedPreferences.Editor editor =  context.getSharedPreferences("yi_data",Context.MODE_PRIVATE).edit();

        if(temp_str.equals(today_str)){
            t=pref.getInt("No.",0);
            //不同一天//第一次
        }else{
            int prev_t=pref.getInt("No.",0);
            editor.putString("date",today_str);
            t = new Random().nextInt(100);
            while((int)(prev_t/10)==(int)(t/10)||(int)(prev_t%10)==(int)(t%10)){
                t = new Random().nextInt(100);
            }
            editor.putString("date",today_str);
            editor.putInt("No.",t);
            editor.apply();
        }


        String[] yi_1={"吃二饭一楼","吃麻辣香锅","吃二饭三楼","吃烧卤","吃一饭二楼","吃清真食堂","吃外卖","吃扬州炒饭","步道乐跑","球类活动"};
        String[] yi_2= {"打代码","上课睡觉","做数学题","剪头发","咕咕咕","落地成盒","认真听课","熬夜学习","观察大自然","去图书馆"};

        yi = yi_1[(int)(t/10)]+"\n\n\n"+yi_2[(int)(t%10)];
        return yi;
    }

    public String getJi(Context context){
        String ji;
        int t;

        String today_str = getTodayStr();
        SharedPreferences pref = context.getSharedPreferences("ji_data",Context.MODE_PRIVATE);
        String temp_str = pref.getString("ji_data","");
        SharedPreferences.Editor editor =  context.getSharedPreferences("ji_data",Context.MODE_PRIVATE).edit();

        if(temp_str.equals(today_str)){
            t=pref.getInt("No.",0);
            //不同一天//第一次
        }else{
            SharedPreferences pref_yi = context.getSharedPreferences("yi_data",Context.MODE_PRIVATE);
            int No_yi = pref_yi.getInt("No.",0);
            int prev_t=pref.getInt("No.",0);
            editor.putString("date",today_str);
            t = new Random().nextInt(100);
            while((int)(prev_t/10)==(int)(t/10)||(int)(prev_t%10)==(int)(t%10)||(int)(No_yi/10)==(int)(t/10)||(int)(No_yi%10)==(int)(t%10)){
               t = new Random().nextInt(100);
            }
            editor.putString("date",today_str);
            editor.putInt("No.",t);
            editor.apply();
        }

        String[] ji_1={"吃二饭一楼","吃麻辣香锅","吃二饭三楼","吃烧卤","吃一饭二楼","吃清真食堂","吃外卖","吃扬州炒饭","步道乐跑","球类活动"};
        String[] ji_2= {"打代码","上课睡觉","做数学题","剪头发","咕咕咕","落地成盒","认真听课","熬夜学习","观察大自然","去图书馆"};

        ji = ji_1[(int)(t/10)]+"\n\n\n"+ji_2[(int)(t%10)];

        return ji;
    }
    public String newSlogan(Context context){
        int t;

        String today_str = getTodayStr();
        SharedPreferences pref = context.getSharedPreferences("slogan_data",Context.MODE_PRIVATE);
        String temp_str = pref.getString("slogan_data","");
        SharedPreferences.Editor editor =  context.getSharedPreferences("slogan_data",Context.MODE_PRIVATE).edit();

        if(temp_str.equals(today_str)){
            t=pref.getInt("No.",0);

        }else{//不同一天//第一次
            int prev_t=pref.getInt("No.",0);
            editor.putString("date",today_str);
            t = new Random().nextInt(7);
            while(prev_t==t){
                t = new Random().nextInt(7);
            }
            editor.putString("date",today_str);
            editor.putInt("No.",t);
            editor.apply();
        }
        switch (t){
            case 0:
                return "今天你解决不了的问题 那就别解决了 反正到了明天你也解决不了";
            case 1:
                return "上帝在给你关上一扇门的时候 必定也会顺带关一扇窗子 关上一扇窗子的同时 也会顺带夹到你的脑子";
            case 2:
                return "其实这个世界上并没有所谓贵的东西 只有那些我买的起和买不起的东西";
            case 3:
                return "都说爱笑的女生运气不会太差 如果一个女生运气一直不好 我不知道她谁给他的勇气 让她笑得出来";
            case 4:
                return "谁说你没有毅力的？单身这件事你不是坚持了好几年吗？";
            case 5:
                return "努力不一定会成功 但是不努力真的好舒服啊";
            case 6:
                return "回首青春 你会发现自己失去了很多宝贵的东西 但请你不要难过 因为你以后会失去的更多";
        }
        return "";
    }
}
