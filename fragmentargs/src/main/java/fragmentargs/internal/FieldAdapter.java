package fragmentargs.internal;

import java.io.IOException;
import java.io.Writer;

interface FieldAdapter {
    void writeGetter(Writer writer, String targetName, String bundleName) throws IOException;
    void writePutter(Writer writer, String targetName, String bundleName) throws IOException;
}
