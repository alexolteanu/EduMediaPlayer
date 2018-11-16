package com.edu.edumediaplayer.playback;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class VisualizerTest {
    private Visualizer vis;

    @Before
    public void setup() {
        vis = new Visualizer(null, null, null, null);
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
}