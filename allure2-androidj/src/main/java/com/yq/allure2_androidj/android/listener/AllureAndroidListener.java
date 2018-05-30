package com.yq.allure2_androidj.android.listener;

import android.support.test.internal.runner.listener.InstrumentationRunListener;

import com.yq.allure2_androidj.common.Allure;
import com.yq.allure2_androidj.common.AllureLifecycle;
import com.yq.allure2_androidj.common.feature.Stream;
import com.yq.allure2_androidj.common.lowJdk.Optional;
import com.yq.allure2_androidj.common.utils.Objects;
import com.yq.allure2_androidj.model.Label;
import com.yq.allure2_androidj.model.Link;
import com.yq.allure2_androidj.model.Status;
import com.yq.allure2_androidj.model.StatusDetails;
import com.yq.allure2_androidj.model.TestResult;
import com.yq.allure2_androidj.model.TestResultContainer;
import com.yq.allure2_androidj.model.annotations.DisplayName;
import com.yq.allure2_androidj.model.annotations.Epic;
import com.yq.allure2_androidj.model.annotations.Feature;
import com.yq.allure2_androidj.model.annotations.Owner;
import com.yq.allure2_androidj.model.annotations.Repeatable;
import com.yq.allure2_androidj.model.annotations.Severity;
import com.yq.allure2_androidj.model.annotations.Story;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.yq.allure2_androidj.common.utils.ResultsUtils.createLabel;
import static com.yq.allure2_androidj.common.utils.ResultsUtils.createLink;
import static com.yq.allure2_androidj.common.utils.ResultsUtils.getHostName;
import static com.yq.allure2_androidj.common.utils.ResultsUtils.getStatus;
import static com.yq.allure2_androidj.common.utils.ResultsUtils.getStatusDetails;
import static com.yq.allure2_androidj.common.utils.ResultsUtils.getThreadName;
import static com.yq.allure2_androidj.common.utils.Tools.grantPermissions;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AllureAndroidListener extends InstrumentationRunListener {
    private final AllureLifecycle lifecycle;
    private final String MD_5 = "md5";
    private final ThreadLocal<String> testCases = new InheritableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return UUID.randomUUID().toString();
        }
    };
    private final ThreadLocal<String> testContainer = new InheritableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return UUID.randomUUID().toString();
        }
    };

    public AllureAndroidListener() {
        this(Allure.getLifecycle());
    }

    public AllureAndroidListener(final AllureLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @Override
    public void testRunStarted(final Description description) throws Exception {
        grantPermissions();
        Allure.getResultFile();

        String uuid = testContainer.get();
        TestResultContainer tc = new TestResultContainer()
                .withUuid(uuid);
        lifecycle.startTestContainer(tc);
    }

    @Override
    public void testRunFinished(final Result result) throws Exception {
        String uuid = testContainer.get();
        testContainer.remove();

        lifecycle.stopTestContainer(uuid);
        lifecycle.writeTestContainer(uuid);
    }

    @Override
    public void testStarted(final Description description) throws Exception {
        final String uuid = testCases.get();
        final TestResult result = createTestResult(uuid, description);
        lifecycle.scheduleTestCase(testContainer.get(),result);
        lifecycle.startTestCase(uuid);
    }

    @Override
    public void testFinished(final Description description) throws Exception {
        final String uuid = testCases.get();
        testCases.remove();
        lifecycle.updateTestCase(uuid, testResult -> {
            if (Objects.isNull(testResult.getStatus())) {
                testResult.setStatus(Status.PASSED);
            }
        });

        lifecycle.stopTestCase(uuid);
        lifecycle.writeTestCase(uuid);
    }

    @Override
    public void testFailure(final Failure failure) throws Exception {
        final String uuid = testCases.get();
        lifecycle.updateTestCase(uuid, testResult -> testResult
                .withStatus(getStatus(failure.getException()).orElse(null))
                .withStatusDetails(getStatusDetails(failure.getException()).orElse(null))
        );
    }

    @Override
    public void testAssumptionFailure(final Failure failure) {
        final String uuid = testCases.get();
        lifecycle.updateTestCase(uuid, testResult ->
                testResult.withStatus(Status.SKIPPED)
                        .withStatusDetails(getStatusDetails(failure.getException()).orElse(null))
        );
    }

    @Override
    public void testIgnored(final Description description) throws Exception {
        final String uuid = testCases.get();
        testCases.remove();

        final TestResult result = createTestResult(uuid, description);
        result.setStatus(Status.SKIPPED);
        result.setStatusDetails(getIgnoredMessage(description));
        result.setStart(System.currentTimeMillis());

        lifecycle.scheduleTestCase(result);
        lifecycle.stopTestCase(uuid);
        lifecycle.writeTestCase(uuid);
    }

    private Optional<String> getDisplayName(final Description result) {
        return Optional.ofNullable(result.getAnnotation(DisplayName.class))
                .map(DisplayName::value);
    }

    private Optional<String> getDescription(final Description result) {
        return Optional.ofNullable(result.getAnnotation(com.yq.allure2_androidj.model.annotations.Description.class))
                .map(com.yq.allure2_androidj.model.annotations.Description::value);
    }

    private List<Link> getLinks(final Description result) {
        return Stream.of(
                createLink(result.getAnnotation(com.yq.allure2_androidj.model.annotations.Link.class)),
                createLink(result.getAnnotation(com.yq.allure2_androidj.model.annotations.Link.class)),
                createLink(result.getAnnotation(com.yq.allure2_androidj.model.annotations.Issue.class)),
                createLink(result.getTestClass().getAnnotation( com.yq.allure2_androidj.model.annotations.Issue.class)),
                createLink(result.getTestClass().getAnnotation(com.yq.allure2_androidj.model.annotations.TmsLink.class)),
                createLink(result.getTestClass().getAnnotation(com.yq.allure2_androidj.model.annotations.TmsLink.class))
        ).toList();
    }

    private List<Label> getLabels(final Description result) {
        return Stream.of(
                createLabel(result.getAnnotation(Epic.class)),
                createLabel(result.getAnnotation(Feature.class)),
                createLabel(result.getAnnotation(Story.class)),
                createLabel(result.getAnnotation(Severity.class)),
                createLabel(result.getAnnotation(Owner.class)),
                createLabel(result.getTestClass().getAnnotation(Epic.class)),
                createLabel(result.getTestClass().getAnnotation(Feature.class)),
                createLabel(result.getTestClass().getAnnotation(Story.class)),
                createLabel(result.getTestClass().getAnnotation(Severity.class)),
                createLabel(result.getTestClass().getAnnotation(Owner.class))
        ).toList();
    }

    @SuppressWarnings("unchecked")
    private <T extends Annotation> List<T> extractRepeatable(final Description result, final Class<T> clazz) {
        if (clazz != null && clazz.isAnnotationPresent(Repeatable.class)) {
            final Repeatable repeatable = clazz.getAnnotation(Repeatable.class);
            final Class<? extends Annotation> wrapper = repeatable.value();
            final Annotation annotation = result.getAnnotation(wrapper);
            if (Objects.nonNull(annotation)) {
                try {
                    final Method value = annotation.getClass().getMethod("value");
                    final Object annotations = value.invoke(annotation);
                    return Arrays.asList((T[]) annotations);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return Collections.emptyList();
    }

    private <T extends Annotation> T getAnnotationsOnClass(final Description result, final Class<T> clazz) {
        return result.getAnnotation(clazz);
    }

    private String getHistoryId(final Description description) {
        return md5(description.getClassName() + description.getMethodName());
    }

    private String md5(final String source) {
        final byte[] bytes = getMessageDigest().digest(source.getBytes(UTF_8));
        return new BigInteger(1, bytes).toString(16);
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(MD_5);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find md5 hashing algorithm", e);
        }
    }

    private String getPackage(final Class<?> testClass) {
        return Optional.ofNullable(testClass)
                .map(Class::getPackage)
                .map(Package::getName)
                .orElse("");
    }

    private StatusDetails getIgnoredMessage(final Description description) {
        final Ignore ignore = description.getAnnotation(Ignore.class);
        final String message = Objects.nonNull(ignore) && !ignore.value().isEmpty()
                ? ignore.value() : "Test ignored (without reason)!";
        return new StatusDetails().withMessage(message);
    }

    private TestResult createTestResult(final String uuid, final Description description) {
        final String className = description.getClassName();
        final String methodName = description.getMethodName();
        final String name = Objects.nonNull(methodName) ? methodName : className;
        final String fullName = Objects.nonNull(methodName) ? String.format("%s.%s", className, methodName) : className;
        final String suite = Optional.ofNullable(description.getTestClass())
                .map(it -> it.getAnnotation(DisplayName.class))
                .map(DisplayName::value).orElse(className);

        final TestResult testResult = new TestResult()
                .withUuid(uuid)
                .withHistoryId(getHistoryId(description))
                .withName(name)
                .withFullName(fullName)
                .withLinks(getLinks(description))
                .withLabels(
                        new Label().withName("package").withValue(getPackage(description.getTestClass())),
                        new Label().withName("testClass").withValue(className),
                        new Label().withName("testMethod").withValue(name),
                        new Label().withName("suite").withValue(suite),
                        new Label().withName("host").withValue(getHostName()),
                        new Label().withName("thread").withValue(getThreadName())
                );
        testResult.getLabels().addAll(getLabels(description));
        getDisplayName(description).ifPresent(testResult::setName);
        getDescription(description).ifPresent(testResult::setDescription);
        return testResult;
    }

}