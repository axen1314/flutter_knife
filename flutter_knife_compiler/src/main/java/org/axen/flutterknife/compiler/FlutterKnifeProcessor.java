package org.axen.flutterknife.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import org.axen.flutterknife.annotation.ExecuteDartCode;
import org.axen.flutterknife.compiler.exception.ProcessingException;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
public class FlutterKnifeProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
    }



    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ExecuteDartCode.class);
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.valueOf(elements.size()));
        for (Element element : elements) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, element.asType().toString());
            if (element.getKind() != ElementKind.FIELD) {
                throw new ProcessingException(String.format("Only support for field of annotation: %s", element));
            }
            VariableElement variableElement = (VariableElement) element;
            DeclaredType declaredType = (DeclaredType) element.asType();
            mMessager.printMessage(Diagnostic.Kind.NOTE, declaredType.getTypeArguments().toString());
            String name = variableElement.asType().getKind().name();
            FieldSpec.Builder field = FieldSpec.builder(String.class, "name")
                    .initializer("$S", name);
            TypeSpec.Builder result = TypeSpec.classBuilder("Binding")
                    .addModifiers(PUBLIC)
                    .addField(field.build());
            JavaFile javaFile = JavaFile.builder("org.axen.flutterknife.binding", result.build()).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ExecuteDartCode dartCode = variableElement.getAnnotation(ExecuteDartCode.class);
            mMessager.printMessage(Diagnostic.Kind.WARNING, "被注解的类有：" + variableElement.getEnclosingElement().getSimpleName());
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<String>() {{
            add(Deprecated.class.getCanonicalName());
            add(ExecuteDartCode.class.getCanonicalName());
        }};
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
