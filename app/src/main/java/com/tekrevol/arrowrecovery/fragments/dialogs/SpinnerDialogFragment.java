package com.tekrevol.arrowrecovery.fragments.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tekrevol.arrowrecovery.R;
import com.tekrevol.arrowrecovery.adapters.SpinnerDialogAdapter;
import com.tekrevol.arrowrecovery.callbacks.OnSpinnerItemClickListener;
import com.tekrevol.arrowrecovery.callbacks.OnSpinnerOKPressedListener;
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.KeyboardHelper;
import com.tekrevol.arrowrecovery.models.SpinnerModel;
import com.tekrevol.arrowrecovery.widget.recyclerview_layout.CustomLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import info.hoang8f.widget.FButton;

/**
 * Created by khanhamza on 21-Feb-17.
 */

public class SpinnerDialogFragment extends DialogFragment {

    String title;
    Unbinder unbinder;
    SpinnerDialogAdapter adapter;

    RecyclerView recyclerView;
    TextView txtTitle;
    FButton txtOK;


    private ArrayList<SpinnerModel> arrData;

    private OnSpinnerItemClickListener onItemClickListener;
    private OnSpinnerOKPressedListener onSpinnerOKPressedListener;
    private int scrollToPosition;

    public SpinnerDialogFragment() {
    }

    public static SpinnerDialogFragment newInstance(String title, ArrayList<SpinnerModel> arrData,
                                                    OnSpinnerItemClickListener onItemClickListener, OnSpinnerOKPressedListener onSpinnerOKPressedListener, int scrollToPosition) {
        SpinnerDialogFragment frag = new SpinnerDialogFragment();

        Bundle args = new Bundle();
        frag.title = title;
        frag.arrData = arrData;
        frag.onItemClickListener = onItemClickListener;
        frag.scrollToPosition = scrollToPosition;
        frag.onSpinnerOKPressedListener = onSpinnerOKPressedListener;
        frag.setArguments(args);

        return frag;
    }


    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spinner_popup, container);
        unbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.recyclerView);
        txtOK = view.findViewById(R.id.txtOK);
        txtTitle = view.findViewById(R.id.txtTitle);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KeyboardHelper.hideSoftKeyboard(getContext(), view);
        setListeners();
        adapter = new SpinnerDialogAdapter(getActivity(), arrData, onItemClickListener);
        adapter.notifyDataSetChanged();
        txtTitle.setText(title);

        bindView();
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private void setListeners() {

        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSpinnerOKPressedListener != null) {
                    onSpinnerOKPressedListener.onItemSelect(null);
                }
                dismiss();
            }
        });
/*
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
//                if (edtSearchBar.getStringTrimmed().length() == 0) {
////                    imgClose.setVisibility(View.GONE);
//                } else {
////                    imgClose.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/
    }

    private void bindView() {
        RecyclerView.LayoutManager mLayoutManager = new CustomLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(mLayoutManager);
        ((DefaultItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        int resId = R.anim.layout_animation_fall_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(adapter);
        scrollToPosition(scrollToPosition);

    }

    public void scrollToPosition(int scrollToPosition) {
        if (scrollToPosition > -1) {
            recyclerView.scrollToPosition(scrollToPosition);
        } else {
            recyclerView.scrollToPosition(0);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

