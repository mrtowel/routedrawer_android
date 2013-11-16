package pl.towelrail.locate.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper<T, ID> extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "towelrail_location.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<T, ID> mLocateDao = null;
    private Class<T> mClass;
    private Class<ID> mIdClass;

    public DatabaseHelper(Context context, String databaseName,
                          SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mLocateDao = null;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, mClass);
        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(), "create table failed");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, mClass, true);
        } catch (SQLException e) {
            Log.d(DatabaseHelper.class.getName(), "drop table failed");
        }
    }




    public Dao<T, ID> getDataDao(Class<T> clazz, Class<ID> idClass) throws SQLException {
        mClass = clazz;
        mIdClass = idClass;

        if (mLocateDao == null) {
            mLocateDao = BaseDaoImpl.createDao(getConnectionSource(), mClass);
        }

        return mLocateDao;
    }
}
