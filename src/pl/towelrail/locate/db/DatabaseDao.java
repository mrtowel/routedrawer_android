package pl.towelrail.locate.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Database access object.
 *
 * @param <T>  Database object class.
 * @param <ID> Id class of database object.
 */
public class DatabaseDao<T, ID> extends BaseDaoImpl<T, ID> {
    public DatabaseDao(ConnectionSource connectionSource, Class<T> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }
}
