package com.drucare.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.annotations.common.util.StringHelper;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.HSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.drucare.persistence.entity.UserEntity;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages = {
"com.drucare.persistence.repository" }, repositoryImplementationPostfix = "Helper", transactionManagerRef = "transactionManager")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class HSQLDBConfig {

    /** Reference to logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(HSQLDBConfig.class);

    @Bean
    public DataSource dataSource() {
        // instantiate, configure and return production DataSource
        return  new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    /**
     * @formatter:off
    @Value("classpath:UserConnection.sql")
    private Resource schemaScript;

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
    @formatter:on
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.HSQL);
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform(HSQLDialect.class.getName());
        return adapter;
    }

    /**
     * it uses container managed transaction, so the name LocalContainer**. since it is spring bean need not worry about container or application managed
     * transactions.
     *
     * @return
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        // em.setMappingResources("META-INF/orm.xml"); // for auditing AuditingEntityListener
        em.setPackagesToScan(new String[] { UserEntity.class.getPackage().getName() });
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaProperties(additionalJpaProperties());
        em.setPersistenceUnitName("DERBY_PERSISTENCE_UNIT");
        em.afterPropertiesSet();
        LOGGER.info("Created entityManager with dataSource API {} ", em.getDataSource().getClass());
        return em.getObject();
    }

    Properties additionalJpaProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop"); // TODO: create-drop,create,none
        properties.setProperty("hibernate.dialect", HSQLDialect.class.getName());
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.ejb.naming_strategy", DatabaseNamingStrategy.class.getName());// fully qualified name a string
        return properties;
    }

    /**
     * It’s important to understand that @PersistenceUnit and @PersistenceContext aren’t Spring annotations;<br>
     * they’re provided by the JPA specification. In order for Spring to understand them and inject an EntityManagerFactory or EntityManager,<br>
     * Spring’s PersistenceAnnotationBeanPostProcessor must be configured. <br>
     * If you’re already using <context:annotation-config> or <context:component-scan>,<br>
     * then you’re good to go because those configuration elements automatically register a PersistenceAnnotationBeanPostProcessor bean.
     *
     * @return PersistenceAnnotationBeanPostProcessor
     */

    @Bean
    public PersistenceAnnotationBeanPostProcessor paPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    /**
     * Bean post-processor that automatically applies persistence exception translation to any bean marked with Spring's @Repository annotation, adding a
     * corresponding PersistenceExceptionTranslationAdvisor to the exposed proxy (either an existing AOP proxy or a newly generated proxy that implements all of
     * the target's interfaces).
     *
     * Translates native resource exceptions to Spring's DataAccessException hierarchy. Autodetects beans that implement the PersistenceExceptionTranslator
     * interface, which are subsequently asked to translate candidate exceptions.
     *
     * @return PersistenceExceptionTranslationPostProcessor
     */
    @Bean
    public BeanPostProcessor persistenceTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory());
        return transactionManager;
    }

    /**
     * The Class DatabaseNamingStrategy will load at deployment time to give the table names dynamically based on Entity names with prefix given.
     */
    public static class DatabaseNamingStrategy extends ImprovedNamingStrategy {

        /** The Constant PREFIX. */
        private static final String PREFIX = "TRBLOG_";

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 5118322121741445924L;

        /**
         * Transforms class names to table names by using the described naming conventions.
         *
         * @param className
         *            the class name
         * @return The constructed table name.
         */
        @Override
        public String classToTableName(final String className) {

            return DatabaseNamingStrategy
                    .addUnderscoredAndCovertToUppercase(DatabaseNamingStrategy.PREFIX + StringHelper.unqualify(className).replaceAll("Entity", ""));
        }

        @Override
        public String tableName(final String tableName) {
            return DatabaseNamingStrategy.addUnderscoredAndCovertToUppercase(DatabaseNamingStrategy.PREFIX + tableName);
        }

        /**
         * Adds the underscored and covert to upperCase.
         *
         * @param name
         *            the name
         * @return the string
         */
        static String addUnderscoredAndCovertToUppercase(final String name) {

            return ImprovedNamingStrategy.addUnderscores(name).toLowerCase();
        }
    }

}
