package bundlebutler;

import android.app.Fragment;

/**
 * Created by patrick on 8/5/14.
 */
public class PrimitivesFragment extends Fragment {

    public static final String KEY_LONG = "long";
    public static final String KEY_BOOLEAN = "boolean";
    public static final String KEY_BYTE = "byte";
    public static final String KEY_INT = "int";
    public static final String KEY_CHAR = "char";
    public static final String KEY_DOUBLE = "double";

    @Argument(KEY_LONG)
    long pLong = 0L;
    @Argument(KEY_BOOLEAN)
    boolean pBoolean = false;
    @Argument(KEY_BYTE)
    byte pByte = 0x00;
    @Argument(KEY_INT)
    int pInt = 0;
    @Argument(KEY_CHAR)
    char pChar = 0;
    @Argument(KEY_DOUBLE)
    double pDouble = 0;

}
