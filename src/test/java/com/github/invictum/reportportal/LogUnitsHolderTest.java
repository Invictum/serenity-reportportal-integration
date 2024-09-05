package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import net.thucydides.model.domain.TestStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

@ExtendWith(MockitoExtension.class)
public class LogUnitsHolderTest {

    @Mock
    private TestStep stepMock;

    @Mock
    private Function<TestStep, Collection<SaveLogRQ>> logUnitMock;

    @Test
    public void proceedTest() {
        LogUnitsHolder holder = new LogUnitsHolder();
        holder.register(logUnitMock);
        Mockito.when(logUnitMock.apply(stepMock)).thenReturn(new HashSet<>());
        holder.proceed(stepMock);
        Mockito.verify(logUnitMock, Mockito.times(1)).apply(stepMock);
    }
}
