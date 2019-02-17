package org.io.rideout.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    private static Database ourInstance = new Database();
    private MongoDatabase database;

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString("mongodb+srv://matyas:fCFBluqaTuKoPGBC@rideout-9fffk.gcp.mongodb.net/rideout?retryWrites=true"))
                .build();

        MongoClient client = MongoClients.create(settings);
        this.database = client.getDatabase("rideout");
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
