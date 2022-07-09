package sk.chatty.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@PropertySource("classpath:database.properties")
public class MessageDAO {
    private static Connection connection;

    public MessageDAO(@Value("${PG_URL}") String PG_URL,
                      @Value("${PG_USERNAME}") String PG_USERNAME,
                      @Value("${PG_PASSWORD}") String PG_PASSWORD) throws ClassNotFoundException, SQLException {
        if (connection != null)
            return;

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(PG_URL, PG_USERNAME, PG_PASSWORD);
    }

    

}
