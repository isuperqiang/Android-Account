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
import com.silence.account.adapter.GridInCatAdapter;
import com.silence.account.dao.IncomeCatDao;
import com.silence.account.dao.IncomeDao;
import com.silence.account.pojo.Income;
import com.silence.account.pojo.IncomeCat;
import com.silence.account.utils.Constant;
import com.silence.account.utils.DateUtils;
import com.silence.account.utils.L;
import com.silence.account.utils.T;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IncomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.icon_income_cat)
    ImageView mIconIncomeCat;
    @Bind(R.id.label_income_cat)
    TextView mLabelIncomeCat;
    @Bind(R.id.et_income)
    EditText mEtIncome;
    @Bind(R.id.label_income_time)
    TextView mEtIncomeTime;
    @Bind(R.id.et_income_note)
    EditText mEtIncomeNote;
    @Bind(R.id.ll_income_cat)
    LinearLayout mLlIncomeCat;
    private static final int REQUEST_ADD_CATEGORY = 0x101;
    private static final int REQUEST_UPDATE_CATEGORY = 0x102;
    private boolean mIsUpdateCat;
    private IncomeCatDao mIncomeCatDao;
    private Date mDate;
    private Income mIncome;
    private PopupWindow mPopupWindow;
    private onTimePickListener mOnTimePickListener;
    private boolean mIsUpdateIncome;
    private GridInCatAdapter mCatAdapter;
    private Context mContext;


    public static IncomeFragment getInstance(Income income) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.RECORD, income);
        IncomeFragment incomeFragment = new IncomeFragment();
        incomeFragment.setArguments(bundle);
        return incomeFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTimePickListener) {
            mOnTimePickListener = (onTimePickListener) context;
        }
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);
        ButterKnife.bind(this, view);
        mIncomeCatDao = new IncomeCatDao(mContext);
        mCatAdapter = new GridInCatAdapter(mContext, getCategory());
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsUpdateIncome = true;
            mIncome = arguments.getParcelable(Constant.RECORD);
            mEtIncome.setText(String.valueOf(mIncome.getAmount()));
            mLabelIncomeCat.setText(mIncome.getCategory().getName());
            mIconIncomeCat.setImageResource(mIncome.getCategory().getImageId());
            mEtIncomeNote.setText(mIncome.getNote());
            mDate = mIncome.getDate();
        } else {
            mIsUpdateIncome = false;
            mIncome = new Income();
            mDate = new Date();
            mIncome.setDate(mDate);
            mIncome.setUser(AppApplication.sUser);
            mIncome.setCategory((IncomeCat) mCatAdapter.getItem(0));
        }
        mEtIncomeTime.setText(DateUtils.date2Str(mDate));
        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.pop_category, null);
        GridView gridIncomeCat = (GridView) linearLayout.findViewById(R.id.grid_category);
        gridIncomeCat.setAdapter(mCatAdapter);
        gridIncomeCat.setOnItemClickListener(this);
        gridIncomeCat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCatAdapter.getCloseVisibility() == View.VISIBLE) {
                    mCatAdapter.setCloseVisibility(View.GONE);
                }
                return false;
            }
        });
        gridIncomeCat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                IncomeCat incomeCat = (IncomeCat) mCatAdapter.getItem(position);
                Intent intent = new Intent(mContext, AddCategoryAty.class);
                intent.putExtra(Constant.UPDATE_CAT, incomeCat);
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

    private List<IncomeCat> getCategory() {
        List<IncomeCat> cats = mIncomeCatDao.getIncomeCat(AppApplication.sUser.getId());
        cats.add(new IncomeCat(R.mipmap.jiahao_bai, "添加", AppApplication.sUser));
        cats.add(new IncomeCat(R.mipmap.jianhao_bai, "删除", AppApplication.sUser));
        return cats;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
        IncomeCat incomeCat = (IncomeCat) parent.getItemAtPosition(position);
        if (incomeCat.getImageId() == R.mipmap.jiahao_bai) {
            Intent intent = new Intent(mContext, AddCategoryAty.class);
            intent.putExtra(Constant.TYPE_CATEGORY, Constant.TYPE_INCOME);
            startActivityForResult(intent, REQUEST_ADD_CATEGORY);
        } else if (incomeCat.getImageId() == R.mipmap.jianhao_bai) {
            mCatAdapter.setCloseVisibility(View.VISIBLE);
        } else {
            mIncome.setCategory(incomeCat);
            mIconIncomeCat.setImageResource(incomeCat.getImageId());
            mLabelIncomeCat.setText(incomeCat.getName());
            mPopupWindow.dismiss();
        }
    }

    @OnClick({R.id.label_income_time, R.id.icon_income_speak, R.id.ll_income_cat, R.id.btn_income_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.label_income_time: {
                if (mOnTimePickListener != null) {
                    mOnTimePickListener.DisplayDialog(mDate);
                }
            }
            break;
            case R.id.ll_income_cat:
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(mLlIncomeCat);
                }
                break;
            case R.id.btn_income_save: {
                saveIncome();
            }
            break;
            case R.id.icon_income_speak: {
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
                        mEtIncomeNote.setText(stringBuilder);
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        L.i("error");
                    }
                });
                mDialog.show();
            }
            break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnTimePickListener = null;
    }

    private void saveIncome() {
        String trim = mEtIncome.getText().toString().trim();
        float amount = Float.parseFloat(TextUtils.isEmpty(trim) ? "0" : trim);
        String note = mEtIncomeNote.getText().toString().trim();
        mIncome.setAmount(amount);
        mIncome.setNote(note);
        IncomeDao incomeDao = new IncomeDao(mContext);
        if (!mIsUpdateIncome) {
            if (incomeDao.addIncome(mIncome)) {
                T.showShort(mContext, "保存成功");
                EventBus.getDefault().post("income_inserted");
                getActivity().finish();
            } else {
                T.showShort(mContext, "保存失败");
            }
        } else {
            if (incomeDao.updateIncome(mIncome)) {
                T.showShort(mContext, "修改成功");
                EventBus.getDefault().post("income_updated");
                getActivity().finish();
            } else {
                T.showShort(mContext, "修改失败");
            }
        }
    }

    public void setDate(Date date) {
        mIncome.setDate(date);
        mEtIncomeTime.setText(DateUtils.date2Str(date));
    }

    public interface onTimePickListener {
        void DisplayDialog(Date date);
    }
}
