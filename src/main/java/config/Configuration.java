package config;

import org.aeonbits.owner.ConfigFactory;

public final class Configuration {

    private static final TestConfig INSTANCE = ConfigFactory.create(TestConfig.class);

    private Configuration() {
        // Prevent instantiation
    }

    /**
     * Gets the singleton instance of the TestConfig.
     * @return Thread-safe configuration instance.
     */
    public static TestConfig get() {
        return INSTANCE;
    }
}
