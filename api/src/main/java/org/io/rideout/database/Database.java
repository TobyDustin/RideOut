package org.io.rideout.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.xml.crypto.Data;
import java.io.File;
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

    public static void main(String[] args) throws IOException {
        Properties prop = new Properties();

        String filename = "dbconfig.properties";
        InputStream input = Database.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            System.out.println("Sorry, unable to find " + filename);
            return;
        }

        prop.load(input);

        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String connString = prop.getProperty("connectionString")
                .replaceFirst("USERNAME", Matcher.quoteReplacement(username))
                .replaceFirst("PASSWORD", Matcher.quoteReplacement(password));

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Conn String: " + connString);


        input.close();
    }

    private Database() {
        String connectionString = getConnectionString();
        if (connectionString == null) return;

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString(getConnectionString()))
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

    public MongoDatabase getDatabase() {
        return database;
    }
}
