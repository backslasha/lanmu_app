package slasha.lanmu.debug;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.BookPosts;
import slasha.lanmu.bean.LoginResult;
import slasha.lanmu.bean.Message;
import slasha.lanmu.bean.User;

import static slasha.lanmu.utils.AppUtils.readStringFromAsset;

public class ArtificialProductFactory {

    public static List<BookPost> bookPosts() {
        String json = readStringFromAsset("sample/bookposts.json");
        BookPosts bookPosts = new Gson().fromJson(json, BookPosts.class);
        return bookPosts.getBookPosts();
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
}
