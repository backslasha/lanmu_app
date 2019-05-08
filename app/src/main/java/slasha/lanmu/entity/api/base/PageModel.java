package slasha.lanmu.entity.api.base;

import com.google.gson.annotations.Expose;

import java.util.List;

import androidx.annotation.NonNull;

public class PageModel<T> {

    @Expose
    private List<T> result;

    @Expose
    private int page;

    @Expose
    private boolean end;

    @Expose
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    @NonNull
    public String toString() {
        return "{" + "\"result\":" +
                result +
                ",\"page\":" +
                page +
                '}';
    }
}
