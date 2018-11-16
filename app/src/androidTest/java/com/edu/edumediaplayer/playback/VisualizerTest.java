package com.edu.edumediaplayer.playback;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VisualizerTest {
    private Visualizer vis;

    @Before
    public void setup() {
        vis = new Visualizer(null, null, null, InstrumentationRegistry.get);
    }
    @Test
    public void feedTest() {
        vis.setBitrate(3);  // assuming setBitrate was tested by another test

        short[] data = {1, -1, 2, 0, 3, 1};
        vis.feed(data);

        List<Integer> expectedLeftChannelData = Arrays.asList(2);
        List<Integer> expectedRightChannelData = Arrays.asList(0);

        assertThat(vis.getLeftChannelData(), is(expectedLeftChannelData));
        assertThat(vis.getRightChannelData(), is(expectedRightChannelData));
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = ;

        assertEquals("com.edu.edumediaplayer", appContext.getPackageName());
    }
}
