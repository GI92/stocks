package com.stocks.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {
    private final ApplicationProperties.Cache cache = new ApplicationProperties.Cache();

    public Cache getCache() {
        return this.cache;
    }

    public static class Cache {
        private long maxEntries;
        private long timeToLiveInSeconds;

        public Cache() {
            this.maxEntries = 100L;
            this.timeToLiveInSeconds = 3600L;
        }

        public long getMaxEntries() {
            return maxEntries;
        }

        public void setMaxEntries(long maxEntries) {
            this.maxEntries = maxEntries;
        }

        public long getTimeToLiveInSeconds() {
            return timeToLiveInSeconds;
        }

        public void setTimeToLiveInSeconds(long timeToLiveInSeconds) {
            this.timeToLiveInSeconds = timeToLiveInSeconds;
        }
    }
}
