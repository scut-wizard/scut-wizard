package com.scut.scutwizard.Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import androidx.annotation.NonNull;

public class FileHelper {
    public static boolean copy(@NonNull File from, @NonNull File to) {
        if (to.exists()) {
            to.delete(); // delete file if exists
        }
        try {
            to.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel;
        FileChannel dstChannel;

        try {
            srcChannel = new FileInputStream(from).getChannel();
            dstChannel = new FileOutputStream(to).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;

//                long size = f.length();
//                if (size > Integer.MAX_VALUE) {
//                    Toast.makeText(mContext, "文件过大!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                FileInputStream fs = new FileInputStream(f);
//                byte[] buffer = new byte[(int) size];
//                int offset = 0;
//                int numRead;
//                while (offset < buffer.length
//                       && (numRead = fs.read(buffer, offset, buffer.length - offset)) >= 0)
//                    offset += numRead;
//                if (offset != buffer.length)
//                    throw new IOException();
//                fs.close();
    }
}
