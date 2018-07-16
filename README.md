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
> Don't add any extra Report Portal listeners or agents. Integration is provided by single module for all available Serenity approaches

**Maven**

Edit project's `pom.xml` file
```
<dependency>
   <groupId>com.github.invictum</groupId>
   <artifactId>serenity-reportportal-integration</artifactId>
   <version>1.2.0</version>
</dependency>
```
Report Portal core libraries are used, but they placed in a separate repository, so its URL also should be added to your build configuration
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
compile: 'com.github.invictum:serenity-reportportal-integration:1.2.0'
```
External Report Portal repository should be defined as the same as for Maven
```
repositories {
    maven {
        url "http://dl.bintray.com/epam/reportportal"
    }
}
```

Actually from this point setup of integration is done. The only thing you have to do is to configure Report Portal itself. In general it means just adding of `reportportal.properties` file to you project tests root. Minimal properties example is described below:
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
It is also possible to use Report portal integration with log frameworks in order to push messages to RP server. Please refer to [Report Portal loggin integtation](http://reportportal.io/docs/Logging-Integration) for more details related to it.

> **Notice**
> Actually to add logs to Report Portal, they should be emitted in scope of test method, otherwise they will not be displayed at all

**Native Serenity reporting**

Serenity TAF may produce its own reporting facility via separate plugins. But `serenity-reportportal-integration` may be used in parallel with it or independently. Both reporting mechanisms should be configured accordingly and do not depends on each other.

Integration configuration
-------------

All available configurations are provided via `ReportIntegrationConfig` object. Each set method returns object itself, so chain of configuration is possible:
```
ReportIntegrationConfig configuration = ReportIntegrationConfig.get();
configuration.useHandler(HandlerType.TREE).useProfile(StepsSetProfile.TREE_OPTIMIZED);
```

> **Notice**
All integration configurations should be provided before Serenity facility init (For example on `@BeforeClass` method on the parent test class for jUnit style tests). Otherwise default values will be used.

**Profiles**

Each Serenity `TestStep` object is passed through chain of configured `StepDataExtractors`. This approach allows to flexible configure reporting behaviour on a step level. By default integration provides following configuration profiles:

- DEFAULT
- FULL
- TREE_OPTIMIZED
- CUSTOM

`DEFAULT` profile is used by default and contains all usually required reporting details. It generates in Report Portal a nice log that does not cluttered with extra details.

`FULL` profile contains all available `StepDataExtractors` and generates full reporting. It suitable for demo purposes in order to choose a set of processors.

`TREE_OPTIMIZED` profile is suitable to use with `TREE` handler type. Refer to Handler Type section for more details.

To configure what should be logged manually `CUSTOM` profile should be used. In following example `CUSTOM` profile with `StartStep` and `FinishStep` executors is configured.
```
StepsSetProfile profile = StepsSetProfile.CUSTOM;
profile.registerProcessors(new StartStep(), new FinishStep());
ReportIntegrationConfig.get().useProfile(profile);
```

**Executors**

All step executors are available out of the box may be observed in `com.github.invictum.reportportal.extractor` package.
For now following processors are available:
- `StartStep` retrieves step's data relevant to its start.
- `FinishStep` extracts step's data related to its finish. Log level depends on step result.
- `StepError` extracts step's error if present. Includes regular errors as well as assertion fails. By default full stack trace will be reported. But it is possible to pass a function to the constructor in order to implement any logic for message formatting.
```
StepsSetProfile profile = StepsSetProfile.CUSTOM;
// Extract a full stack stace supplied by Serenity
StepError error = new StepError();
// Extract only a short error description provided by Serenity
StepError error = new StepError(TestStep::getConciseErrorMessage);
profile.registerProcessors(error);
```
- `StepScreenshots` extracts screenshots if present. It simply retrieves all available step's screenshots, so screenshot strategy is configured on Serenity level.
- `StepHtmlSources` extracts page source if available.
- `SeleniumLogs` retrieves logs supplied by Selenium. By default extracts all available logs. It is possible to pass predicate to constructor in order to push particular logs.
```
StepsSetProfile profile = StepsSetProfile.CUSTOM;
// Collect only browser related logs
SeleniumLogs logs = new SeleniumLogs(log -> log.getType().contentEquals("browser"));
profile.registerProcessors(logs);
```

It is possible to use integrated extractors as well as implemented by your own. To make own extractor implement `StepDataExtractor` interface. In custom implementation access to Serenity's `TestStep` object is provided.
For example let's implement extractor that generates a log message before each step start (yeap it is pretty useful)
```
public class GreetingExtractor implements StepDataExtractor {

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        Date startDate = Utils.stepStartDate(step);
        // Create an istance of EnhancedMessage that is able to hold a log message
        EnhancedMessage message = new EnhancedMessage("Hello from started step " + step.getDescription());
        // Do not forget to set propper log level and timestamp
        message.withDate(startDate).withLevel(Utils.logLevel(step.getResult());
        // Extractor may produce several logs for emit, but this example supplyies only one
        return Collections.singleton(message);
    }
}
```
> **Warning**
> To emit log to Report Portal date should be specified. If log timestamp is out of range of step it won't be emitted at all. `TestStep` object contains all data to calculate start, end dates and duration

Extracted collection of `EnhancedMessage` will be used to push logs to to Report Portal and their order will be based on timestamp.

**Handler type (experimental feature)**

Integration provides two strategies of storing Serenity's test data to Report Portal facility:
- *FLAT* (default behaviour) - Represents steps data as plain logs emitted to the test scope
- *TREE* - Reconstructs steps structure as a tree representation in RP

Report Portal has a few limitations regarding to flexible nested structures support for now. As a result test report may contain some inaccurate data.
E. g. test count for launch will show total number of tests + total number of steps.

Nevertheless `TREE` configuration allows to get additional features with RP. E. g. integrated RP test analysis facility scope will be changed from test to step.

Handler type may be changed with following configuration
```
ReportIntegrationConfig.get().useHandler(HandlerType.TREE);
```

**Narrative formatter**

By default, narrative is formatted as a bullet list before storing to the test description field. It is possible to alter this logic in accordance to project needs.

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
ReportIntegrationConfig.get().useNarrativeFormatter(new NumberedListFormatter());
```

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
1.1.0 - 1.1.3  | Minor version update due RP v4 release. Versions older than 1.1.0 are not compatible with RP v4+ and vise versa
1.2.0+         | Minor version updated due Configuration approach refactoring. New configuratiion approach is not compatible with versions under 1.2.0

Limitations
-------------
Integration does not support concurrency for parametrized Serenity tests execution.

Report Portal launch finish timestamp is calculated before Java VM shutdown. Overall launch duration will also include the time of Serenity report generation.
