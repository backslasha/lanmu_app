package slasha.lanmu.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import slasha.lanmu.utils.common.LogUtil;

public class LanmuDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "lanmu.database";

    static final String TB_FRIEND = "tb_friend";
    static final String TB_MESSAGE = "tb_message";

    LanmuDBOpenHelper(@Nullable Context context,
                      @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String createSqlFriend = "create table " + TB_FRIEND + "(" +
                "id integer primary key, " +
                "name varchar(128), " +
                "avatarUrl varchar(256), " +
                "introduction varchar(1280), " +
                "gender varchar(8), " +
                "phone varchar(62))";

        final String createSqlMessage = "create table " + TB_MESSAGE + "(" +
                "id integer primary key, " +
                "name type(128), " +
                "time varchar(256), " +
                "content varchar(8), " +
                "type integer, " +
                "received integer, " +
                "fromId integer, " +
                "toId integer, " +
                "talk2Id integer, " +
                "constraint fk1_friend foreign key (fromId) references tb_friend(id)," +
                "constraint fk2_friend foreign key (toId) references tb_friend(id)" +
                ")";

        db.execSQL(createSqlFriend);
        db.execSQL(createSqlMessage);
        LogUtil.i(TAG, createSqlFriend);
        LogUtil.i(TAG, createSqlMessage);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
