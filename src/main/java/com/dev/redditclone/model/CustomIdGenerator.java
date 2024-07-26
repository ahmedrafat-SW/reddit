package com.dev.redditclone.model;

import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        JdbcConnectionAccess jdbcConnectionAccess = session.getJdbcConnectionAccess();
        final String sequenceName = "hibernate_seq";
        try {
            Connection connection = jdbcConnectionAccess.obtainConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT "+sequenceName+".NEXTVAL FROM DUAL";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id=resultSet.getInt(1)+101;
                String generatedId = "CustomID" + id;
                System.out.println("Generated Id: " + generatedId);
                return generatedId;
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return null;
    }
}
