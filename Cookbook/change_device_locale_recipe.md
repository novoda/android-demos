Change the Device Locale for Instrumentation Tests
===================

Internationalisation can be hard on Android so you might end up writing automated tests that should run on a `Device` with a specific `Locale`.

To avoid code duplication you can use this `TestRule`, which changes the `Locale` of a device to a given one before a test runs and afterwards back to the original one.

``` java
class LocaleTestRule implements TestRule {

    private final Locale locale;

    LocaleTestRule(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
               Locale defaultLocale = Locale.getDefault();
               try {
                   changeLocaleTo(locale);
                   base.evaluate();

               } finally {
                   changeLocaleTo(defaultLocale);
               }
            }

            private void changeLocaleTo(Locale locale) {
                Locale.setDefault(locale);

                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                configuration.setLocale(locale);
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            }
        };
    }

    private Resources getResources() {
        return InstrumentationRegistry.getInstrumentation()
                .getTargetContext()
                .getApplicationContext().getResources();
    }
}
```

[JUnit TestRules](http://junit.org/junit4/javadoc/4.12/org/junit/rules/TestRule.html) were introduced with JUnit4 and allow you to perform necessary setup or cleanup for tests and can be easily shared between test classes.

You can use this rule together with the `ActivityTestRule` in your instrumentation test.

``` java
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public RuleChain chain = RuleChain
            .outerRule(new LocaleTestRule(Locale.GERMAN))
            .around(new ActivityTestRule<>(MainActivity.class));

    @Test
    public void test() throws Exception {
        // test your locale specific code here
    }
}
```
