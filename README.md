Serenity integration with ReportPortal
===================

Module allows to report Serenity powered tests to [Report Portal]((http://reportportal.io)).

Setup
-------------
To add support of Serenity with Report Portal integration simply add dependencies to your project
```
<dependency>
   <groupId>com.github.invictum</groupId>
   <artifactId>serenity-reportportal-integration</artifactId>
   <version>1.0.3</version>
</dependency>
```
Report Portal uses external repository, so it URL also should be added to your build configuration
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
Actually from this point setup of integration is done. The only thing you should to do is to configure Report Portal itself. This process is described in [Report Portal Documentation](http://reportportal.io/docs/JVM-based-clients-configuration).
Now run your tests normally and report should appear on Report Portal. To add custom messages into report portal, just emit logs
```
ReportPortal.emitLog("My message", "INFO", Calendar.getInstance().getTime());
```
Message will appear in the scope of entity it was triggered. I. e. inside related test or step.

Integration configuration
-------------
Integration provides two reporting styles:

- build steps as nested entities into tree `StepTreeHandler.class` (default)
- build steps as sequence of emited logs `StepFlatHandler.class`

This behaviour may be configured using code snippet
```
ReportIntegrationConfig.setHandlerClass(StepFlatHandler.class);
```
> **Note**
Configuration should be provided only once, before any Serenity facility initialization. For example in `@BeforeClass` method

Limitations
-------------
Integration does not support concurency for parametrized Serenity tests execution.
