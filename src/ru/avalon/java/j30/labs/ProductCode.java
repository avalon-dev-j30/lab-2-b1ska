package ru.avalon.java.j30.labs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Класс описывает представление о коде товара и отражает соответствующую
 * таблицу базы данных Sample (таблица PRODUCT_CODE).
 *
 * @author Daniel Alpatov <danial.alpatov@gmail.com>
 */
public class ProductCode {

    private String code; //код товара
    private char discountCode; //код скидки
    private String description; //описание

    public ProductCode(String code, char discountCode, String description) {
        this.code = code;
        this.discountCode = discountCode;
        this.description = description;
    }

    private ProductCode(ResultSet set) throws SQLException {
        /*
         * TODO #05 реализуйте конструктор класса ProductCode
         */
        code = set.getString("prod_code");
        discountCode = set.getString("discount_code").charAt(0);
        description = set.getString("description");
    }

    public String getCode() {  //Возвращает код товара
        return code;
    }

    public void setCode(String code) {   // Устанавливает код товара
        this.code = code;
    }

    public char getDiscountCode() {  //Возвращает код скидки
        return discountCode;
    }

    public void setDiscountCode(char discountCode) {  //Устанавливает код скидки
        this.discountCode = discountCode;
    }

    public String getDescription() {  //Возвращает описание
        return description;
    }

    public void setDescription(String description) {   //Устанавливает описание
        this.description = description;
    }

    @Override
    public int hashCode() {  //переопределяет hashCode
        /*
         * TODO #06 Реализуйте метод hashCode
         */
        return code.hashCode() + description.hashCode();
    }

    /**
     * Сравнивает некоторый произвольный объект с текущим объектом типа
     * {@link ProductCode}
     *
     * @param obj Объект, скоторым сравнивается текущий объект.
     * @return true, если объект obj тождественен текущему объекту. В обратном
     * случае - false.
     */
    @Override
    public boolean equals(Object obj) {
        /*
         * TODO #07 Реализуйте метод equals
         */
        if (!(obj instanceof ProductCode)) {
            return false;
        } else if (this == obj) {
            return true;
        } else {
            ProductCode productCode = (ProductCode) obj;
            return code.equals(productCode.code);
        }
    }

    /**
     * Возвращает строковое представление кода товара.
     *
     * @return Объект типа {@link String}
     */
    @Override
    public String toString() {
        /*
         * TODO #08 Реализуйте метод toString
         */
        return code;
    }

    //создаем нову таблицу
    public void createTable(Connection connection) throws SQLException {

        String createTable = "CREATE TABLE product_code(prod_code VARCHAR(255) NOT NULL,"
                + " discount_code VARCHAR(20) NOT NULL,"
                + " description VARCHAR(255) NOT NULL)";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(createTable);

    }

    /**
     * Возвращает запрос на выбор всех записей из таблицы PRODUCT_CODE базы
     * данных Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getSelectQuery(Connection connection) throws SQLException {
        /*
         * TODO #09 Реализуйте метод getSelectQuery
         */

        String selectQuery = "SELECT * FROM product_code";
        return connection.prepareStatement(selectQuery);

    }

    /**
     * Возвращает запрос на добавление записи в таблицу PRODUCT_CODE базы данных
     * Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getInsertQuery(Connection connection) throws SQLException {
        /*
         * TODO #10 Реализуйте метод getInsertQuery
         */
        String query = "INSERT INTO product_code(prod_code,discount_code,description) VALUES(?,?,?)";
        return connection.prepareStatement(query);
    }

    /**
     * Возвращает запрос на обновление значений записи в таблице PRODUCT_CODE
     * базы данных Sample
     *
     * @param connection действительное соединение с базой данных
     * @return Запрос в виде объекта класса {@link PreparedStatement}
     */
    public static PreparedStatement getUpdateQuery(Connection connection) throws SQLException {
        /*
         * TODO #11 Реализуйте метод getUpdateQuery
         */
        String query = "UPDATE product_code set discount_code = ?,description = ? where prod_code = ?";
        return connection.prepareStatement(query);
    }

    /**
     * Преобразует {@link ResultSet} в коллекцию объектов типа
     * {@link ProductCode}
     *
     * @param set {@link ResultSet}, полученный в результате запроса,
     * содержащего все поля таблицы PRODUCT_CODE базы данных Sample
     * @return Коллекция объектов типа {@link ProductCode}
     * @throws SQLException
     */
    public static Collection<ProductCode> convert(ResultSet set) throws SQLException {
        /*
         * TODO #12 Реализуйте метод convert
         */
        Collection<ProductCode> productCollection = new ArrayList<>();
        while (set.next()) {
            productCollection.add(new ProductCode(set));
        }
        return productCollection;
    }

    /**
     * Сохраняет текущий объект в базе данных.
     * <p>
     * Если запись ещё не существует, то выполняется запрос типа INSERT.
     * <p>
     * Если запись уже существует в базе данных, то выполняется запрос типа
     * UPDATE.
     *
     * @param connection действительное соединение с базой данных
     */
    public void save(Connection connection) throws SQLException {
        /*
         * TODO #13 Реализуйте метод convert
         */

        Collection<ProductCode> products = all(connection);
        if (products.contains(this)) {
            PreparedStatement updateStatement = getUpdateQuery(connection);
            updateStatement.setString(1, String.valueOf(discountCode));
            updateStatement.setString(2, description);
            updateStatement.setString(3, code);
            updateStatement.executeUpdate();
        } else {
            PreparedStatement insertStatement = getInsertQuery(connection);
            insertStatement.setString(1, code);
            insertStatement.setString(2, String.valueOf(discountCode));
            insertStatement.setString(3, description);
            insertStatement.execute();
        }
    }

    /**
     * Возвращает все записи таблицы PRODUCT_CODE в виде коллекции объектов типа
     * {@link ProductCode}
     *
     * @param connection действительное соединение с базой данных
     * @return коллекция объектов типа {@link ProductCode}
     * @throws SQLException
     */
    public static Collection<ProductCode> all(Connection connection) throws SQLException {
        try (PreparedStatement statement = getSelectQuery(connection)) {
            try (ResultSet result = statement.executeQuery()) {
                return convert(result);
            }
        }
    }
}
