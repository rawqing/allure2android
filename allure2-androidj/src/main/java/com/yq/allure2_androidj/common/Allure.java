package com.yq.allure2_androidj.common;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;

import com.yq.allure2_androidj.common.feature.Screenshot;
import com.yq.allure2_androidj.common.utils.Objects;
import com.yq.allure2_androidj.common.utils.Tools;
import com.yq.allure2_androidj.model.Label;
import com.yq.allure2_androidj.model.Link;
import com.yq.allure2_androidj.model.annotations.Tag;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static com.yq.allure2_androidj.common.resultRW.AllureConstants.TAG;
import static com.yq.allure2_androidj.common.utils.Tools.notNone;

/**
 * The class contains some useful methods to work with {@link AllureLifecycle}.
 */
public final class Allure {
    private Allure() {
        throw new IllegalStateException("Do not instance");
    }

    private static final String TXT_EXTENSION = ".txt";
    private static final String TEXT_PLAIN = "text/plain";
    private static AllureLifecycle lifecycle;
    private static File resultFile = null;

    public static String resultsName = "allure-results" + File.separator;
//    public static final int CACHE = 0;
//    public static final int SDCARD = 1;
//
//    public static int site = 0;


    public static File getResultFile() {
        if (resultFile == null) {
            resultFile = getSdcardDir();
            if (resultFile == null) {
                resultFile = getCacheDir();
            }
        }
        return resultFile;
    }

    public static void setResultFile(File resultFile) {
        Allure.resultFile = resultFile;
        makeDir(resultFile);
    }

    /**
     * 在app缓存目录创建results目录
     * @return
     */
    private static File getCacheDir(){
        String path = getInstrumentation().getTargetContext()
                .getFilesDir().getAbsolutePath() + File.separator + resultsName;
        File file = new File(path);
        if (makeDir(file)) {
            return file;
        }
        return file;
    }

    /**
     * 在sdcard 下创建目录
     * @return
     */
    private static File getSdcardDir(){
//        try {
//            Tools.grantPermissions();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            file =  new File(Environment.getExternalStorageDirectory(), resultsName);
        } else {
            file = getInstrumentation().getContext().getDir(resultsName, Context.MODE_PRIVATE);
        }
        if (makeDir(file)) {
            return file;
        }
        return file;

    }

    /**
     * 创建文件 , 创建前先删除
     * @param file
     * @return
     */
    private static Boolean makeDir(File file) {
        if (notNone(file)) {
            if (file.exists()) {
                Tools.deleteFolderFile(file,true);
            }
            if (file.isDirectory()) {
                Log.i(TAG, "mkdir : '"+file.getAbsolutePath() + "' is directory !");
                return true;
            }
            Boolean mk = file.mkdirs();
            Log.i(TAG, "mkdir : '"+file.getAbsolutePath() + "'"+mk);
            return mk;
        }
        return false;
    }

    public static AllureLifecycle getLifecycle() {
        if (Objects.isNull(lifecycle)) {
            lifecycle = new AllureLifecycle();
        }
        return lifecycle;
    }

    public static void addLabels(final Label... labels) {
        getLifecycle().updateTestCase(testResult -> testResult.withLabels(labels));
    }

    public static void addLinks(final Link... links) {
        getLifecycle().updateTestCase(testResult -> testResult.withLinks(links));
    }

    public static void addDescription(final String description) {
        getLifecycle().updateTestCase(executable -> executable.withDescription(description));
    }

    public static void addDescriptionHtml(final String descriptionHtml) {
        getLifecycle().updateTestCase(executable -> executable.withDescriptionHtml(descriptionHtml));
    }

    public static void addAttachment(final String name, final String content) {
        getLifecycle().addAttachment(name, TEXT_PLAIN, TXT_EXTENSION, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void addAttachment(final String name, final String type, final String content) {
        getLifecycle().addAttachment(name, type, TXT_EXTENSION, content.getBytes(StandardCharsets.UTF_8));
    }

    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    public static void addAttachment(final String name, final String type,
                                     final String content, final String fileExtension) {
        getLifecycle().addAttachment(name, type, fileExtension, content.getBytes(StandardCharsets.UTF_8));
    }

    public static void addAttachment(final String name, final InputStream content) {
        getLifecycle().addAttachment(name, null, null, content);
    }

    @SuppressWarnings("PMD.UseObjectForClearerAPI")
    public static void addAttachment(final String name, final String type,
                                     final InputStream content, final String fileExtension) {
        getLifecycle().addAttachment(name, type, fileExtension, content);
    }

    /**
     * 作为截图专用的附件添加
     * @param name 自定义的名称(若同一testcase中将出现多张截图 ,尽可能区分名称)
     * @param screenshot 截图并保存的过程 , 若成功 -> true , 反之亦然
     * @return 生产文件的绝对路径
     */
    public static String addAttachment(String name, Screenshot screenshot) {
        String source = lifecycle.makeAttachmentSource(".png");

        String filePath = (getResultFile().getAbsolutePath()) + File.separator + source;
        Boolean isTakeed = screenshot.takeScreenshot(new File(filePath));
        if (isTakeed){
            lifecycle.addAttachment(name, source);
        }
        return filePath;
    }

    public static String addAttachment(String name){
        return addAttachment(name, file ->
                UiDevice.getInstance(getInstrumentation()).takeScreenshot(file ,0.8f,80));
    }

    public static void removeAttachment(String filePath){
        if (!filePath.isEmpty()){
            Boolean deled = new File(filePath).delete();
            Log.i(TAG, "file deleted "+deled);
        }
        lifecycle.removeAttachment();
    }

//    public static CompletableFuture<byte[]> addByteAttachmentAsync(
//            final String name, final String type, final Supplier<byte[]> body) {
//        return addByteAttachmentAsync(name, type, "", body);
//    }

//    public static CompletableFuture<byte[]> addByteAttachmentAsync(
//            final String name, final String type, final String fileExtension, final Supplier<byte[]> body) {
//        final String source = getLifecycle().prepareAttachment(name, type, fileExtension);
//        return supplyAsync(body).whenComplete((result, ex) ->
//                getLifecycle().writeAttachment(source, new ByteArrayInputStream(result)));
//    }

//    public static CompletableFuture<InputStream> addStreamAttachmentAsync(
//            final String name, final String type, final Supplier<InputStream> body) {
//        return addStreamAttachmentAsync(name, type, "", body);
//    }
//
//    public static CompletableFuture<InputStream> addStreamAttachmentAsync(
//            final String name, final String type, final String fileExtension, final Supplier<InputStream> body) {
//        final String source = lifecycle.prepareAttachment(name, type, fileExtension);
//        return supplyAsync(body).whenComplete((result, ex) -> lifecycle.writeAttachment(source, result));
//    }

    public static void setLifecycle(final AllureLifecycle lifecycle) {
        Allure.lifecycle = lifecycle;
    }
}
