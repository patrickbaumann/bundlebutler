package fragmentargs.internal;

import android.os.Bundle;

public interface ArgumentWrapper {
    void saveArgs(Object target, Bundle args);
    void loadArgs(Object target, Bundle args);
}
