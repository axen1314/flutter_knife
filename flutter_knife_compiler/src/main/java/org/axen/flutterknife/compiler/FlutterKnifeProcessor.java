package org.axen.flutterknife.compiler;

import com.google.auto.service.AutoService;
import com.sun.xml.internal.txw2.IllegalAnnotationException;

import org.axen.flutterknife.annotation.ExecuteDartCode;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@AutoService(Processor.class)
public class FlutterKnifeProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ExecuteDartCode.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.FIELD) {
                throw new IllegalAnnotationException(String.format("Only support for field of annotation: %s", element));
            }
            VariableElement variableElement = (VariableElement) element;
            ExecuteDartCode dartCode = variableElement.getAnnotation(ExecuteDartCode.class);
            System.out.println(variableElement.getEnclosingElement().getSimpleName());
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        for (Class<? extends Annotation> clazz : getSupportAnnotations()) {
            annotationTypes.add(clazz.getCanonicalName());
        }
        return annotationTypes;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotations() {
        return new LinkedHashSet<Class<? extends Annotation>>() {{
            add(ExecuteDartCode.class);
        }};
    }
}
