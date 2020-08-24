package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogUnitsHolder;
import com.github.invictum.reportportal.SuiteStorage;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

import static com.github.invictum.reportportal.ReportIntegrationConfig.FAILSAFE_RERUN_KEY;
import static com.github.invictum.reportportal.ReportIntegrationConfig.SUREFIRE_RERUN_KEY;

@RunWith(JUnit4.class)
public class RegularTest {

    private SuiteStorage suiteStorageMock;
    private Launch launchMock;
    private LogUnitsHolder logUnitsHolderMock;

    @Before
    public void before() {
        suiteStorageMock = Mockito.mock(SuiteStorage.class);
        launchMock = Mockito.mock(Launch.class);
        logUnitsHolderMock = Mockito.mock(LogUnitsHolder.class);
    }

    @Test
    public void regularRecordTest() {
        System.clearProperty(FAILSAFE_RERUN_KEY);
        System.clearProperty(SUREFIRE_RERUN_KEY);
        TestRecorder recorder = new Regular(suiteStorageMock, launchMock, logUnitsHolderMock);
        TestOutcome testOutcome = Mockito.mock(TestOutcome.class);
        Mockito.when(testOutcome.getUserStory()).thenReturn(Story.called("story").withNarrative("narrative"));
        ZonedDateTime start = ZonedDateTime.now();
        Mockito.when(testOutcome.getStartTime()).thenReturn(start);
        Mockito.when(testOutcome.getName()).thenReturn("Test name");
        recorder.record(testOutcome);
        Mockito.verify(suiteStorageMock,
                Mockito.times(1)).start(
                Mockito.eq("story"), Mockito.any());
        Mockito.verify(suiteStorageMock,
                Mockito.times(1)).suiteFinisher(
                Mockito.eq("story"), Mockito.any());
    }

    @Test
    public void retryRecordCallIsFailPresentTest() {
        System.setProperty(SUREFIRE_RERUN_KEY, "2");
        TestRecorder recorder = new Regular(suiteStorageMock, launchMock, logUnitsHolderMock);
        TestOutcome testOutcome = Mockito.mock(TestOutcome.class);
        Mockito.when(testOutcome.getUserStory()).thenReturn(Story.called("story").withNarrative("narrative"));
        Mockito.when(testOutcome.getId()).thenReturn("testId");
        ZonedDateTime start = ZonedDateTime.now();
        Mockito.when(testOutcome.getStartTime()).thenReturn(start);
        Mockito.when(testOutcome.getName()).thenReturn("Test name");
        recorder.record(testOutcome);
        Mockito.verify(suiteStorageMock,
                Mockito.times(1)).isFailPresent(
                Mockito.eq("story"), Mockito.eq("testId"));
    }

    @Test
    public void retryRecordStoreNewFailTest() {
        System.setProperty(SUREFIRE_RERUN_KEY, "2");
        TestRecorder recorder = new Regular(suiteStorageMock, launchMock, logUnitsHolderMock);
        TestOutcome testOutcome = Mockito.mock(TestOutcome.class);
        Mockito.when(testOutcome.getUserStory()).thenReturn(Story.called("story").withNarrative("narrative"));
        Mockito.when(testOutcome.getId()).thenReturn("testId");
        Mockito.when(testOutcome.getResult()).thenReturn(TestResult.ERROR);
        ZonedDateTime start = ZonedDateTime.now();
        Mockito.when(testOutcome.getStartTime()).thenReturn(start);
        Mockito.when(testOutcome.getName()).thenReturn("Test name");
        Mockito.when(suiteStorageMock.isFailPresent(Mockito.any(), Mockito.any())).thenReturn(false);
        recorder.record(testOutcome);
        Mockito.when(testOutcome.getResult()).thenReturn(TestResult.FAILURE);
        recorder.record(testOutcome);
        Mockito.verify(suiteStorageMock,
                Mockito.times(2)).addFail(
                Mockito.eq("story"), Mockito.eq("testId"));
    }

    @Test
    public void retryRecordClearSuiteIsRetryPassedTest() {
        System.setProperty(FAILSAFE_RERUN_KEY, "1");
        TestRecorder recorder = new Regular(suiteStorageMock, launchMock, logUnitsHolderMock);
        TestOutcome testOutcome = Mockito.mock(TestOutcome.class);
        Mockito.when(testOutcome.getUserStory()).thenReturn(Story.called("story").withNarrative("narrative"));
        Mockito.when(testOutcome.getId()).thenReturn("testId");
        Mockito.when(testOutcome.getResult()).thenReturn(TestResult.SUCCESS);
        ZonedDateTime start = ZonedDateTime.now();
        Mockito.when(testOutcome.getStartTime()).thenReturn(start);
        Mockito.when(testOutcome.getName()).thenReturn("Test name");
        Mockito.when(suiteStorageMock.isFailPresent(Mockito.any(), Mockito.any())).thenReturn(true);
        recorder.record(testOutcome);
        Mockito.verify(suiteStorageMock,
                Mockito.times(1)).removeFail(
                Mockito.eq("story"), Mockito.eq("testId"));
    }


}
