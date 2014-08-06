package fragmentargs;

import android.app.Fragment;
import android.os.Bundle;
import fragmentargs.internal.ArgumentWrapper;

import java.util.HashMap;
import java.util.Map;

public final class FragmentArgs {

    private FragmentArgs() {
    }

    private static final Map<Class<Fragment>, ArgumentWrapper> ARGUMENT_WRAPPERS =
            new HashMap<Class<Fragment>, ArgumentWrapper>();

    public static void saveArgs(Fragment target) {
        Bundle args = target.getArguments();
        if (args == null) {
            args = new Bundle();
        }

        ArgumentWrapper argWrapper = getWrapperFor((Class<Fragment>) target.getClass());
        argWrapper.saveArgs(target, args);

        target.setArguments(args);
    }

    public static void loadArgsWithState(Fragment target, Bundle savedInstanceState) {
        ArgumentWrapper argWrapper = getWrapperFor((Class<Fragment>) target.getClass());

        Bundle args = target.getArguments();
        if (args != null) {
            argWrapper.loadArgs(target, target.getArguments());
        }
        if (savedInstanceState != null) {
            argWrapper.loadArgs(target, savedInstanceState);
        }
    }

    public static void saveState(Fragment target, Bundle instanceState) {
        ArgumentWrapper argWrapper = getWrapperFor((Class<Fragment>) target.getClass());

        argWrapper.saveArgs(target, instanceState);

    }

    private static ArgumentWrapper getWrapperFor(Class<Fragment> cls) {
        if (ARGUMENT_WRAPPERS.containsKey(cls)) {
            return ARGUMENT_WRAPPERS.get(cls);
        }

        final String className =
                new StringBuilder(cls.getCanonicalName()).append("$$").append("ArgumentWrapper").toString();
        ArgumentWrapper argumentWrapper;
        try {
            argumentWrapper = (ArgumentWrapper) Class.forName(className).newInstance();
        } catch (ClassNotFoundException e) {
            throw new WrapperNotFoundException(className);
        } catch (InstantiationException e) {
            throw new WrapperNotFoundException(className);
        } catch (IllegalAccessException e) {
            throw new WrapperNotFoundException(className);
        }

        ARGUMENT_WRAPPERS.put(cls, argumentWrapper);
        return argumentWrapper;
    }


    private static class WrapperNotFoundException extends RuntimeException {
        private final String className;

        public WrapperNotFoundException(String className) {
            super(className + " does not have a wrapper!");
            this.className = className;
        }

        public String getClassName() {
            return className;
        }
    }
}
