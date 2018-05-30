package com.yq.allure2_androidj.common.resultRW;

import com.yq.allure2_androidj.common.resultRW.gson.GsonSerializationProcessor;
import com.yq.allure2_androidj.common.resultRW.gson.SerializationProcessor;
import com.yq.allure2_androidj.model.TestResult;
import com.yq.allure2_androidj.model.TestResultContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.yq.allure2_androidj.common.Allure.getResultFile;
import static com.yq.allure2_androidj.common.utils.AllureUtils.generateTestResultContainerName;
import static com.yq.allure2_androidj.common.utils.AllureUtils.generateTestResultName;

public class FileAndroidResultsWriter implements AllureResultsWriter {
    private final SerializationProcessor serializationProcessor = new GsonSerializationProcessor();

    @Override
    public void write(TestResult testResult) {
        this.serializationProcessor.serialize(
                new File(getResultFile(), generateTestResultName(testResult.getUuid())), testResult);
    }

    @Override
    public void write(TestResultContainer testResultContainer) {
        this.serializationProcessor.serialize(
                new File(getResultFile(), generateTestResultContainerName(testResultContainer.getUuid())),
                testResultContainer);
    }

    @Override
    public void write(String source, InputStream attachment) {
        write(new File(getResultFile(), source), attachment);
    }

    private void write(File dest, InputStream attachment) {
        try(FileOutputStream out = new FileOutputStream(dest)) {
            int bytesWritten = 0;
            int byteCount = 0;
            byte[] bytes = new byte[1024];
            while ((byteCount = attachment.read(bytes)) != -1)
            {
                out.write(bytes, bytesWritten, byteCount);
                bytesWritten += byteCount;
            }
            attachment.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
