package com.yq.allure2_androidj.common;

import android.util.Log;

import com.yq.allure2_androidj.android.listener.LifecycleNotifier;
import com.yq.allure2_androidj.common.lowJdk.Consumer;
import com.yq.allure2_androidj.common.lowJdk.Optional;
import com.yq.allure2_androidj.common.resultRW.AllureResultsWriter;
import com.yq.allure2_androidj.common.resultRW.FileAndroidResultsWriter;
import com.yq.allure2_androidj.common.utils.Objects;
import com.yq.allure2_androidj.model.Attachment;
import com.yq.allure2_androidj.model.FixtureResult;
import com.yq.allure2_androidj.model.Stage;
import com.yq.allure2_androidj.model.StepResult;
import com.yq.allure2_androidj.model.TestResult;
import com.yq.allure2_androidj.model.TestResultContainer;
import com.yq.allure2_androidj.model.WithAttachments;
import com.yq.allure2_androidj.model.listeners.ContainerLifecycleListener;
import com.yq.allure2_androidj.model.listeners.FixtureLifecycleListener;
import com.yq.allure2_androidj.model.listeners.StepLifecycleListener;
import com.yq.allure2_androidj.model.listeners.TestLifecycleListener;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.yq.allure2_androidj.common.resultRW.AllureConstants.ATTACHMENT_FILE_SUFFIX;
import static com.yq.allure2_androidj.common.resultRW.AllureConstants.TAG;
import static com.yq.allure2_androidj.common.utils.ServiceLoaderUtils.load;
import static com.yq.allure2_androidj.common.utils.Tools.isNone;
import static java.sql.DriverManager.println;


/**
 * The class contains Allure context and methods to change it.
 */
@SuppressWarnings("PMD.TooManyMethods")
public class AllureLifecycle {

    private final AllureResultsWriter writer;

    private final AllureStorage storage;

    private final LifecycleNotifier notifier;

    public AllureLifecycle() {
        this(new FileAndroidResultsWriter());
    }

    public AllureLifecycle(final AllureResultsWriter writer) {
        final ClassLoader classLoader = getClass().getClassLoader();
        this.notifier = new LifecycleNotifier(
                load(ContainerLifecycleListener.class, classLoader),
                load(TestLifecycleListener.class, classLoader),
                load(FixtureLifecycleListener.class, classLoader),
                load(StepLifecycleListener.class, classLoader)
        );
        this.writer = writer;
        this.storage = new AllureStorage();
    }

    public void startTestContainer(final String parentUuid, final TestResultContainer container) {
        updateTestContainer(parentUuid, found -> found.getChildren().add(container.getUuid()));
        startTestContainer(container);
    }

    public void startTestContainer(final TestResultContainer container) {
        notifier.beforeContainerStart(container);
        container.setStart(System.currentTimeMillis());
        storage.addContainer(container);
        notifier.afterContainerStart(container);
    }

    public void updateTestContainer(final String uuid, final Consumer<TestResultContainer> update) {
        storage.getContainer(uuid).ifPresent(container -> {
            notifier.beforeContainerUpdate(container);
            update.accept(container);
            notifier.afterContainerUpdate(container);
        });
    }

    public void stopTestContainer(final String uuid) {
        storage.getContainer(uuid).ifPresent(container -> {
            notifier.beforeContainerStop(container);
            container.setStop(System.currentTimeMillis());
            notifier.afterContainerUpdate(container);
        });
    }

    public void writeTestContainer(final String uuid) {
        storage.removeContainer(uuid).ifPresent(container -> {
            notifier.beforeContainerWrite(container);
            writer.write(container);
            notifier.afterContainerWrite(container);
        });
    }

    public void startPrepareFixture(final String parentUuid, final String uuid, final FixtureResult result) {
        notifier.beforeFixtureStart(result);
        updateTestContainer(parentUuid, container -> container.getBefores().add(result));
        startFixture(uuid, result);
        notifier.afterFixtureStart(result);
    }

    public void startTearDownFixture(final String parentUuid, final String uuid, final FixtureResult result) {
        notifier.beforeFixtureStart(result);
        updateTestContainer(parentUuid, container -> container.getAfters().add(result));
        startFixture(uuid, result);
        notifier.afterFixtureStart(result);
    }

    private void startFixture(final String uuid, final FixtureResult result) {
        storage.addFixture(uuid, result);
        result.setStage(Stage.RUNNING);
        result.setStart(System.currentTimeMillis());
        storage.clearStepContext();
        storage.startStep(uuid);
    }

    public void updateFixture(final Consumer<FixtureResult> update) {
        updateFixture(storage.getRootStep(), update);
    }

    public void updateFixture(final String uuid, final Consumer<FixtureResult> update) {
        storage.getFixture(uuid).ifPresent(fixture -> {
            notifier.beforeFixtureUpdate(fixture);
            update.accept(fixture);
            notifier.afterFixtureUpdate(fixture);
        });
    }

    public void stopFixture(final String uuid) {
        storage.removeFixture(uuid).ifPresent(fixture -> {
            notifier.beforeFixtureStop(fixture);
            storage.clearStepContext();
            fixture.setStage(Stage.FINISHED);
            fixture.setStop(System.currentTimeMillis());
            notifier.afterFixtureStop(fixture);
        });
    }

    public Optional<String> getCurrentTestCase() {
        return Optional.ofNullable(storage.getRootStep());
    }

    public void scheduleTestCase(final String parentUuid, final TestResult result) {
        updateTestContainer(parentUuid, container -> container.getChildren().add(result.getUuid()));
        scheduleTestCase(result);
    }

    public void scheduleTestCase(final TestResult result) {
        notifier.beforeTestSchedule(result);
        result.setStage(Stage.SCHEDULED);
        storage.addTestResult(result);
        notifier.afterTestSchedule(result);
    }

    public void startTestCase(final String uuid) {
        storage.getTestResult(uuid).ifPresent(testResult -> {
            notifier.beforeTestStart(testResult);
            testResult
                    .withStage(Stage.RUNNING)
                    .withStart(System.currentTimeMillis());
            storage.clearStepContext();
            storage.startStep(uuid);
            notifier.afterTestStart(testResult);
        });
    }

    public void updateTestCase(final Consumer<TestResult> update) {
        final String uuid = storage.getRootStep();
        updateTestCase(uuid, update);
    }

    public void updateTestCase(final String uuid, final Consumer<TestResult> update) {
        storage.getTestResult(uuid).ifPresent(testResult -> {
            notifier.beforeTestUpdate(testResult);
            update.accept(testResult);
            notifier.afterTestUpdate(testResult);
        });
    }

    public void stopTestCase(final String uuid) {
        storage.getTestResult(uuid).ifPresent(testResult -> {
            notifier.beforeTestStop(testResult);
            testResult
                    .withStage(Stage.FINISHED)
                    .withStop(System.currentTimeMillis());
            storage.clearStepContext();
            notifier.afterTestStop(testResult);
        });
    }

    public void writeTestCase(final String uuid) {
        storage.removeTestResult(uuid).ifPresent(testResult -> {
            notifier.beforeTestWrite(testResult);
            writer.write(testResult);
            notifier.afterTestWrite(testResult);
        });
    }

    public void startStep(final String uuid, final StepResult result) {
        storage.getCurrentStep().ifPresent(parentUuid -> startStep(parentUuid, uuid, result));
    }

    public void startStep(final String parentUuid, final String uuid, final StepResult result) {
        notifier.beforeStepStart(result);
        result.setStage(Stage.RUNNING);
        result.setStart(System.currentTimeMillis());
        storage.startStep(uuid);
        storage.addStep(parentUuid, uuid, result);
        notifier.afterStepStart(result);
    }

    public void updateStep(final Consumer<StepResult> update) {
        storage.getCurrentStep().ifPresent(uuid -> updateStep(uuid, update));
    }

    public void updateStep(final String uuid, final Consumer<StepResult> update) {
        storage.getStep(uuid).ifPresent(step -> {
            notifier.beforeStepUpdate(step);
            update.accept(step);
            notifier.afterStepUpdate(step);
        });
    }

    public void stopStep() {
        storage.getCurrentStep().ifPresent(this::stopStep);
    }

    public void stopStep(final String uuid) {
        storage.removeStep(uuid).ifPresent(step -> {
            notifier.beforeStepStop(step);
            step.setStage(Stage.FINISHED);
            step.setStop(System.currentTimeMillis());
            storage.stopStep();
            notifier.afterStepStop(step);
        });
    }

    public void addAttachment(final String name, final String type,
                              final String fileExtension, final byte[] body) {
        addAttachment(name, type, fileExtension, new ByteArrayInputStream(body));
    }

    public void addAttachment(final String name, final String type,
                              final String fileExtension, final InputStream stream) {
        writeAttachment(prepareAttachment(name, type, fileExtension ,null), stream);
    }

    /**
     * 主要针对于自动截图功能( 会自动保存的 , 不用write)
     */
    public void addAttachment(String name ,String source){
        prepareAttachment(name ,"image/png" , ".png" ,source);
    }

    /**
     * 主要针对于删除文件后的处理操作 ( 删除关联的 Attachment )
     */
    public void removeAttachment(){
        Optional<String> currentStep = storage.getCurrentStep();
        currentStep.ifPresent(uuid ->
            storage.get(uuid ,WithAttachments.class).ifPresent(attachments ->
                    {
                        List<Attachment> att = attachments.getAttachments();
                        if (att.isEmpty()) {
                            Log.d(TAG,"empty Attachments ,can not remove it .");
                        }
                        att.remove(att.size() - 1);
                    }
            )
        );
    }

    @SuppressWarnings({"PMD.NullAssignment", "PMD.UseObjectForClearerAPI"})
    public String prepareAttachment(final String name, final String type, final String fileExtension , String source) {
        final Optional<String> currentStep = storage.getCurrentStep();
        currentStep.ifPresent(uuid -> Log.d(TAG,"Adding attachment to item with uuid "+uuid));
        final String extension = Optional.ofNullable(fileExtension)
                .filter(ext -> !ext.isEmpty())
                .map(ext -> ext.charAt(0) == '.' ? ext : "." + ext)
                .orElse("");
        if (isNone(source)) source = makeAttachmentSource(extension);
        final Attachment attachment = new Attachment()
                .withName(isEmpty(name) ? null : name)
                .withType(isEmpty(type) ? null : type)
                .withSource(source);

        currentStep.flatMap(uuid -> storage.get(uuid, WithAttachments.class))
                .ifPresent(withAttachments -> withAttachments.getAttachments().add(attachment));
        return attachment.getSource();
    }

    public void writeAttachment(final String attachmentSource, final InputStream stream) {
        writer.write(attachmentSource, stream);
    }

    public String makeAttachmentSource(String fileExtension){
        return UUID.randomUUID().toString() + ATTACHMENT_FILE_SUFFIX + fileExtension;
    }

    private boolean isEmpty(final String s) {
        return Objects.isNull(s) || s.isEmpty();
    }

}
