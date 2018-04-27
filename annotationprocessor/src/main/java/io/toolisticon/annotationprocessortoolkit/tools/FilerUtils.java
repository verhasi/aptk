package io.toolisticon.annotationprocessortoolkit.tools;


import io.toolisticon.annotationprocessortoolkit.tools.generators.SimpleJavaWriter;
import io.toolisticon.annotationprocessortoolkit.tools.generators.SimpleResourceReader;
import io.toolisticon.annotationprocessortoolkit.tools.generators.SimpleResourceWriter;

import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import java.io.IOException;

/**
 * Utility class for handling {@link javax.tools.FileObject}.
 */
public final class FilerUtils {


    /**
     * Hidden constructor that prevents instantiation.
     */
    private FilerUtils() {
    }

    /**
     * Gets a SimpleResourceReader for reading a resource file.
     *
     * @param fileName      the file name of the resource to be opened
     * @param targetPackage the target package
     * @return SimpleResourceReader if resource exists and can be read (== not opened for writing)
     */
    public static SimpleResourceReader getResource(String fileName, String targetPackage) throws IOException {
        return getResource(fileName, targetPackage, StandardLocation.SOURCE_OUTPUT);
    }

    /**
     * Gets a SimpleResourceReader for reading a resource file.
     *
     * @param fileName         the file name of the resource to be opened
     * @param targetPackage    the target package
     * @param standardLocation the location to read the resource from
     * @return SimpleResourceReader if resource exists and can be read (== not opened for writing)
     */
    public static SimpleResourceReader getResource(String fileName, String targetPackage, StandardLocation standardLocation) throws IOException {
        return new SimpleResourceReader(ProcessingEnvironmentUtils.getFiler().getResource(standardLocation, targetPackage != null ? targetPackage : "", fileName));
    }

    /**
     * Gets a SimpleResourceReader for reading a resource file.
     *
     * @param fileName the file name of the resource to be opened
     * @return SimpleResourceReader if resource exists and can be read (== not opened for writing)
     */
    public static SimpleResourceReader getResource(String fileName) throws IOException {
        return getResource(fileName, "");
    }

    /**
     * Creates a resource file resident in CLASS_OUTPUT in root package with give name.
     *
     * @param fileName            the file name to use
     * @param targetPackage       the target package to use
     * @param originatingElements the originating elements responsible for resource file creation.
     * @return SimpleResourceWriter if it hasn't been created before.
     * @throws IOException
     * @throws javax.annotation.processing.FilerException if the same pathname has already been created
     */
    public static SimpleResourceWriter createResource(String fileName, String targetPackage, Element... originatingElements) throws IOException {
        return createResource(fileName, targetPackage, StandardLocation.CLASS_OUTPUT);
    }

    /**
     * Creates a resource file resident in CLASS_OUTPUT in root package with give name.
     *
     * @param fileName            the file name to use
     * @param targetPackage       the target package to use
     * @param location            the location to write the file to.
     * @param originatingElements the originating elements responsible for resource file creation.
     * @return SimpleResourceWriter if it hasn't been created before.
     * @throws IOException
     * @throws javax.annotation.processing.FilerException if the same pathname has already been created
     */
    public static SimpleResourceWriter createResource(String fileName, String targetPackage, StandardLocation location, Element... originatingElements) throws IOException {
        return new SimpleResourceWriter(ProcessingEnvironmentUtils.getFiler().createResource(location, targetPackage != null ? targetPackage : "", fileName, originatingElements));
    }

    /**
     * Creates a resource file resident in SOURCE_OUTPUT in root package with give name.
     *
     * @param fileName the file name to use
     * @return SimpleResourceWriter if it hasn't been created before.
     * @throws IOException
     * @throws javax.annotation.processing.FilerException if the same pathname has already been created
     */
    public static SimpleResourceWriter createResource(String fileName) throws IOException {
        return createResource(fileName, "");
    }


    /**
     * Creates a class file writer.
     *
     * @param fileName            the filename to use
     * @param originatingElements the element which originates the creation of the class file
     * @return a SimpleJavaWriter that can be used to write java classes
     * @throws IOException is thrown if writer can't be created
     */
    public static SimpleJavaWriter createClassFile(String fileName, Element... originatingElements) throws IOException {
        return new SimpleJavaWriter(ProcessingEnvironmentUtils.getFiler().createClassFile(fileName, originatingElements));
    }

    /**
     * Creates a source file writer.
     *
     * @param fileName            the filename to use
     * @param originatingElements the element which originates the creation of the source file
     * @return a SimpleJavaWriter that can be used to write java source code
     * @throws IOException is thrown if writer can't be created
     */
    public static SimpleJavaWriter createSourceFile(String fileName, Element... originatingElements) throws IOException {
        return new SimpleJavaWriter(ProcessingEnvironmentUtils.getFiler().createSourceFile(fileName, originatingElements));
    }


}