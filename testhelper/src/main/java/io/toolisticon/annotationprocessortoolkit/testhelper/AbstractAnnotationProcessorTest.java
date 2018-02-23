package io.toolisticon.annotationprocessortoolkit.testhelper;

import com.google.testing.compile.CompileTester;
import io.toolisticon.annotationprocessortoolkit.testhelper.validator.TestMessageValidator;
import io.toolisticon.annotationprocessortoolkit.testhelper.validator.TestValidator;
import io.toolisticon.annotationprocessortoolkit.testhelper.validator.TestValidatorType;
import org.hamcrest.MatcherAssert;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;


/**
 * Abstract base class which allows parameterized tests.
 */

public abstract class AbstractAnnotationProcessorTest<T extends AnnotationProcessorCommonTestConfiguration> {

    public static final String TEST_EXECUTION_MESSAGE = "!!!--- TEST WAS EXECUTED ---!!!";

    private T annotationProcessorCommonTestConfiguration;

    public AbstractAnnotationProcessorTest(T annotationProcessorCommonTestConfiguration) {
        this.annotationProcessorCommonTestConfiguration = annotationProcessorCommonTestConfiguration;
    }


    /**
     * Gets the annotation processor to used for testing.
     *
     * @return the annotation processor to use for testing
     */
    protected abstract Processor getAnnotationProcessor();

    /**
     * Gets the {@link JavaFileObject} to be compiled.
     *
     * @return the code to be compiled
     */
    protected abstract JavaFileObject getSourceFileForCompilation();


    private Processor getWrappedProcessor() {
        return AnnotationProcessorWrapper.wrapProcessor(getAnnotationProcessor());
    }


    protected void test() {

        JavaFileObject testClassSource = getSourceFileForCompilation();

        TestMessageValidator messageValidationTest = (TestMessageValidator) getTestOfType(
                annotationProcessorCommonTestConfiguration.getTestcases(),
                TestValidatorType.MESSAGE_VALIDATOR);

        // check if error messages and shouldSucceed aren't set contradictionary
        if (annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed() != null
                && annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed().booleanValue()
                && messageValidationTest != null
                && messageValidationTest.getErrors().length > 0) {
            throw new InvalidTestConfigurationException("Test configuration error : Compilation should succeed but also error messages !");
        }


        if (
                (
                        annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed() == null
                                || annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed().booleanValue()
                ) && (
                        messageValidationTest == null
                                || messageValidationTest.getErrors().length == 0
                )
                ) {
            CompileTester.SuccessfulCompilationClause compileTester = assertAbout(javaSource())
                    .that(testClassSource)
                    .processedWith(this.getWrappedProcessor()).compilesWithoutError();

            // check for created files
            if (annotationProcessorCommonTestConfiguration.getExpectedGeneratedJavaFileObjects() != null) {
                for (JavaFileObject fileObject : annotationProcessorCommonTestConfiguration.getExpectedGeneratedJavaFileObjects()) {
                    compileTester = compileTester.and().generatesFiles(fileObject);
                }
            }


            // check for info messages (notes)
            if (messageValidationTest != null && messageValidationTest.getInfos() != null) {
                for (String infos : messageValidationTest.getInfos()) {
                    compileTester.withNoteContaining(infos);
                }
            }

            // check for warnings
            if (messageValidationTest != null) {
                for (String warning : messageValidationTest.getWarnings()) {
                    compileTester.withWarningContaining(warning);
                }
            }


            // check if test was executed at all
            compileTester.withNoteContaining(TEST_EXECUTION_MESSAGE);

        } else {
            CompileTester.UnsuccessfulCompilationClause compileTester = assertAbout(javaSource())
                    .that(testClassSource)
                    .processedWith(this.getWrappedProcessor()).failsToCompile();

            if (annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed() != null) {
                MatcherAssert.assertThat("Compiling should have failed", !annotationProcessorCommonTestConfiguration.getCompilingShouldSucceed());
            }

            // check for info messages (notes)
            if (messageValidationTest != null && messageValidationTest.getInfos() != null) {
                for (String infos : messageValidationTest.getInfos()) {
                    compileTester.withNoteContaining(infos);
                }
            }

            // check for warnings
            if (messageValidationTest != null) {
                for (String warning : messageValidationTest.getWarnings()) {
                    compileTester.withWarningContaining(warning);
                }
            }

            // check for errors
            if (messageValidationTest != null && messageValidationTest.getErrors() != null) {
                for (String error : messageValidationTest.getErrors()) {
                    compileTester.withErrorContaining(error);
                }
            }

            // check if test was executed at all
            compileTester.withNoteContaining(TEST_EXECUTION_MESSAGE);
        }


    }

    protected T getAnnotationProcessorTestConfiguration() {
        return annotationProcessorCommonTestConfiguration;
    }

    private TestValidator getTestOfType(TestValidator[] testValidators, TestValidatorType type) {

        if (testValidators == null || type == null) {
            return null;
        }

        for (TestValidator testValidator : testValidators) {

            if (type.equals(testValidator.getAnnotationProcessorTestType())) {
                return testValidator;
            }

        }

        return null;

    }


}