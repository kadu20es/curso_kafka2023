package br.com.alura.ecommerce.database;

import java.io.IOException;
import java.sql.*;

public class LocalDatabase {

    private final Connection connection;

    public LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:target/"+ name + ".db";
        this.connection = DriverManager.getConnection(url);
        /**/
    }

    // bem genérico / de acordo com a ferramenta de database, evite injection
    public void createIfNotExists(String sql){
        try {
            this.connection.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Atualiza dados de usuário
     * @param statement
     * @param params
     * @throws SQLException
     */
    public Boolean update(String statement, String ... params) throws SQLException {
        return prepare(statement, params).execute();
    }

    /**
     * Insere um novo usuário
     * @param statement
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet query(String statement, String ... params) throws SQLException {
        return prepare(statement, params).executeQuery();
    }

    private PreparedStatement prepare(String statement, String[] params) throws SQLException {
        var preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < params.length; i++){
            preparedStatement.setString(i+1, params[i]);
        }
        return preparedStatement;
    }

    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
