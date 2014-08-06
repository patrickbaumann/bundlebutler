package bundlebutler;

import android.os.Bundle;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by patrick on 8/5/14.
 */
@RunWith(RobolectricTestRunner.class)
public class PrimitiveTest {

    @Test
    public void testFragmentArgsBehaviorsWithLong() {
        PrimitivesFragment fragment = new PrimitivesFragment();

        fragment.pLong = 5L;
        BundleButler.saveArgs(fragment);
        Assert.assertEquals("saveArgs should store latest value.", 5L, fragment.getArguments().getLong(PrimitivesFragment.KEY_LONG));

        fragment.pLong = 7L;
        Bundle state = new Bundle();
        BundleButler.saveState(fragment, state);
        Assert.assertEquals("saveState should store to state bundle", 7L, state.getLong(PrimitivesFragment.KEY_LONG));

        state.putLong(PrimitivesFragment.KEY_LONG, 10L);
        BundleButler.loadArgsWithState(fragment, state);
        Assert.assertEquals("loadArgsWithState with state should override args and default", 10L, fragment.pLong);

        fragment.pLong = 13L;
        BundleButler.loadArgsWithState(fragment, null);
        Assert.assertEquals("loadArgsWithState with no state should reset to arg value", 5L, fragment.pLong);

    }

    @Test
    public void testAllPrimitiveTypes() {
        PrimitivesFragment fragment = new PrimitivesFragment();

        fragment.pBoolean = true;
        fragment.pByte = 0x79;
        fragment.pChar = 'p';
        fragment.pDouble = 99.5D;
        fragment.pInt = 99;
        fragment.pLong = 50L;

        BundleButler.saveArgs(fragment);

        Bundle arguments = fragment.getArguments();

        Assert.assertEquals(true, arguments.getBoolean(PrimitivesFragment.KEY_BOOLEAN));
        Assert.assertEquals(0x79, arguments.getByte(PrimitivesFragment.KEY_BYTE));
        Assert.assertEquals('p', arguments.getChar(PrimitivesFragment.KEY_CHAR));
        Assert.assertEquals(99.5D, arguments.getDouble(PrimitivesFragment.KEY_DOUBLE));
        Assert.assertEquals(99, arguments.getInt(PrimitivesFragment.KEY_INT));
        Assert.assertEquals(50L, arguments.getLong(PrimitivesFragment.KEY_LONG));
    }
}
