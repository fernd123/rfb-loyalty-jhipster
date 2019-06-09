package com.rfb.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.rfb.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.rfb.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.rfb.domain.User.class.getName());
            createCache(cm, com.rfb.domain.Authority.class.getName());
            createCache(cm, com.rfb.domain.User.class.getName() + ".authorities");
            createCache(cm, com.rfb.domain.PersistentToken.class.getName());
            createCache(cm, com.rfb.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, com.rfb.domain.Customer.class.getName());
            createCache(cm, com.rfb.domain.Customer.class.getName() + ".customerMeasures");
            createCache(cm, com.rfb.domain.Customer.class.getName() + ".customerTrainings");
            createCache(cm, com.rfb.domain.Customer.class.getName() + ".customerDiets");
            createCache(cm, com.rfb.domain.Customer.class.getName() + ".customerDates");
            createCache(cm, com.rfb.domain.Measure.class.getName());
            createCache(cm, com.rfb.domain.Training.class.getName());
            createCache(cm, com.rfb.domain.Training.class.getName() + ".trainingDays");
            createCache(cm, com.rfb.domain.TrainingDay.class.getName());
            createCache(cm, com.rfb.domain.TrainingDay.class.getName() + ".trainingExercises");
            createCache(cm, com.rfb.domain.TrainingExercise.class.getName());
            createCache(cm, com.rfb.domain.Exercise.class.getName());
            createCache(cm, com.rfb.domain.Exercise.class.getName() + ".exerciseTrainings");
            createCache(cm, com.rfb.domain.Diet.class.getName());
            createCache(cm, com.rfb.domain.Diet.class.getName() + ".dietFoods");
            createCache(cm, com.rfb.domain.DietFood.class.getName());
            createCache(cm, com.rfb.domain.CustomerDate.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
