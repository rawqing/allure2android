package com.yq.allure2_android.common

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice
import android.util.Log
import com.yq.allure2_android.android.listener.LifecycleNotifier
import com.yq.allure2_android.common.resultRW.AllureResultsWriter
import com.yq.allure2_android.model.listeners.ContainerLifecycleListener
import com.yq.allure2_android.model.listeners.FixtureLifecycleListener
import com.yq.allure2_android.model.listeners.StepLifecycleListener
import com.yq.allure2_android.model.listeners.TestLifecycleListener
import com.yq.allure2_android.model.Label
import com.yq.allure2_android.model.Link
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import kotlin.text.Charsets.UTF_8
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.os.Build
import android.os.Environment
import com.yq.allure2_android.common.utils.*
import java.io.IOException


public object Allure {
    private val TXT_EXTENSION = ".txt"
    private val TEXT_PLAIN = "text/plain"
    private val TAG = "${allureTag}Allure"

    var resDir: File? = null
//    var resDirPath: String? = null

    val lifecycle: AllureLifecycle = getAllureLifecycle(FileAndroidResultsWriter())


//    val CACHE = 0
//    val SDCARD = 1
//
//    var site = 0


    fun getResultFile(): File? {
        if (resDir == null) {
            resDir = getSdcardDir()
            if (resDir == null) {
                resDir = getCacheDir()
            }
        }
        return resDir
    }

    fun setResultFile(resultFile: File) {
        Allure.resDir = resultFile
        makeDir(resultFile)
    }

    /**
     * 在app缓存目录创建results目录
     * @return
     */
    private fun getCacheDir(): File? {
        val path = getInstrumentation().targetContext
                .filesDir.absolutePath + File.separator + resultsName
        val file = File(path)
        return if (makeDir(file)!!) {
            file
        } else null
    }

    /**
     * 在sdcard 下创建目录
     * @return
     */
    private fun getSdcardDir(): File? {
        try {
            grantPermissions()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val file: File
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            file = File(Environment.getExternalStorageDirectory(), resultsName)
        } else {
            file = getInstrumentation().context.getDir(resultsName, Context.MODE_PRIVATE)
        }
        return if (makeDir(file)!!) {
            file
        } else null

    }

    /**
     * 创建文件 , 创建前先删除
     * @param file
     * @return
     */
    private fun makeDir(file: File?): Boolean? {
        if (file != null) {
            if (file.exists()) {
                deleteFolderFile(file, true)
            }
            val mk = file.mkdirs()
            Log.i(TAG, "mkdir : $mk")
            return mk
        }
        return false
    }


    private fun getAllureLifecycle(writer: AllureResultsWriter): AllureLifecycle {
        val classLoader = javaClass.classLoader
        val notifier = LifecycleNotifier(
                ServiceLoaderUtils.load(ContainerLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(TestLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(FixtureLifecycleListener::class.java, classLoader),
                ServiceLoaderUtils.load(StepLifecycleListener::class.java, classLoader)
        )
        return AllureLifecycle(writer , AllureStorage(),notifier)
    }

    fun addLabels(vararg labels: Label) {
        lifecycle.updateTestCase({ this.withLabels(*labels) })
    }

    fun addLinks(vararg links: Link) {
        lifecycle.updateTestCase({ this.withLinks(*links) })
    }

    fun addDescription(description: String) {
        lifecycle.updateTestCase({ this.withDescription(description) })
    }

    fun addDescriptionHtml(descriptionHtml: String) {
        lifecycle.updateTestCase({ this.withDescriptionHtml(descriptionHtml) })
    }

    fun addAttachment(name: String, content: String) {
        lifecycle.addAttachment(name, TEXT_PLAIN, TXT_EXTENSION, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, type: String, content: String) {
        lifecycle.addAttachment(name, type, TXT_EXTENSION, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, type: String,
                      content: String, fileExtension: String) {
        lifecycle.addAttachment(name, type, fileExtension, content.toByteArray(UTF_8))
    }

    fun addAttachment(name: String, content: InputStream) {
        lifecycle.addAttachment(name, null, null, content)
    }

    fun addAttachment(name: String, type: String,
                      content: InputStream, fileExtension: String) {
        lifecycle.addAttachment(name, type, fileExtension, content)
    }

    /**
     * 作为截图专用的附件添加
     * @param name 自定义的名称(若同一testcase中将出现多张截图 ,尽可能区分名称)
     * @param takeScreenshot 截图并保存的过程 , 若成功 -> true , 反之亦然
     * @return 生产文件的绝对路径
     */
    fun addAttachment(name: String ,takeScreenshot: (file: File)-> Boolean) :String{
        val source = lifecycle.makeAttachmentSource(".png")

        val filePath = (resDir?.absolutePath?: getResultFile()?.absolutePath) + File.separator + source
        val isTakeed = takeScreenshot(File(filePath))
        if (isTakeed){
            lifecycle.addAttachment(name ,source)
        }
        return filePath
    }
    fun addAttachment(name: String) :String{
        return addAttachment(name ,{
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).takeScreenshot(it ,0.8f,80)
        })
    }
    fun removeAttachment(filePath:String){
        if (!filePath.isEmpty()){
            val deled = File(filePath).delete()
            Log.i(TAG,"file [$deled] deleted.")
        }
        lifecycle.removeAttachment()
    }

    fun prepareAttachment(name: String, type: String?, fileExtension: String?): String {
        return lifecycle.prepareAttachment(name,type ,fileExtension)
    }

    fun addByteAttachmentAsync(
            name: String, type: String, body: ByteArray) {
        addByteAttachmentAsync(name, type, "", body)
    }

    fun addByteAttachmentAsync(
            name: String, type: String, fileExtension: String, body: ByteArray) {
        val source = lifecycle.prepareAttachment(name, type, fileExtension)
        launch(CommonPool){
            lifecycle.writeAttachment(source, ByteArrayInputStream(body))
        }
    }

    fun addStreamAttachmentAsync(
            name: String, type: String, body: InputStream){
        return addStreamAttachmentAsync(name, type, "", body)
    }

    fun addStreamAttachmentAsync(
            name: String, type: String, fileExtension: String, body: InputStream){
        val source = lifecycle.prepareAttachment(name, type, fileExtension)
        launch(CommonPool){
            lifecycle.writeAttachment(source, body)
        }
    }

}