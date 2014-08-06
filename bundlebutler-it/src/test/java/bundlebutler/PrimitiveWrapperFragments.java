package bundlebutler;

import android.app.Fragment;

/**
 * Created by patrick on 8/5/14.
 */
public class PrimitiveWrapperFragments extends Fragment {

    public static final String KEY_LONG = "Long";
    public static final String KEY_BOOLEAN = "Boolean";
    public static final String KEY_BYTE = "Byte";
    public static final String KEY_INT = "Integer";
    public static final String KEY_CHAR = "Character";
    public static final String KEY_DOUBLE = "Double";

    @Argument(KEY_LONG)
    Long pLong = 0L;
    @Argument(KEY_BOOLEAN)
    Boolean pBoolean = false;
    @Argument(KEY_BYTE)
    Byte pByte = 0x00;
    @Argument(KEY_INT)
    Integer pInt = 0;
    @Argument(KEY_CHAR)
    Character pChar = 0;
    @Argument(KEY_DOUBLE)
    Double pDouble = 0D;

}
