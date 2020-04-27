package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogUnitsHolder;
import com.github.invictum.reportportal.SuiteStorage;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

@RunWith(JUnit4.class)
public class RegularTest {

    private SuiteStorage suiteStorageMock;
    private Launch launchMock;
    private LogUnitsHolder logUnitsHolderMock;

    @Test
    public void recordTest() {
        suiteStorageMock = Mockito.mock(SuiteStorage.class);
        launchMock = Mockito.mock(Launch.class);
        logUnitsHolderMock = Mockito.mock(LogUnitsHolder.class);
        TestRecorder recorder = new Regular(suiteStorageMock, launchMock, logUnitsHolderMock);
        TestOutcome testOutcome = Mockito.mock(TestOutcome.class);
        Mockito.when(testOutcome.getUserStory()).thenReturn(Story.called("story").withNarrative("narrative"));
        ZonedDateTime start = ZonedDateTime.now();
        Mockito.when(testOutcome.getStartTime()).thenReturn(start);
        Mockito.when(testOutcome.getName()).thenReturn("Test name");
        recorder.record(testOutcome);
        Mockito.verify(suiteStorageMock, Mockito.times(1)).start(Mockito.eq("story"), Mockito.any());
        Mockito.verify(suiteStorageMock, Mockito.times(1)).suiteFinisher(Mockito.eq("story"), Mockito.any());
    }
}
