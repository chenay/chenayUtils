package com.chenay.platform.apk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Y.Chen5
 */
public class CyDownloadDialogFragment extends DialogFragment {


    private CyProgressCircleLayout view;
    private DownloadInfoEntity infoEntity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = new CyProgressCircleLayout(inflater.getContext());
        return view;
//        return inflater.inflate(R.layout.fragment_download);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            infoEntity = (DownloadInfoEntity) getArguments().getSerializable("data");
            view.setProgress((int) infoEntity.getProgress());
        }
    }

    public void update() {
        if (infoEntity!=null) {
            view.setProgress((int) infoEntity.getProgress());
        }
    }
}
