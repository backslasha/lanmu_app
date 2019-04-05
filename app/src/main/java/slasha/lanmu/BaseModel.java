package slasha.lanmu;

/**
 * Created by yhb on 18-1-17.
 */

public interface BaseModel {

    interface Callback<T> {
        void callback(T t);
    }
}