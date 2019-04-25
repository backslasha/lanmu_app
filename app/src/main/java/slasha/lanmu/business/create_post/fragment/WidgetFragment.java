package slasha.lanmu.business.create_post.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WidgetFragment extends Fragment {


    private static final String ARG_VIEW_CLASS = "arg_view_class";

    public static WidgetFragment newInstance(Class<View> clazz) {
        Bundle args = new Bundle();
        WidgetFragment fragment = new WidgetFragment();
        args.putSerializable(ARG_VIEW_CLASS, clazz);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            Class<View> viewClass =
                    (Class<View>) getArguments().getSerializable(ARG_VIEW_CLASS);
            try {
                if (viewClass != null) {
                    return viewClass.getConstructor(Context.class).newInstance(getActivity());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
