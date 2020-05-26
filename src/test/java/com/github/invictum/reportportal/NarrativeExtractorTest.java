package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.function.Function;

@RunWith(JUnit4.class)
public class NarrativeExtractorTest {

    private TestOutcome testMock;
    private Story storyMock;

    @Before
    public void beforeTest() {
        testMock = Mockito.mock(TestOutcome.class);
        storyMock = Mockito.mock(Story.class);
        Mockito.when(testMock.getUserStory()).thenReturn(storyMock);
    }

    @Test
    public void storyNarrative() {
        Mockito.when(storyMock.getNarrative()).thenReturn("narrative");
        NarrativeExtractor extractor = new NarrativeExtractor(testMock, narrative -> null);
        Assert.assertEquals("narrative", extractor.extract().get());
    }

    @Test
    public void noNarrative() {
        Mockito.when(storyMock.getNarrative()).thenReturn(null);
        Mockito.when(testMock.getTestCase()).then(i -> Object.class);
        NarrativeExtractor extractor = new NarrativeExtractor(testMock, narrative -> "text");
        Assert.assertFalse(extractor.extract().isPresent());
    }

    @Test
    public void classNarrative() {
        Mockito.when(storyMock.getNarrative()).thenReturn(null);
        Mockito.when(testMock.getTestCase()).then(i -> TestInstance.class);
        Function<Narrative, String> map = narrative -> String.join("", narrative.text());
        NarrativeExtractor extractor = new NarrativeExtractor(testMock, map);
        Assert.assertEquals("line 1line 2", extractor.extract().get());
    }
}
