package pl.towelrail.locate.db;

import android.util.Log;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTools {
    private DatabaseTools() {
    }

    public static <T, ID> int persist(T obj, DatabaseModel<T, ID> model) {
        try {
            Dao<T, ID> dao = model.getDao();
            dao.create(obj);
        } catch (SQLException e) {
            return 1;
        }
        return 0;
    }

    public static <T, ID> List<T> fetchAll(DatabaseModel<T, ID> model) {
        try {
            return model.getDao().queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T, ID> T getById(DatabaseModel<T, ID> model, ID id) {
        try {
            return model.getDao().queryForId(id);
        } catch (SQLException e) {
            Log.d(DatabaseTools.class.getName(), "getById() failed");
            return null;
        }
    }
}
