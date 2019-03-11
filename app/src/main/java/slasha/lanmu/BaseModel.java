package slasha.lanmu;

import java.util.List;

/**
 * Created by yhb on 18-1-17.
 */

public interface BaseModel<T> {
    List<T> offer(String keyword);
}