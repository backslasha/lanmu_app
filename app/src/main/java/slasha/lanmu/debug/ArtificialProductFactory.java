package slasha.lanmu.debug;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.response.BookPostFlow;
import slasha.lanmu.bean.response.Comments;
import slasha.lanmu.bean.response.LoginResult;
import slasha.lanmu.bean.Message;
import slasha.lanmu.bean.User;

import static slasha.lanmu.utils.AppUtils.readStringFromAsset;

public class ArtificialProductFactory {

    public static List<BookPost> bookPosts() {
        return new Gson().fromJson(
                readStringFromAsset("sample/search.json"),
                new TypeToken<List<BookPost>>() {
                }.getType()
        );
    }

    public static List<Message> messages() {
        return new Gson().
                fromJson(
                        readStringFromAsset("sample/messages.json"),
                        new TypeToken<List<Message>>() {
                        }.getType()
                );
    }

    public static User user() {
        return new Gson().
                fromJson(
                        readStringFromAsset("sample/user.json"), User.class
                );
    }

    public static LoginResult loginResult() {
        return new Gson().
                fromJson(
                        readStringFromAsset("sample/login.json"), LoginResult.class
                );
    }

    public static BookPostFlow offerPostFlow() {
        return new Gson().
                fromJson(
                        readStringFromAsset("sample/main.json"), BookPostFlow.class
                );
    }

    public static Comments offerComments() {
        return new Gson().
                fromJson(
                        readStringFromAsset("sample/comment.json"), Comments.class
                );
    }
}
