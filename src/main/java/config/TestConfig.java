package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:local-config.properties",
        "classpath:config.properties"
})
public interface TestConfig extends Config {

    @Key("booker.base.url")
    @DefaultValue("https://restful-booker.herokuapp.com")
    String bookerBaseUrl();

    @Key("graphql.base.url")
    @DefaultValue("https://eu-central-1-shared-euc1-02.cdn.hygraph.com/content/clv6lwqu7000001w690st4vix/master")
    String graphqlBaseUrl();

    @Key("demoqa.base.url")
    @DefaultValue("https://demoqa.com")
    String demoqaBaseUrl();

    @Key("ui.browser")
    @DefaultValue("chromium")
    String browser();

    @Key("ui.headless")
    @DefaultValue("true")
    boolean headless();

    @Key("ui.timeout")
    @DefaultValue("30000")
    double timeout();

    @Key("BOOKER_USERNAME")
    String bookerUsername();

    @Key("BOOKER_PASSWORD")
    String bookerPassword();

    @Key("TEST_USER_EMAIL")
    String testUserEmail();
}
