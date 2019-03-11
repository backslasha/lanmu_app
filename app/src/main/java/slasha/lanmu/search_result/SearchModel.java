package slasha.lanmu.search_result;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.BookPosts;

public class SearchModel implements SearchContract.SearchModel<BookPost> {

    @Override
    public List<BookPost> offer(String keyword) {
        return offer_fake();
    }

    private List<BookPost> offer_fake() {
        String json = readStringFrom("sample/data_fake.json");
        BookPosts bookPosts = new Gson().fromJson(json, BookPosts.class);
        return bookPosts.getBookPosts();
    }


    private String readStringFrom(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    LanmuApplication.instance().getAssets().open(fileName)
            );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufReader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
