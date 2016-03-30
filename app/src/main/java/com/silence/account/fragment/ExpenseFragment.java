package com.silence.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.silence.account.R;
import com.silence.account.activity.AddCategoryAty;
import com.silence.account.application.AppApplication;
import com.silence.account.adapter.GridExpCatAdapter;
import com.silence.account.dao.ExpenseCatDao;
import com.silence.account.dao.ExpenseDao;
import com.silence.account.pojo.Expense;
import com.silence.account.pojo.ExpenseCat;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.L;
import com.silence.account.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Silence on 2016/3/9 0009.
 */
public class ExpenseFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.icon_expense_cat)
    ImageView mIconExpenseCat;
    @Bind(R.id.label_expense_cat)
    TextView mLabelExpenseCat;
    @Bind(R.id.et_expense)
    EditText mEtExpense;
    @Bind(R.id.label_expense_time)
    TextView mLabelExpenseTime;
    @Bind(R.id.et_expense_note)
    EditText mEtExpenseNote;
    @Bind(R.id.ll_expense_cat)
    LinearLayout mLlExpenseCat;
    private ExpenseCatDao mExpenseCatDao;
    private static final int REQUEST_ADD_CATEGORY = 0x201;
    private static final int REQUEST_UPDATE_CATEGORY = 0x202;
    private boolean mIsUpdateExpense;
    private boolean mIsUpdateCat;
    private Expense mExpense;
    private Date mDate;
    private GridExpCatAdapter mCatAdapter;
    private PopupWindow mPopupWindow;
    private onTimePickListener mOnTimePickListener;
    private Context mContext;

    public static ExpenseFragment getInstance(Expense expense) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.RECORD, expense);
        ExpenseFragment expenseFragment = new ExpenseFragment();
        expenseFragment.setArguments(bundle);
        return expenseFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTimePickListener) {
            mOnTimePickListener = (onTimePickListener) context;
        }
        mContext = getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        ButterKnife.bind(this, view);
        mExpenseCatDao = new ExpenseCatDao(mContext);
        mCatAdapter = new GridExpCatAdapter(mContext, getCategory());
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsUpdateExpense = true;
            mExpense = arguments.getParcelable(Constant.RECORD);
            mEtExpenseNote.setText(mExpense.getNote());
            mEtExpense.setText(String.valueOf(mExpense.getAmount()));
            mIconExpenseCat.setImageResource(mExpense.getCategory().getImageId());
            mLabelExpenseCat.setText(mExpense.getCategory().getName());
            mDate = mExpense.getDate();
        } else {
            mIsUpdateExpense = false;
            mDate = new Date();
            mExpense = new Expense();
            mExpense.setUser( AppApplication.getUser());
            mExpense.setDate(mDate);
            mExpense.setCategory((ExpenseCat) mCatAdapter.getItem(0));
        }
        mLabelExpenseTime.setText(DateUtils.date2Str(mDate));
        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.pop_category, null);
        GridView gridExpenseCat = (GridView) linearLayout.findViewById(R.id.grid_category);
        gridExpenseCat.setOnItemClickListener(this);
        gridExpenseCat.setAdapter(mCatAdapter);
        gridExpenseCat.setOnItemClickListener(this);
        gridExpenseCat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCatAdapter.getCloseVisibility() == View.VISIBLE) {
                    mCatAdapter.setCloseVisibility(View.GONE);
                }
                return false;
            }
        });
        gridExpenseCat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseCat expenseCat = (ExpenseCat) mCatAdapter.getItem(position);
                Intent intent = new Intent(mContext, AddCategoryAty.class);
                intent.putExtra(Constant.UPDATE_CAT, expenseCat);
                startActivityForResult(intent, REQUEST_UPDATE_CATEGORY);
                return true;
            }
        });
        mPopupWindow = new PopupWindow(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsUpdateCat && mCatAdapter != null) {
            mCatAdapter.setData(getCategory());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private List<ExpenseCat> getCategory() {
        List<ExpenseCat> cats = mExpenseCatDao.getExpenseCat(( AppApplication.getUser().getId()));
        cats.add(new ExpenseCat(R.mipmap.jiahao_bai, "添加",  AppApplication.getUser()));
        cats.add(new ExpenseCat(R.mipmap.jianhao_bai, "删除",  AppApplication.getUser()));
        return cats;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mIsUpdateCat = requestCode == REQUEST_ADD_CATEGORY || requestCode == REQUEST_UPDATE_CATEGORY;
        } else {
            mIsUpdateCat = false;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpenseCat expenseCat = (ExpenseCat) parent.getItemAtPosition(position);
        if (expenseCat.getImageId() == R.mipmap.jiahao_bai) {
            Intent intent = new Intent(mContext, AddCategoryAty.class);
            intent.putExtra(Constant.TYPE_CATEGORY, Constant.TYPE_EXPENSE);
            startActivityForResult(intent, REQUEST_ADD_CATEGORY);
        } else if (expenseCat.getImageId() == R.mipmap.jianhao_bai) {
            mCatAdapter.setCloseVisibility(View.VISIBLE);
        } else {
            mExpense.setCategory(expenseCat);
            mIconExpenseCat.setImageResource(expenseCat.getImageId());
            mLabelExpenseCat.setText(expenseCat.getName());
            mPopupWindow.dismiss();
        }
    }

    @OnClick({R.id.ll_expense_cat, R.id.label_expense_time, R.id.icon_expense_speak, R.id.btn_expense_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_expense_cat: {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(mLlExpenseCat);
                }
            }
            break;
            case R.id.label_expense_time: {
                if (mOnTimePickListener != null) {
                    mOnTimePickListener.DisplayDialog(mDate);
                }
            }
            break;
            case R.id.icon_expense_speak: {
                RecognizerDialog mDialog = new RecognizerDialog(mContext, null);
                mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                final StringBuilder stringBuilder = new StringBuilder();
                mDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        try {
                            JSONObject jsonObject = new JSONObject(recognizerResult.getResultString());
                            JSONArray jsonArray = jsonObject.getJSONArray("ws");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONArray cw = jsonArray.getJSONObject(i).getJSONArray("cw");
                                JSONObject w = cw.getJSONObject(0);
                                stringBuilder.append(w.get("w"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mEtExpenseNote.setText(stringBuilder);
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        L.i("error");
                    }
                });
                mDialog.show();
            }
            break;
            case R.id.btn_expense_save:
                saveExpense();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnTimePickListener = null;
    }

    public void setDate(Date date) {
        mExpense.setDate(date);
        mLabelExpenseTime.setText(DateUtils.date2Str(date));
    }

    public void saveExpense() {
        String trim = mEtExpense.getText().toString().trim();
        float amount = Float.parseFloat(TextUtils.isEmpty(trim) ? "0" : trim);
        String note = mEtExpenseNote.getText().toString().trim();
        mExpense.setAmount(amount);
        mExpense.setNote(note);
        ExpenseDao expenseDao = new ExpenseDao(mContext);
        if (mIsUpdateExpense) {
            if (expenseDao.updateExpense(mExpense)) {
                T.showShort(mContext, "保存成功");
                getActivity().setResult(Constant.RESULT_UPDATE_FINANCE);
                getActivity().finish();
            } else {
                T.showShort(mContext, "保存失败");
            }
        } else {
            if (expenseDao.addExpense(mExpense)) {
                T.showShort(mContext, "保存成功");
                getActivity().setResult(Constant.RESULT_INSERT_FINANCE);
                getActivity().finish();
            } else {
                T.showShort(mContext, "保存失败");
            }
        }
    }

    public interface onTimePickListener {
        void DisplayDialog(Date date);
    }
}
