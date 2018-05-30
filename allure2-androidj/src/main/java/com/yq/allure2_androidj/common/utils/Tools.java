package com.yq.allure2_androidj.common.utils;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.yq.allure2_androidj.common.lowJdk.CopyOption;
import com.yq.allure2_androidj.common.lowJdk.Path;
import com.yq.allure2_androidj.common.lowJdk.StandardCopyOption;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class Tools {

    public static <T> boolean isNone( T  t ) {
        if (t == null)  return true;

        if (t instanceof Array) return Array.getLength(t) == 0;
        if(t instanceof String) return ((String) t).isEmpty();
        if(t instanceof Collection) return ((Collection) t).isEmpty();
        if(t instanceof Map)    return ((Map) t).isEmpty();

        return false;
    }

    public static <T> boolean notNone( T  t ) {
        return !isNone(t);
    }

    @SafeVarargs
    public static<T> List<T> of(T... values) {
        List<T> list = new ArrayList<>();
        for (T t : values) {
            if (!isNone(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static String printHexBinary(byte[] data, boolean lowerCase) {
        String hexCode = "0123456789ABCDEF";
        StringBuilder r = new StringBuilder(data.length * 2);

        for (byte b : data) {
            r.append(hexCode.charAt(b >> 4 & 15));
            r.append(hexCode.charAt(b & 15));
        }
        String var10000;
        if (lowerCase) {
            var10000 = r.toString().toLowerCase();
        } else {
            var10000 = r.toString();
        }
        return var10000;
    }

    /**
     * 递归删除目录及子文件
     */
    public static void deleteFolderFile(File file, Boolean deleteThisPath) {
        try {
            if (file.isDirectory()) { //目录
                File[] files = file.listFiles();
                for (File f: files) {
                    deleteFolderFile(f, true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) { //如果是文件，删除
                    boolean delete = file.delete();
                } else { //目录
                    if (isNone(file.listFiles())) { //目录下没有文件或者目录，删除
                        boolean delete = file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void grantPermissions() throws IOException {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        uiDevice.executeShellCommand("pm grant " +
                instrumentation.getContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        uiDevice.executeShellCommand("pm grant " +
                instrumentation.getTargetContext().getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE");
        uiDevice.executeShellCommand("pm grant " +
                instrumentation.getContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");
        uiDevice.executeShellCommand("pm grant " +
                instrumentation.getTargetContext().getPackageName() + " android.permission.READ_EXTERNAL_STORAGE");

    }

}
