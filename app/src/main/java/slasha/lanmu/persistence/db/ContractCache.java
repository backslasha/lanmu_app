package slasha.lanmu.persistence.db;

import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.common.LogUtil;

/**
 * 根据 id 查找联系人，实现三级缓存
 * 内存和数据库中没有找到指定的联系人时，从服务器同步整个联系人列表
 */
public class ContractCache {

    private static final String TAG = "lanmu.database";
    private LinkedHashMap<Long, UserCard> mUserCache = new LinkedHashMap<>();
    private LanmuDBOpenHelper mDBOpenHelper;

    ContractCache(LanmuDBOpenHelper DBOpenHelper) {
        mDBOpenHelper = DBOpenHelper;
    }

    public UserCard get(long userId) {
        SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();
        UserCard user = mUserCache.get(userId); // lever 1
        if (user == null) {
            user = LanmuDB.queryFriend(userId, db); // lever 2
        }
        if (user == null) { // level 3
            List<UserCard> cards =
                    pullFriendsFromServer(UserInfo.id()); // pull all friends from server
            if (cards != null) {
                LanmuDB.saveFriends(cards.toArray(new UserCard[0]));
                for (UserCard card : cards) {
                    if (card.getId() == userId) {
                        user = card;
                        break;
                    }
                }
            }
        }
        if (user == null) {
            LogUtil.e(TAG, "can not find any user whose id is " + userId);
        } else {
            mUserCache.put(userId, user);
        }
        return user;
    }

    private List<UserCard> pullFriendsFromServer(long userId) {
        Call<RspModelWrapper<List<UserCard>>> rspModelWrapperCall
                = Network.remote().pullFriends(userId);
        try {
            Response<RspModelWrapper<List<UserCard>>> response
                    = rspModelWrapperCall.execute();
            if (response.isSuccessful() && response.body() != null) {
                List<UserCard> result = response.body().getResult();
                if (result != null) {
                    return result;
                }
            }
        } catch (IOException e) {
            LogUtil.e(TAG, String.valueOf(e.getCause()));
        }
        return null;
    }

}
