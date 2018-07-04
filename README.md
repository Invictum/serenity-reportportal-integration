[![Codacy Badge](https://api.codacy.com/project/badge/Grade/25f54d5b5d4a4ee083b5b2a969380b80)](https://www.codacy.com/app/zim182/serenity-reportportal-integration?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Invictum/serenity-reportportal-integration&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/Invictum/serenity-reportportal-integration.svg?branch=develop)](https://travis-ci.org/Invictum/serenity-reportportal-integration)
[![Version](https://img.shields.io/github/release/Invictum/serenity-reportportal-integration.svg)](https://github.com/Invictum/serenity-reportportal-integration/releases/latest)

Serenity integration with Report Portal
=======================================

Module allows to report Serenity powered tests to [reportportal.io](http://reportportal.io). Supplies additional reporting facility to Serenity based test automation frameworks.

Setup
-------------
To add support of integration between Serenity and Report Portal simply add dependencies to your project based on used build tool.

> **Warning**
> Don't add any extra Report Portal listeners or agents. Integration is provided by single module for all availavle Serenity approaches

**Maven**

Edit project's `pom.xml` file
```
<dependency>
   <groupId>com.github.invictum</groupId>
   <artifactId>serenity-reportportal-integration</artifactId>
   <version>1.1.3</version>
</dependency>
```
Report Portal core libraries are used, but they placed in repository, so its URL also should be added to your build configuration
```
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>bintray-epam-reportportal</id>
        <name>bintray</name>
        <url>http://dl.bintray.com/epam/reportportal</url>
    </repository>
</repositories>
```

**Gradle**

Edit `build.gradle` file in the project root
```
compile group: 'com.github.invictum', name: 'serenity-reportportal-integration', version: '1.1.3'
```
External Report Portal repository should be defined the same as for Maven
```
repositories {
    maven {
        url "http://dl.bintray.com/epam/reportportal"
    }
}
```

Actually from this point setup of integration is done. The only thing you should to do is to configure Report Portal itself. In general it means just adding of `reportportal.properties` file to you project tests root. Properties example is described below
```
rp.endpoint = http://report-portal-url
rp.uuid = 385bha54-c1df-42c7-afa4-9e4c028930af
rp.launch = My_Cool_Launch
rp.project = My_Cool_Project
```
For more details related to Report Portal configuration please refer to [Report Portal Documentation](http://reportportal.io/docs/JVM-based-clients-configuration).

Now run your tests normally and report should appear on Report Portal in accordance to provided configuration. To add custom messages to Report Portal, you may emit logs in any place in your test
```
ReportPortal.emitLog("My message", "INFO", Calendar.getInstance().getTime());
```
Message will appear in the scope of entity it was triggered. I. e. inside related test.
> **Notice**
> Actually to add logs to Report Portal, they should be emitted in scope of test method, otherwise they will not be displayed at all

**Native Serenity reporting**

Serenity TAF provides its own reporting facility, but `serenity-reportportal-integration` may be used in parralel with it or independently. Both reporting mechanisms should be configured accordingly and do not depends on each other.

Integration configuration
-------------

Section discribes all available integration configurations.

**Profiles**

Each Serenity `TestStep` object is passed through chain of configured `StepProcessors`. This approach allows to flexible configure reporting behaviour on the step level. All configuration is accessible from the code. By default integration provides following configuration profiles:

- DEFAULT
- FULL
- TREE_OPTIMIZED
- CUSTOM

`DEFAULT` profile is used by default and contains all usually required reporting details. It generates in Report Portal a nice log that does not cluttered with extra details.

`FULL` profile contains all available `StepProcessors` and generates full reporting.

`TREE_OPTIMIZED` profile is sutable to use with `TREE` handler type. Reffer to Handler Type section for more details.

To customize what should be logged `CUSTOM` profile should be used.
```
StepsSetProfile config = StepsSetProfile.CUSTOM;
config.registerProcessors(new StartStepLogger(), new FinishStepLogger());
ReportIntegrationConfig.profile = config;
```
In example above `CUSTOM` profile with `StartStepLogger` and `FinishStepLogger` processors is configured.

**Processors**

All step processors available out of the box may be observed in `com.github.invictum.reportportal.processor` package.
For now following processors are available:
- `StartStepLogger` logs all started steps.
- `FinishStepLogger` logs all finished steps. Log level depends on step results.
- `ErrorLogger` reports error if present. Includes regular errors as well as assertion fails. By default full stack strace will be reported. But it is possible to pass a function to the constructor in order to implement any logic for message formatting.
```
StepsSetProfile config = StepsSetProfile.CUSTOM;
// Report full stack stace supplied by Serenity
ErrorLogger errorLogger = new ErrorLogger();
// Report a short error description provided by Serenity
ErrorLogger errorLogger = new ErrorLogger(TestStep::getConciseErrorMessage);
config.registerProcessors(errorLogger);
```
- `ScreenshotAttacher` emits screenshots to RP if present. It simply attaches all available step's screenshots, so screenshot strategy is configured on Serenity level.
- `HtmlSourceAttacher` logs page source if available.
- `SeleniumLogsAttacher` reports logs supplied by Selenium. It is possible to pass predicate to constructor in order to push particular logs. By default emits all available logs.

It is possible to use integrated processors as well as implemented by your own. To make own processor implement `StepProcessor` interface. In custom implementation access to Serenity's `TestStep` object is provided
```
public class MyCustomLoggerLogger implements StepProcessor {

    @Override
    public void proceed(final TestStep step) {
        // You logic here to emit logs
    }
}
```
> **Warning**
> To emit log to Report Portal time should be specified. If log timestamp is out of range of step it won't be emitted at all. `TestStep` object contains all data to calculate start, end dates and duration

The order of processors registration is matters, this order the same as order of invocation.

**Handler type (expiremental feature)**

Integration provides two strategies of storing Serenity's test data to Report Portal facility:
- *FLAT* (default behaviour) - Represents steps data as plain logs emmited to the test scope
- *TREE* - Reconstructs steps structure as a tree representation in RP

Report Portal has a few limitations regurding to flexible nested structures support for now. As a result test report may contain some inaccuracate data.
E. g. test count for launch will show total number of tests + total number of steps.

Nevertheless `TREE` configuration allows to get additional features with RP. E. g. integrated RP test analisys facility scope will be changed from test to step.

Handler type may be changed with following configuration
```
ReportIntegrationConfig.handlerType = HandlerType.TREE;
```

**Narrative formatter**

By default, narrative is formatted as a bullet list before storring to the test description field. It is possible to alter this logic in accordance to project needs.

To achieve it implement `NarrativeFormatter` interface and define your own implementation of formatter. For example
```
public class NumberedListFormatter implements NarrativeFormatter {

    @Override
    public String format(String[] strings) {
        return IntStream.range(0, strings.length).mapToObj(index -> (index + 1) + ". " + strings[index])
                .collect(Collectors.joining("\n"));
    }
}
```

Code snippet above will format narrative lines as a numbered list.
```
Initial lines
line 1, line 2

Result lines
1. line 1
2. line 2
```

Custom `NarrativeFormatter` should be registered via configuration
```
ReportIntegrationConfig.narrativeFormatter = new NumberedListFormatter();
```

> **Notice**
All integration configurations should be provided before Serenity facility init (For example on `@BeforeClass` method on the parent test class for jUnit style tests). Otherwise default values will be used.

Data mapping
-------------

Serenity framework and Report Portal facility have a different entities structure. This section explains how data related to each other in mentioned systems.

**Name** relation is straightforward.

 Serenity   | Report portal
------------|---------------
Test Class  | Suite
Test Method | Test
Scenario    | Test
Step        | Log

**Description** Each non-log entity in Report Portal may has a description. This field is populated from Serenity narrative section for both jUnit and BDD test sources.

For jBehave there is a special Narrative section in the story
```
Story example

Narrative:
Some simple example text

Scenario: Simple scenario
Given for simple scenario
When for simple scenario
Then for simple scenario
```

For jUnit there is a `@Narrative` annotation. Each line of narrative text will be concatinated
```
@RunWith(SerenityRunner.class)
@Narrative(text = {"line 1", "line 2"})
public class SimpleTest {
    ...
}
```

**Tags** supplying depends on test source.
For jBehave (BDD) tests tags is defined in Meta section with `@tag` or `@tags` keyword
```
Story example

Meta:
@tags scope:smoke

Scenario: Simple scenario
Given for simple scenario
When for simple scenario
Then for simple scenario
```

For jUnit `@WithTagValuesOf` annotation is provided
```
@RunWith(SerenityRunner.class)
@WithTagValuesOf("scope:smoke")
public class SimpleTest {
    ...
}
```

Versioning
----------
Report Portal integration uses 3 digit version format - x.y.z

**z** - regular release increment version. Includes bugfix and extending with minor features. Also includes Serenity and Report Portal modules versions update. Backward compatibility is guaranteed.

**y** - minor version update. Includes major Serenity and Report portal core modules update. Backward compatibility for Serenity and Report Portal are not guaranteed.

**x** - major version update. Dramatically changed integration architecture. Backward compatibility doesn't guaranteed. Actually increment of major version is not expected at all

Important release notes
-----------------------
Important release notes are described below. Use [releases](https://github.com/Invictum/serenity-reportportal-integration/releases) section for details regarding regular release notes.

 Version       | Note
---------------|---------------------------
1.0.0 - 1.0.6  | Supports RP v3 and below
1.1.0+         | Minor version update due RP v4 release. Versions older than 1.1.0 are not compatible with RP v4+ and vise versa

Limitations
-------------
Integration does not support concurrency for parametrized Serenity tests execution.

Report Portal launch finish timestamp is calculated before Java VM shutdown. Overall launch duration will also include the time of Serenity report generation.
