package pl.towelrail.locate.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class DatabaseModel<T, ID> {
    private Class<T> mClass;
    private Class<ID> mIdClass;
    private Context mContext;
    private DatabaseHelper<T, ID> helper;

    public DatabaseModel(Class<T> mClass, Class<ID> mIdClass, Context mContext) {
        this.mClass = mClass;
        this.mIdClass = mIdClass;
        this.mContext = mContext;
    }

    private DatabaseModel() {
    }


    public Dao<T, ID> getDao() throws SQLException {
        helper = new DatabaseHelper<T, ID>(mContext);
        return helper.getDataDao(mClass, mIdClass);
    }

    public DatabaseHelper<T, ID> getHelper() {
        if (helper == null) {
            helper = new DatabaseHelper<T, ID>(mContext);
        }
        return helper;
    }
}
