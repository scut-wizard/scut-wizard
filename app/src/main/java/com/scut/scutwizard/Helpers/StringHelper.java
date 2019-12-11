package com.scut.scutwizard.Helpers;

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

    public String newSlogan(){
        int t = new Random().nextInt(7);
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
