package slasha.lanmu.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * @author Bright Van
 * @version 1.0
 * @describe 对话框封装类
 * @date 2015年9月8日
 */
public class DialogUtils {

    /**
     * 创建消息对话框
     *
     * @param context  上下文 必填
     * @param title    标题 必填
     * @param message  显示内容 必填
     * @param btnName  按钮名称 必填
     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createMessageDialog(Context context, String title,
                                             String message, String btnName,
                                             @NonNull OnClickListener listener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置对话框消息
        builder.setMessage(message);
        // 设置按钮
        builder.setPositiveButton(btnName, listener);
        // 创建一个消息对话框
        builder.setCancelable(false);

        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建警示（确认、取消）对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param message             显示内容 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createConfirmDialog(Context context, String title,
                                             String message, String positiveBtnName, String negativeBtnName,
                                             OnClickListener positiveBtnListener,
                                             OnClickListener negativeBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置对话框消息
        builder.setMessage(message);
        // 设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        // 设置取消按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建警示（确认、取消）对话框,没有标题
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createConfirmDialog(Context context, String title,
                                             String positiveBtnName, String negativeBtnName,
                                             OnClickListener positiveBtnListener,
                                             OnClickListener negativeBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 设置对话框标题
        builder.setTitle(title);
        // 设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        // 设置取消按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建单选对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         选择项 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param itemClickListener   监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createSingleChoiceDialog(Context context,
                                                  String title, String[] itemsString, String positiveBtnName,
                                                  String negativeBtnName, OnClickListener positiveBtnListener,
                                                  OnClickListener negativeBtnListener,
                                                  OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置单选选项, 参数0: 默认第一个单选按钮被选中
        builder.setSingleChoiceItems(itemsString, 0, itemClickListener);
        // 设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    public static Dialog createSingleChoiceDialog(Context context,
                                                  String[] items, OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setItems(items, itemClickListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建复选对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         选择项 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param itemClickListener   监听器，需实现android.content.DialogInterface.
     *                            OnMultiChoiceClickListener;接口 必填
     * @return
     */
    public static Dialog createMultiChoiceDialog(Context context, String title,
                                                 String[] itemsString, String positiveBtnName,
                                                 String negativeBtnName, OnClickListener positiveBtnListener,
                                                 OnClickListener negativeBtnListener,
                                                 OnMultiChoiceClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置选项
        builder.setMultiChoiceItems(itemsString, null, itemClickListener);
        // 设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建列表对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param itemsString         列表项 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @return
     */
    public static Dialog createListDialog(Context context, String title,
                                          String[] itemsString, String negativeBtnName,
                                          OnClickListener negativeBtnListener,
                                          OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        // 设置列表选项
        builder.setItems(itemsString, itemClickListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    /**
     * 创建自定义（含确认、取消）对话框
     *
     * @param context             上下文 必填
     * @param title               标题 必填
     * @param positiveBtnName     确定按钮名称 必填
     * @param negativeBtnName     取消按钮名称 必填
     * @param positiveBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param negativeBtnListener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
     * @param view                对话框中自定义视图 必填
     * @return
     */
    public static Dialog createDesignDialog(Context context, String title,
                                            String positiveBtnName, String negativeBtnName,
                                            OnClickListener positiveBtnListener,
                                            OnClickListener negativeBtnListener, View view) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 设置对话框标题
        builder.setTitle(title);
        builder.setView(view);
        // 设置确定按钮
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        // 设置确定按钮
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        // 创建一个消息对话框
        dialog = builder.create();

        return dialog;
    }

    public static ProgressDialog createProgressDialog(Context context, String title, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.setTitle(title);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}