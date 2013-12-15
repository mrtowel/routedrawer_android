package pl.towelrail.locate.db;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.BaseDaoEnabled;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTools {
    private DatabaseTools() {
    }

    public static <T, ID> boolean persist(T obj, DatabaseModel<T, ID> model) {
        boolean success = true;

        try {
            Dao<T, ID> dao = model.getDao();
            dao.create(obj);
        } catch (SQLException e) {
            success = false;
        } finally {
            model.getHelper().close();
        }
        return success;
    }

    public static <T, ID> boolean createOrUpdate(T obj, DatabaseModel<T, ID> model) {
        boolean success = true;
        try {
            model.getDao().createOrUpdate(obj);
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public static <T, ID> List<T> fetchAll(DatabaseModel<T, ID> model) {
        List<T> queryResult = null;
        try {
            queryResult = model.getDao().queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            model.getHelper().close();
        }
        return queryResult;
    }

    public static <T, ID> T getById(DatabaseModel<T, ID> model, ID id) {
        T obj = null;
        try {
            obj = model.getDao().queryForId(id);
        } catch (SQLException e) {
            Log.d(DatabaseTools.class.getName(), "getById() failed");
        } finally {
            model.getHelper().close();
        }
        return obj;
    }

    public static <T, ID> boolean clearTable(DatabaseModel<T, ID> model) {
        boolean success = true;
        try {
            model.getDao().clearObjectCache();

        } catch (SQLException e) {
            success = false;
            e.printStackTrace();
        } finally {

        }
        return success;
    }

    public static <T extends BaseDaoEnabled<T, ?>, ID> boolean update(DatabaseModel<T, ID> model, T obj) {
        boolean success;
        try {
            success = model.getDao().update(obj) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static <T extends BaseDaoEnabled<T, ?>> boolean refresh(T obj) {
        boolean success;
        try {
            success = obj.refresh() == 1 && obj.update() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static <T extends BaseDaoEnabled> boolean remove(T obj) {
        boolean success = true;
        try {
            obj.delete();
        } catch (SQLException e) {
            success = false;
        }
        return success;
    }
}
