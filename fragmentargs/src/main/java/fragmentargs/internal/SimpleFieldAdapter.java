package fragmentargs.internal;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;
import fragmentargs.Argument;

import javax.lang.model.element.Element;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedList;

class SimpleFieldAdapter implements FieldAdapter {

    private static final String STRING_ARRAY_LIST_CLASS = "java.util.ArrayList<java.lang.String>";
    private static final String CHAR_SEQUENCE_ARRAY_LIST_CLASS = "java.util.ArrayList<java.lang.CharSequence>";
    private static final String INTEGER_ARRAY_LIST_CLASS = "java.util.ArrayList<java.lang.Integer>";
    private static final String PARCELABLE_ARRAY_LIST_CLASS = "java.util.ArrayList<android.os.Parcelable>";
    public static final String PARCELABLE_CLASS = "android.os.Parcelable[]";
    public static final String BUNDLE_CLASS = "android.os.Bundle";

    /* Pending support for
       "ParcelableArrayList"       java.util.ArrayList<? extends android.os.Parcelable>
       "SparseParcelableArray"     android.util.SparseArray<? extends android.os.Parcelable>
     */

    private final String fieldName;
    private final String argumentKeyName;
    private final String methodSuffix;
    private final String fieldType;
    private boolean nullable = false;

    public SimpleFieldAdapter(Element field) {
        this.fieldName = field.getSimpleName().toString();
        this.argumentKeyName = field.getAnnotation(Argument.class).value();
        fieldType = field.asType().toString();
        LinkedList<String> interfaces = new LinkedList<String>();
        if (Type.ClassType.class.isAssignableFrom(field.asType().getClass())) {
            List<Type> type = ((Type.ClassType) field.asType()).interfaces_field;
            if (type != null)
                for (Type t : type) {
                    interfaces.add(t.toString());
                }
        }
        if (fieldType.equals(boolean.class.getCanonicalName())) {
            methodSuffix = "Boolean";
        } else if (fieldType.equals(byte.class.getCanonicalName())) {
            methodSuffix = "Byte";
        } else if (fieldType.equals(char.class.getCanonicalName())) {
            methodSuffix = "Char";
        } else if (fieldType.equals(int.class.getCanonicalName())) {
            methodSuffix = "Int";
        } else if (fieldType.equals(long.class.getCanonicalName())) {
            methodSuffix = "Long";
        } else if (fieldType.equals(float.class.getCanonicalName())) {
            methodSuffix = "Float";
        } else if (fieldType.equals(double.class.getCanonicalName())) {
            methodSuffix = "Double";
        } else if (fieldType.equals(Boolean.class.getCanonicalName())) {
            methodSuffix = "Boolean";
            nullable = true;
        } else if (fieldType.equals(Byte.class.getCanonicalName())) {
            methodSuffix = "Byte";
            nullable = true;
        } else if (fieldType.equals(Character.class.getCanonicalName())) {
            methodSuffix = "Char";
            nullable = true;
        } else if (fieldType.equals(Integer.class.getCanonicalName())) {
            methodSuffix = "Int";
            nullable = true;
        } else if (fieldType.equals(Long.class.getCanonicalName())) {
            methodSuffix = "Long";
            nullable = true;
        } else if (fieldType.equals(Float.class.getCanonicalName())) {
            methodSuffix = "Float";
            nullable = true;
        } else if (fieldType.equals(Double.class.getCanonicalName())) {
            methodSuffix = "Double";
            nullable = true;
        } else if (fieldType.equals(String.class.getCanonicalName())) {
            methodSuffix = "String";
            nullable = true;
        } else if (fieldType.equals(CharSequence.class.getCanonicalName())) {
            methodSuffix = "CharSequence";
            nullable = true;
        } else if (fieldType.equals(PARCELABLE_CLASS)) {
            methodSuffix = "ParcelableArray";
            nullable = true;
        } else if (fieldType.equals(boolean[].class.getCanonicalName())) {
            methodSuffix = "BooleanArray";
            nullable = true;
        } else if (fieldType.equals(byte[].class.getCanonicalName())) {
            methodSuffix = "ByteArray";
            nullable = true;
        } else if (fieldType.equals(short[].class.getCanonicalName())) {
            methodSuffix = "ShortArray";
            nullable = true;
        } else if (fieldType.equals(int[].class.getCanonicalName())) {
            methodSuffix = "IntegerArray";
            nullable = true;
        } else if (fieldType.equals(long[].class.getCanonicalName())) {
            methodSuffix = "LongArray";
            nullable = true;
        } else if (fieldType.equals(float[].class.getCanonicalName())) {
            methodSuffix = "FloatArray";
            nullable = true;
        } else if (fieldType.equals(double[].class.getCanonicalName())) {
            methodSuffix = "DoubleArray";
            nullable = true;
        } else if (fieldType.equals(String[].class.getCanonicalName())) {
            methodSuffix = "StringArray";
            nullable = true;
        } else if (fieldType.equals(CharSequence[].class.getCanonicalName())) {
            methodSuffix = "CharSequenceArray";
            nullable = true;
        } else if (fieldType.equals(BUNDLE_CLASS)) {
            methodSuffix = "Bundle";
            nullable = true;
        } else if (fieldType.equals(STRING_ARRAY_LIST_CLASS)) {
            methodSuffix = "StringArrayList";
            nullable = true;
        } else if (fieldType.equals(INTEGER_ARRAY_LIST_CLASS)) {
            methodSuffix = "IntegerArrayList";
            nullable = true;
        } else if (fieldType.equals(PARCELABLE_ARRAY_LIST_CLASS)) {
            methodSuffix = "ParcelableArrayList";
            nullable = true;
        } else if (fieldType.equals(CHAR_SEQUENCE_ARRAY_LIST_CLASS)) {
            methodSuffix = "CharSequenceArrayList";
            nullable = true;
        } else if (fieldType.equals(PARCELABLE_CLASS) || interfaces.contains(PARCELABLE_CLASS)) {
            methodSuffix = "Parcelable";
            nullable = true;
        } else if (fieldType.equals(Serializable.class.getCanonicalName())
                || interfaces.contains(Serializable.class.getCanonicalName())) {
            methodSuffix = "Serializable";
            nullable = true;
        } else {
            methodSuffix = null;
        }
        if (methodSuffix == null) {
            throw new IllegalArgumentException("Type " + fieldType + " is not recognized");
        }
    }

    @Override
    public void writeGetter(Writer writer, String targetName, String bundleName) throws IOException {
        writer.append("        if(").append(bundleName).append(".containsKey(\"")
                .append(argumentKeyName).append("\")){\n");
        writer.append("            ").append(targetName).append(".").append(fieldName).append(" = (")
                .append(fieldType).append(")")
                .append(bundleName).append(".get").append(methodSuffix)
                .append("(\"").append(argumentKeyName).append("\"").append(");\n");
        writer.append("        }\n");
    }

    @Override
    public void writePutter(Writer writer, String targetName, String bundleName) throws IOException {
        if (nullable) {
            writer.append("        if(").append(targetName).append(".").append(fieldName).append(" != null) {\n    ");
        }
        writer.append("        ").append(bundleName).append(".put").append(methodSuffix).append("(\"")
                .append(argumentKeyName).append("\",")
                .append(targetName).append(".").append(fieldName).append(");\n");
        if (nullable) {
            writer.append("        }\n");
        }
    }
}
