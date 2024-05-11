package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final HivemqLibraryAccessors laccForHivemqLibraryAccessors = new HivemqLibraryAccessors(owner);
    private final JacksonLibraryAccessors laccForJacksonLibraryAccessors = new JacksonLibraryAccessors(owner);
    private final JetbrainsLibraryAccessors laccForJetbrainsLibraryAccessors = new JetbrainsLibraryAccessors(owner);
    private final LogbackLibraryAccessors laccForLogbackLibraryAccessors = new LogbackLibraryAccessors(owner);
    private final MssqlLibraryAccessors laccForMssqlLibraryAccessors = new MssqlLibraryAccessors(owner);
    private final TestcontainersLibraryAccessors laccForTestcontainersLibraryAccessors = new TestcontainersLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for log4j (org.apache.logging.log4j:log4j-core)
     * with versionRef 'log4j'.
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLog4j() {
            return create("log4j");
    }

        /**
         * Creates a dependency provider for mockito (org.mockito:mockito-core)
     * with versionRef 'mockito'.
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMockito() {
            return create("mockito");
    }

    /**
     * Returns the group of libraries at hivemq
     */
    public HivemqLibraryAccessors getHivemq() {
        return laccForHivemqLibraryAccessors;
    }

    /**
     * Returns the group of libraries at jackson
     */
    public JacksonLibraryAccessors getJackson() {
        return laccForJacksonLibraryAccessors;
    }

    /**
     * Returns the group of libraries at jetbrains
     */
    public JetbrainsLibraryAccessors getJetbrains() {
        return laccForJetbrainsLibraryAccessors;
    }

    /**
     * Returns the group of libraries at logback
     */
    public LogbackLibraryAccessors getLogback() {
        return laccForLogbackLibraryAccessors;
    }

    /**
     * Returns the group of libraries at mssql
     */
    public MssqlLibraryAccessors getMssql() {
        return laccForMssqlLibraryAccessors;
    }

    /**
     * Returns the group of libraries at testcontainers
     */
    public TestcontainersLibraryAccessors getTestcontainers() {
        return laccForTestcontainersLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class HivemqLibraryAccessors extends SubDependencyFactory {

        public HivemqLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for mqttClient (com.hivemq:hivemq-mqtt-client)
         * with versionRef 'hivemq.mqttClient'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getMqttClient() {
                return create("hivemq.mqttClient");
        }

    }

    public static class JacksonLibraryAccessors extends SubDependencyFactory {

        public JacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (com.fasterxml.jackson.core:jackson-core)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() {
                return create("jackson.core");
        }

            /**
             * Creates a dependency provider for databind (com.fasterxml.jackson.core:jackson-databind)
         * with versionRef 'jackson'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getDatabind() {
                return create("jackson.databind");
        }

    }

    public static class JetbrainsLibraryAccessors extends SubDependencyFactory {

        public JetbrainsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for annotations (org.jetbrains:annotations)
         * with versionRef 'jetbrains.annotations'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getAnnotations() {
                return create("jetbrains.annotations");
        }

    }

    public static class LogbackLibraryAccessors extends SubDependencyFactory {

        public LogbackLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for classic (ch.qos.logback:logback-classic)
         * with versionRef 'logback'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getClassic() {
                return create("logback.classic");
        }

    }

    public static class MssqlLibraryAccessors extends SubDependencyFactory {

        public MssqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jdbc (com.microsoft.sqlserver:mssql-jdbc)
         * with versionRef 'mssql.jdbc'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJdbc() {
                return create("mssql.jdbc");
        }

    }

    public static class TestcontainersLibraryAccessors extends SubDependencyFactory {

        public TestcontainersLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for hivemq (org.testcontainers:hivemq)
         * with versionRef 'testcontainers'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getHivemq() {
                return create("testcontainers.hivemq");
        }

            /**
             * Creates a dependency provider for junitJupiter (org.testcontainers:junit-jupiter)
         * with versionRef 'testcontainers'.
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJunitJupiter() {
                return create("testcontainers.junitJupiter");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final HivemqVersionAccessors vaccForHivemqVersionAccessors = new HivemqVersionAccessors(providers, config);
        private final JetbrainsVersionAccessors vaccForJetbrainsVersionAccessors = new JetbrainsVersionAccessors(providers, config);
        private final JunitVersionAccessors vaccForJunitVersionAccessors = new JunitVersionAccessors(providers, config);
        private final MssqlVersionAccessors vaccForMssqlVersionAccessors = new MssqlVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: jackson (2.17.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJackson() { return getVersion("jackson"); }

            /**
             * Returns the version associated to this alias: log4j (2.23.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLog4j() { return getVersion("log4j"); }

            /**
             * Returns the version associated to this alias: logback (1.4.7)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLogback() { return getVersion("logback"); }

            /**
             * Returns the version associated to this alias: mockito (4.11.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMockito() { return getVersion("mockito"); }

            /**
             * Returns the version associated to this alias: testcontainers (1.19.7)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTestcontainers() { return getVersion("testcontainers"); }

        /**
         * Returns the group of versions at versions.hivemq
         */
        public HivemqVersionAccessors getHivemq() {
            return vaccForHivemqVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.jetbrains
         */
        public JetbrainsVersionAccessors getJetbrains() {
            return vaccForJetbrainsVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.junit
         */
        public JunitVersionAccessors getJunit() {
            return vaccForJunitVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.mssql
         */
        public MssqlVersionAccessors getMssql() {
            return vaccForMssqlVersionAccessors;
        }

    }

    public static class HivemqVersionAccessors extends VersionFactory  {

        public HivemqVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: hivemq.mqttClient (1.3.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getMqttClient() { return getVersion("hivemq.mqttClient"); }

    }

    public static class JetbrainsVersionAccessors extends VersionFactory  {

        public JetbrainsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: jetbrains.annotations (24.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getAnnotations() { return getVersion("jetbrains.annotations"); }

    }

    public static class JunitVersionAccessors extends VersionFactory  {

        public JunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: junit.jupiter (5.9.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJupiter() { return getVersion("junit.jupiter"); }

    }

    public static class MssqlVersionAccessors extends VersionFactory  {

        public MssqlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: mssql.jdbc (12.6.1.jre11)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJdbc() { return getVersion("mssql.jdbc"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final HivemqPluginAccessors paccForHivemqPluginAccessors = new HivemqPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for defaults to the plugin id 'io.github.sgtsilvio.gradle.defaults'
             * with version '0.2.0'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getDefaults() { return createPlugin("defaults"); }

            /**
             * Creates a plugin provider for license to the plugin id 'com.github.hierynomus.license'
             * with version '0.16.1'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getLicense() { return createPlugin("license"); }

            /**
             * Creates a plugin provider for lombok to the plugin id 'io.freefair.lombok'
             * with version '8.6'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getLombok() { return createPlugin("lombok"); }

        /**
         * Returns the group of plugins at plugins.hivemq
         */
        public HivemqPluginAccessors getHivemq() {
            return paccForHivemqPluginAccessors;
        }

    }

    public static class HivemqPluginAccessors extends PluginFactory {

        public HivemqPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for hivemq.extension to the plugin id 'com.hivemq.extension'
             * with version '3.0.0'.
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getExtension() { return createPlugin("hivemq.extension"); }

    }

}
