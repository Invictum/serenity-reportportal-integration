Serenity integration with ReportPortal
===================

Module allows to report Serenity powered tests to [reportportal.io]((http://reportportal.io)).

Setup
-------------
To add support of Serenity with Report Portal integration simply add dependencies to your project
```
<dependency>
   <groupId>com.github.invictum</groupId>
   <artifactId>serenity-reportportal-integration</artifactId>
   <version>1.0.4</version>
</dependency>
```
Report Portal core libraries are used, but it uses external repository, so it URL also should be added to your build configuration
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
Actually from this point setup of integration is done. The only thing you should to do is to configure Report Portal itself. In general it means just adding of `reportportal.properties` file to you project. Properties example is described below
```
rp.endpoint = http://report-portal-url
rp.uuid = 385bha54-c1df-42c7-afa4-9e4c028930af
rp.launch = My_Cool_Launch
rp.project = My_Cool_Project
```
For more details related to Report Portal configuration please reffer to [Report Portal Documentation](http://reportportal.io/docs/JVM-based-clients-configuration).

Now run your tests normally and report should appear on Report Portal in accordance to provided configuration. To add custom messages to Report Portal, you may emit logs in any place in your test
```
ReportPortal.emitLog("My message", "INFO", Calendar.getInstance().getTime());
```
Message will appear in the scope of entity it was triggered. I. e. inside related step.
> **Notice**
> Actually to add logs to Report Portal, they should be emitted in scope of test method

Integration configuration
-------------

Each Serenity `TestStep` is passed throug chain of configured `StepProcessors`. This approach allows to flexible configure reporting behaviour on the step level. All configuration is accessible from the code. By default integration provides two configuration profiles:

- DEFAULT
- CUSTOM

`DEFAULT` profile is used by default and contains all usually required reporting details. To change default behavior `CUSTOM` profile shoul be used.
```
StepsSetProfile config = StepsSetProfile.CUSTOM.registerProcessors(new ScreenshotAttacher());
ReportIntegrationConfig.useProfile(config);
```
In example above `CUSTOM` profile with `ScreenshotAttacher` processor is configured. All step processors available out of the box may be observed in `com.github.invictum.reportportal.processor` package.
It is possible to use integrated processors as well as implemented by your own
```
public class StartStepLogger implements StepProcessor {

    @Override
    public void proceed(final TestStep step) {
        // You logic here to emit logs
    }
}
```
> **Warning**
To emit log to Report Portal date should be specified. If log timestams is out of range of step it won't be emitted at all. `TestStep` contains all data to calculate start, end dates and duration

The order of processors registtration is matters, this order the same as order of invocation.

> **Notice**
Profile configuration should be provided before Serenity facility init (For example on `@BeforeClass` method on the parent test class). Otherwise default profile will be used.

Limitations
-------------
Integration does not support concurency for parametrized Serenity tests execution.
