package de.holisticon.annotationprocessortoolkit.generators;

import javax.tools.JavaFileObject;
import java.io.IOException;


public class SimpleJavaWriter extends AbstractSimpleWriter<JavaFileObject> {


    public SimpleJavaWriter(JavaFileObject fileObject) throws IOException {
        super(fileObject);
    }


}
