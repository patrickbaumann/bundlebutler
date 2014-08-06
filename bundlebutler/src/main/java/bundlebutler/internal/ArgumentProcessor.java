package bundlebutler.internal;

import bundlebutler.Argument;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by patrick on 8/3/14.
 */
public class ArgumentProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> strings = new HashSet<String>();
        strings.add(Argument.class.getCanonicalName());
        return strings;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<Element, List<Element>> wrappedFragments = new HashMap<Element, List<Element>>();

        for (Element e : roundEnv.getElementsAnnotatedWith(Argument.class)) {
            Element enclosingElement = e.getEnclosingElement();
            if (!wrappedFragments.containsKey(enclosingElement)) {
                if (enclosingElement.getKind() != ElementKind.CLASS) {
                    error(className(enclosingElement) + " is not a class!", enclosingElement);
                }
                wrappedFragments.put(enclosingElement, new LinkedList<Element>());
            }
            wrappedFragments.get(enclosingElement).add(e);
        }

        for (Map.Entry<Element, List<Element>> entry : wrappedFragments.entrySet()) {
            String fqWrapperClassName =
                    new StringBuilder(className(entry.getKey())).append("$$").append("ArgumentWrapper").toString();
            try {
                List<FieldAdapter> adapters = new LinkedList<FieldAdapter>();
                for (Element field : entry.getValue()) {
                    TypeElement t;
                    adapters.add(new SimpleFieldAdapter(field));
                    if (field.getModifiers().contains(Modifier.PRIVATE)
                            || field.getModifiers().contains(Modifier.FINAL)) {
                        throw new IllegalStateException("Fields cannot be private when annotated with @Argument");
                    }
                }
                JavaFileObject jfo =
                        processingEnv.getFiler().createSourceFile(fqWrapperClassName, (TypeElement) entry.getKey());

                String wrapperClassName = getClassName(fqWrapperClassName);
                String className = entry.getKey().getSimpleName().toString();
                String fragmentName = entry.getKey().getSimpleName().toString();

                Writer writer = jfo.openWriter();

                writer.append("/** Generated code, do no modify. */\n");
                writer.append("package ").append(packageName(entry.getKey())).append(";\n");
                writer.append("import ").append(ArgumentWrapper.class.getPackage().getName()).append(".*;\n");
                writer.append("import android.os.Bundle;\n");

                writer.append("public class ").append(wrapperClassName).append(" implements ")
                        .append(ArgumentWrapper.class.getSimpleName()).append(" {\n");
                writer.append("    public void saveArgs(Object target, Bundle args) {\n");
                writer.append("        ").append(className).append(" fragment = (")
                        .append(fragmentName).append(")target;\n");
                for (FieldAdapter adapter : adapters) {
                    adapter.writePutter(writer, "fragment", "args");
                }
                writer.append("    }\n");
                writer.append("    public void loadArgs(Object target, Bundle args) {\n");
                writer.append("        ").append(className).append(" fragment = (")
                        .append(fragmentName).append(")target;\n");
                for (FieldAdapter adapter : adapters) {
                    adapter.writeGetter(writer, "fragment", "args");
                }
                writer.append("    }\n");
                writer.append("}\n");

                writer.flush();
                writer.close();
            } catch (IOException exp) {
                error("Couldn't create new class " + fqWrapperClassName, ((TypeElement) entry).getEnclosingElement());
                exp.printStackTrace();
            }

        }
        return true;
    }

    private String getClassName(String fullyQualifiedName) {
        List<String> parts = Arrays.asList(fullyQualifiedName.split("\\."));
        return parts.get(parts.size() - 1);
    }


    private void error(String message, Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, e);
    }

    private String className(Element e) {
        return e.asType().toString();
    }

    private String packageName(Element e) {
        return processingEnv.getElementUtils().getPackageOf((TypeElement) e).getQualifiedName().toString();
    }

}