package com.scut.scutwizard.Helpers;

import java.util.Iterator;

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
}
