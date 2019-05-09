package slasha.lanmu.persistence.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.LogUtil;

/**
 * 全局数据库工具类
 */
public class LanmuDB implements UserInfo.UserInfoChangeListener {

    private static final String TAG = "lanmu.database";

    private static final LanmuDB sINSTANCE;

    static {
        sINSTANCE = new LanmuDB();
    }

    private LanmuDBOpenHelper mDBOpenHelper;
    private ContractCache mContractCache;

    private LanmuDB() {
        switch2DB("lanmu_" + UserInfo.id(), LanmuApplication.instance());
        UserInfo.registerLoginStatusListener(this);
    }

    private void switch2DB(String dbName, Context context) {
        mDBOpenHelper = new LanmuDBOpenHelper(context.getApplicationContext(), dbName, 1);
        mContractCache = new ContractCache(mDBOpenHelper);
        LogUtil.i(TAG, "switch to database: " + mDBOpenHelper.getDatabaseName());
    }

    /**
     * 保存/更新本地数据库好友列表
     */
    public static void saveFriends(UserCard... friends) {
        if (friends == null || friends.length <= 0) {
            return;
        }
        SQLiteDatabase writableDB = sINSTANCE.mDBOpenHelper.getWritableDatabase();
        writableDB.beginTransaction();
        for (UserCard friend : friends) {
            ContentValues contentValues = cv(friend);
            long id = writableDB.insertWithOnConflict(
                    LanmuDBOpenHelper.TB_FRIEND,
                    null,
                    cv(friend),
                    SQLiteDatabase.CONFLICT_IGNORE
            );
            if (id == -1) { // insert fail, then try to update
                contentValues.remove("id");
                writableDB.update(
                        LanmuDBOpenHelper.TB_FRIEND,
                        contentValues,
                        "id=?",
                        new String[]{String.valueOf(friend.getId())}
                );
            }
        }
        writableDB.setTransactionSuccessful();
        writableDB.endTransaction();
        writableDB.close();
        LogUtil.i(TAG, "save or update " + friends.length + " friends to db.");
    }

    /**
     * 保存/更新本地数据库私信消息
     */
    public static void saveMessages(MessageCard... messages) {
        if (messages == null || messages.length <= 0) {
            return;
        }
        SQLiteDatabase writableDB = sINSTANCE.mDBOpenHelper.getWritableDatabase();
        writableDB.beginTransaction();
        for (MessageCard message : messages) {
            ContentValues contentValues = cv(message);
            long id = writableDB.insertWithOnConflict(
                    LanmuDBOpenHelper.TB_MESSAGE,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_IGNORE
            );
            if (id == -1) { // insert fail, then try to update
                contentValues.remove("id");
                writableDB.update(
                        LanmuDBOpenHelper.TB_MESSAGE,
                        contentValues,
                        "id=?",
                        new String[]{String.valueOf(message.getId())}
                );
            }
        }
        writableDB.setTransactionSuccessful();
        writableDB.endTransaction();
        writableDB.close();
        LogUtil.i(TAG, "save or update " + messages.length + " messages to db.");
    }


    public static List<UserCard> queryFriends() {
        SQLiteDatabase db = sINSTANCE.mDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(LanmuDBOpenHelper.TB_FRIEND, null, null, null, null, null, null);
        List<UserCard> cards = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            UserCard userCard = new UserCard();
            userCard.setFriend(true);
            userCard.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            userCard.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userCard.setId(cursor.getLong(cursor.getColumnIndex("id")));
            userCard.setName(cursor.getString(cursor.getColumnIndex("name")));
            userCard.setAvatarUrl(cursor.getString(cursor.getColumnIndex("avatarUrl")));
            userCard.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
            cards.add(userCard);
        }
        LogUtil.i(TAG, "get " + cards.size() + " friends from db.");
        cursor.close();
        return cards;
    }

    public static List<MessageCard> queryConversations() {
        SQLiteDatabase db = sINSTANCE.mDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from " + LanmuDBOpenHelper.TB_MESSAGE +
                        " where time in (select max(time) from " + LanmuDBOpenHelper.TB_MESSAGE
                        + " group by talk2Id) order by time desc",
                null);
        List<MessageCard> cards = readMsgCards(cursor);
        LogUtil.i(TAG, "get " + cards.size() + " conversations from db.");
        cursor.close();
        return cards;
    }

    public static List<MessageCard> queryMessages(long talk2Id) {
        SQLiteDatabase db = sINSTANCE.mDBOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from " + LanmuDBOpenHelper.TB_MESSAGE + " where talk2Id=? order by time ",
                new String[]{String.valueOf(talk2Id)}
        );
        List<MessageCard> cards = readMsgCards(cursor);
        LogUtil.i(TAG, "get " + cards.size() + " messages from db.");
        cursor.close();
        return cards;
    }

    private static List<MessageCard> readMsgCards(Cursor cursor) {
        ArrayList<MessageCard> cards = new ArrayList<>();
        cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int received = cursor.getInt(cursor.getColumnIndex("received"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            long fromId = cursor.getLong(cursor.getColumnIndex("fromId"));
            long toId = cursor.getLong(cursor.getColumnIndex("toId"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String content = cursor.getString(cursor.getColumnIndex("content"));

            MessageCard card = new MessageCard();
            card.setContent(content);
            card.setId(id);
            card.setReceived(received);
            card.setType(type);
            card.setTime(FormatUtils.dbFormat(time));

            if (UserInfo.id() == fromId) { // send by me
                card.setFrom(UserInfo.self());
                card.setTo(sINSTANCE.mContractCache.get(toId));
            } else {
                card.setTo(UserInfo.self());
                card.setFrom(sINSTANCE.mContractCache.get(fromId));
            }
            cards.add(card);
        }

        return cards;
    }

    private static ContentValues cv(MessageCard message) {
        ContentValues values = new ContentValues();
        values.put("id", message.getId());
        values.put("type", message.getType());
        values.put("time", FormatUtils.dbFormat(message.getTime()));
        values.put("content", message.getContent());
        final long fromId = message.getFrom().getId();
        final long toId = message.getTo().getId();
        values.put("fromId", fromId);
        values.put("toId", toId);
        values.put("talk2Id", fromId == UserInfo.id() ? toId : fromId); // 辅助查询回话
        values.put("received", message.getReceived());
        return values;
    }

    private static ContentValues cv(UserCard userCard) {
        ContentValues values = new ContentValues();
        values.put("id", userCard.getId());
        values.put("name", userCard.getName());
        values.put("avatarUrl", userCard.getAvatarUrl());
        values.put("introduction", userCard.getIntroduction());
        values.put("gender", userCard.getGender());
        return values;
    }

    static UserCard queryFriend(long userId, SQLiteDatabase db) {
        Cursor cursor = db.query(LanmuDBOpenHelper.TB_FRIEND,
                null,
                "id=?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );
        UserCard userCard = null;
        //noinspection StatementWithEmptyBody
        if (cursor.moveToFirst()) {
            userCard = new UserCard();
            userCard.setFriend(true);
            userCard.setGender(cursor.getString(cursor.getColumnIndex("gender")));
            userCard.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            userCard.setId(cursor.getLong(cursor.getColumnIndex("id")));
            userCard.setName(cursor.getString(cursor.getColumnIndex("name")));
            userCard.setAvatarUrl(cursor.getString(cursor.getColumnIndex("avatarUrl")));
            userCard.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
//            LogUtil.i(TAG, "get 1 friend from db.");
        } else {
//            LogUtil.e(TAG, "get no friend whose is is " + userId + " from db!");
        }
        cursor.close();
        return userCard;
    }

    private static ContentValues cv(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(key, value);
        return values;
    }


    public static void markMsgReceived(long userId) {
        SQLiteDatabase writableDB = sINSTANCE.mDBOpenHelper.getWritableDatabase();
        writableDB.beginTransaction();
        writableDB.update(
                LanmuDBOpenHelper.TB_MESSAGE,
                cv("received", "1"),
                "fromId=?",
                new String[]{String.valueOf(userId)}
        );
        writableDB.setTransactionSuccessful();
        writableDB.endTransaction();
        writableDB.close();
        LogUtil.i(TAG, "update " + userId + "'s messages to read already status to db.");
    }

    @Override
    public void onUserInfoLoaded(UserCard user) {

    }

    @Override
    public void onUserInfoCleared() {

    }

    @Override
    public void onUserInfoUpdated(UserCard user) {
        switch2DB("lanmu_" + UserInfo.id(), LanmuApplication.instance());
    }
}
