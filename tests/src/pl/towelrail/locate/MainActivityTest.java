package pl.towelrail.locate;

import android.test.ActivityInstrumentationTestCase2;
import android.test.FlakyTest;
import android.test.suitebuilder.annotation.SmallTest;
import junit.framework.Assert;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class pl.towelrail.locate.MainActivityTest \
 * pl.towelrail.locate.tests/android.test.InstrumentationTestRunner
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        setActivityInitialTouchMode(false);
    }

    @FlakyTest
    public void testMain() throws Exception {
        Assert.assertEquals(1, 2);
    }

    @SmallTest
    public void testOther() throws Exception {
        Assert.assertEquals(2,2*2/2);
    }
}
