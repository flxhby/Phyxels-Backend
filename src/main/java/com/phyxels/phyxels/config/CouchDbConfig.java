package com.phyxels.phyxels.config;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchDbConfig {

    @Value("${couchdb.url}")
    private String couchDbUrl;

    @Value("${couchdb.bucket}")
    private String dbName;

    @Value("${couchdb.username}")
    private String username;

    @Value("${couchdb.password}")
    private String password;

    @Bean
    public CouchDbClient couchDbClient() {
        CouchDbProperties properties = new CouchDbProperties()
                .setDbName(dbName)
                .setProtocol(getProtocolFromUrl(couchDbUrl))
                .setHost(getHostFromUrl(couchDbUrl))
                .setPort(getPortFromUrl(couchDbUrl))
                .setUsername(username)
                .setPassword(password);
        return new CouchDbClient(properties);
    }

    String getProtocolFromUrl(String url) {
        return url.split(":")[0];
    }

    String getHostFromUrl(String url) {
        String withoutProtocol = url.split("//")[1];
        return withoutProtocol.contains(":") ? withoutProtocol.split(":")[0] : withoutProtocol;
    }

    int getPortFromUrl(String url) {
        if (url.contains(":")) {
            String portPart = url.split(":")[2];
            return Integer.parseInt(portPart.split("/")[0]);
        }
        return 5984;
    }
}
