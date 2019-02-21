package org.io.rideout.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    private static Database ourInstance = new Database();
    private MongoDatabase database;

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        String connectionString = getConnectionString();
        if (connectionString == null) return;

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString(connectionString))
                .build();

        MongoClient client = MongoClients.create(settings);
        this.database = client.getDatabase("rideout");
    }

    private String getConnectionString() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            String filename = "dbconfig.properties";
            input = Database.class.getClassLoader().getResourceAsStream(filename);

            if (input == null) {
                return null;
            }

            prop.load(input);

            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            return prop.getProperty("connectionString")
                    .replaceFirst("USERNAME", Matcher.quoteReplacement(username))
                    .replaceFirst("PASSWORD", Matcher.quoteReplacement(password));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    MongoDatabase getDatabase() {
        return database;
    }

    void setDatabase(MongoDatabase database) {
        this.database = database;
    }
}
