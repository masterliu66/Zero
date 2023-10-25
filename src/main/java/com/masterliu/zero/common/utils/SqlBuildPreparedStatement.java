package com.masterliu.zero.common.utils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;

public class SqlBuildPreparedStatement  implements PreparedStatement {

    private final String sql;

    private int index;

    private final StringBuilder sqlBuilder;

    public SqlBuildPreparedStatement(String sql) {
        this.sql = sql;
        this.sqlBuilder = new StringBuilder();
    }

    public void nextToken() {
        while (index < sql.length()) {
            char c = sql.charAt(index++);
            if (c == '?') {
                break;
            }
            sqlBuilder.append(c);
        }
    }

    @Override
    public ResultSet executeQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        nextToken();
        sqlBuilder.append("null");
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        nextToken();
        sqlBuilder.append(x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        nextToken();
        sqlBuilder.append("'").append(x).append("'");
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        nextToken();
        sqlBuilder.append("'").append(new String(x)).append("'");
    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        nextToken();
        sqlBuilder.append("'").append(x.toLocalDate()).append("'");
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        nextToken();
        sqlBuilder.append("'").append(x.toLocalTime()).append("'");
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        nextToken();
        LocalDateTime localDateTime = x.toLocalDateTime();
        sqlBuilder.append("'")
                .append(localDateTime.toLocalDate())
                .append(" ")
                .append(localDateTime.toLocalTime())
                .append("'");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        nextToken();
        sqlBuilder.append("'").append(x).append("'");
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        nextToken();
        sqlBuilder.append("'").append(x).append("'");
    }

    @Override
    public boolean execute() {
        System.out.println(sqlBuilder);
        return true;
    }

    @Override
    public void addBatch() {
        sqlBuilder.append(sql, index, sql.length());
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRef(int parameterIndex, Ref x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Clob x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setArray(int parameterIndex, Array x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSetMetaData getMetaData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        nextToken();
        sqlBuilder.append("'").append(x.toLocalDate()).append("'");
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        nextToken();
        sqlBuilder.append("'").append(x.toLocalTime()).append("'");
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        nextToken();
        LocalDateTime localDateTime = x.toLocalDateTime();
        sqlBuilder.append("'")
                .append(localDateTime.toLocalDate())
                .append(" ")
                .append(localDateTime.toLocalTime())
                .append("'");
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {
        nextToken();
        sqlBuilder.append("null");
    }

    @Override
    public void setURL(int parameterIndex, URL x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParameterMetaData getParameterMetaData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNString(int parameterIndex, String value) {
        nextToken();
        sqlBuilder.append("'").append(value).append("'");
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {
        nextToken();
        sqlBuilder.append("'").append(x).append("'");
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet executeQuery(String sql) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String sql) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxFieldSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxFieldSize(int max) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaxRows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMaxRows(int max) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEscapeProcessing(boolean enable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getQueryTimeout() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQueryTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLWarning getWarnings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearWarnings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCursorName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String sql) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getUpdateCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getMoreResults() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFetchDirection(int direction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFetchDirection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFetchSize(int rows) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFetchSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getResultSetConcurrency() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getResultSetType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addBatch(String sql) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] executeBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Connection getConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getMoreResults(int current) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getGeneratedKeys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean execute(String sql, String[] columnNames) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getResultSetHoldability() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isClosed() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPoolable(boolean poolable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPoolable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeOnCompletion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCloseOnCompletion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        throw new UnsupportedOperationException();
    }
}
