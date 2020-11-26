package com.note11.easy_calling.screen.popup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.AccModel;
import com.note11.easy_calling.data.ShortCache;
import com.note11.easy_calling.data.ShortModel;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.data.UriCache;
import com.note11.easy_calling.databinding.ActivityPopupDownBinding;
import com.note11.easy_calling.util.AccAdapter;

public class PopupDownActivity extends AppCompatActivity {

    private ActivityPopupDownBinding binding;
    private ObservableArrayList<AccModel> it = new ObservableArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_down);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_popup_down);

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2);

        binding.recyclerFast.setLayoutManager(gridLayoutManager);

        AccAdapter a = new AccAdapter();
        a.setOnItemClickListener((view, item) -> startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.getTm().getPhone()))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
        a.setOnItemLongClickListener((view, item) -> true);
        binding.recyclerFast.setAdapter(a);
        it = getItem();
        binding.setItem(it);
    }

    private ObservableArrayList<AccModel> getItem() {
        ObservableArrayList<AccModel> datas = new ObservableArrayList<>();

        ShortModel s = ShortCache.getShort(this);

        if (s != null) {
            binding.txtFastNon.setVisibility(View.INVISIBLE);
            if (!s.getN1()[0].isEmpty())
                datas.add(getAM(s.getN1()));
            if (!s.getN2()[0].isEmpty())
                datas.add(getAM(s.getN2()));
            if (!s.getN3()[0].isEmpty())
                datas.add(getAM(s.getN3()));
            if (!s.getN4()[0].isEmpty())
                datas.add(getAM(s.getN4()));
            if (!s.getN5()[0].isEmpty())
                datas.add(getAM(s.getN5()));
            if (!s.getN6()[0].isEmpty())
                datas.add(getAM(s.getN6()));
            if (!s.getN7()[0].isEmpty())
                datas.add(getAM(s.getN7()));
            if (!s.getN8()[0].isEmpty())
                datas.add(getAM(s.getN8()));
            if (!s.getN9()[0].isEmpty())
                datas.add(getAM(s.getN9()));
            if (!s.getNs()[0].isEmpty())
                datas.add(getAM(s.getNs()));
            if (!s.getN0()[0].isEmpty())
                datas.add(getAM(s.getN0()));
            if (!s.getNh()[0].isEmpty())
                datas.add(getAM(s.getNh()));
        }

        return datas;
    }

    @Override
    public void onResume() {
        super.onResume();
        it = getItem();
        binding.setItem(it);
    }

    private AccModel getAM(String[] g) {
        String p = getH(g[1]);

        TelModel t = new TelModel(null, g[0], p);

        AccModel a = new AccModel();
        a.setTm(t);

        if (UriCache.getUri(this) != null && UriCache.getUri(this).getMap().get(p) != null)
            a.setIu(UriCache.getUri(this).getMap().get(p));
        else
            a.setIu(null);

        return a;
    }

    private String getH(String number) {
        if (number.length() > 11 || number.length() <= 5) return number;
        if (number.length() == 8) number = "010" + number;

        try {
            if (number.startsWith("02")) {
                number = number.substring(0, 2) + "-" + number.substring(2, 2 + (number.length() - 2) / 2) + "-" + number.substring(2 + (number.length() - 2) / 2, number.length());
            } else if (number.startsWith("031") || number.startsWith("032") || number.startsWith("097")) {
                number = number.substring(0, 3) + "-" + number.substring(3, 3 + (number.length() - 3) / 2) + "-" + number.substring(3 + (number.length() - 3) / 2, number.length());
            } else {
                number = number.substring(0, number.length() - 8) + "-" + number.substring(number.length() - 8, number.length() - 4) + "-" + number.substring(number.length() - 4, number.length());
            }
        } catch (Exception e) {
            Log.d("ASDF", "ERROR Phone[SetShortCut]: " + number);
        }

        return number;
    }
}