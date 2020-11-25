package com.note11.easy_calling.screen.main;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.note11.easy_calling.R;
import com.note11.easy_calling.data.NumberCache;
import com.note11.easy_calling.data.ShortCache;
import com.note11.easy_calling.data.ShortModel;
import com.note11.easy_calling.data.TelModel;
import com.note11.easy_calling.databinding.FragmentDialBinding;
import com.note11.easy_calling.screen.popup.SetShortForDial;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DialFragment extends Fragment {

    public static DialFragment newInstance() {
        return new DialFragment();
    }

    private Context mContext;
    private FragmentDialBinding binding;
    private boolean isSeoul = false;

    private ArrayList<TelModel> it = new ArrayList<>();

    private final static int TEL_ADD_REQUEST = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dial, container, false);

        if (NumberCache.getNumber(mContext) != null)
            it = getItems();
        settingPad();

        return binding.getRoot();
    }

    private void settingPad() {
        binding.setPhone("");
        binding.btnDialNum0.setOnClickListener(v -> dial("0"));
        binding.btnDialNum1.setOnClickListener(v -> dial("1"));
        binding.btnDialNum2.setOnClickListener(v -> dial("2"));
        binding.btnDialNum3.setOnClickListener(v -> dial("3"));
        binding.btnDialNum4.setOnClickListener(v -> dial("4"));
        binding.btnDialNum5.setOnClickListener(v -> dial("5"));
        binding.btnDialNum6.setOnClickListener(v -> dial("6"));
        binding.btnDialNum7.setOnClickListener(v -> dial("7"));
        binding.btnDialNum8.setOnClickListener(v -> dial("8"));
        binding.btnDialNum9.setOnClickListener(v -> dial("9"));
        binding.btnDialNumStar.setOnClickListener(v -> dial("*"));
        binding.btnDialNumHash.setOnClickListener(v -> dial("#"));
        binding.btnDialNum0.setOnLongClickListener(v -> lDial("0"));
        binding.btnDialNum1.setOnLongClickListener(v ->lDial("1"));
        binding.btnDialNum2.setOnLongClickListener(v -> lDial("2"));
        binding.btnDialNum3.setOnLongClickListener(v -> lDial("3"));
        binding.btnDialNum4.setOnLongClickListener(v -> lDial("4"));
        binding.btnDialNum5.setOnLongClickListener(v -> lDial("5"));
        binding.btnDialNum6.setOnLongClickListener(v -> lDial("6"));
        binding.btnDialNum7.setOnLongClickListener(v -> lDial("7"));
        binding.btnDialNum8.setOnLongClickListener(v -> lDial("8"));
        binding.btnDialNum9.setOnLongClickListener(v -> lDial("9"));
        binding.btnDialNumStar.setOnLongClickListener(v -> lDial("*"));
        binding.btnDialNumHash.setOnLongClickListener(v -> lDial("#"));
        binding.btnDialDel.setOnClickListener(v -> delDial());
        binding.btnDialCall.setOnClickListener(v -> calling());
        binding.btnDialDel.setOnLongClickListener(v -> delAll());
        binding.constraintDialLike.setOnClickListener(v -> {
            TelModel t = binding.getNowLike();
            if (t != null) {
                if (t.getPhone().equals("연락처에 추가") && t.getName().equals("+"))
                    goToTelAddActivity(binding.getPhone());
                else
                    binding.setPhone(t.getPhone());
            }
        });

        if(ShortCache.getShort(mContext)!=null)
            binding.setS(ShortCache.getShort(mContext));
        else
            binding.setS(new ShortModel(
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""},
                new String[]{"",""}
        ));
    }

    private void dial(String d) {
        binding.setPhone(binding.getPhone() + d);
        if (binding.getPhone().length() == 2) {
            if (binding.getPhone().startsWith("02")) {
                binding.setPhone(binding.getPhone() + "-");
                isSeoul = true;
            }
        }
        if (!isSeoul) {
            if (binding.getPhone().length() == 3 || binding.getPhone().length() == 8)
                binding.setPhone(binding.getPhone() + "-");
        } else {
            if (binding.getPhone().length() == 6)
                binding.setPhone(binding.getPhone() + "-");
        }

        TelModel telModel = searchBestLike(it, binding.getPhone());
        if(telModel != null)
            binding.setNowLike(telModel);
        else if(binding.getPhone().length() >= 3){
            binding.setNowLike(new TelModel("", "+", "연락처에 추가"));
        }else {
            binding.setNowLike(new TelModel("", "", ""));
        }

    }

    private void goToTelAddActivity(String phone) {
        Intent i = new Intent(mContext.getApplicationContext(), TelAddActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("getPhone", phone);
        startActivityForResult(i, TEL_ADD_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TEL_ADD_REQUEST)
            it = getItems();
    }

    private void delDial() {
        if (binding.getPhone().length() == 1)
            isSeoul = false;
        if (!binding.getPhone().isEmpty()) {
            if (binding.getPhone().endsWith("-"))
                binding.setPhone(binding.getPhone().substring(0, binding.getPhone().length() - 1));
            binding.setPhone(binding.getPhone().substring(0, binding.getPhone().length() - 1));
        }

        TelModel telModel = searchBestLike(it, binding.getPhone());
        if(telModel != null)
            binding.setNowLike(telModel);
        else if(binding.getPhone().length() >= 3){
            binding.setNowLike(new TelModel("", "+", "연락처에 추가"));
        }else {
            binding.setNowLike(new TelModel("", "", ""));
        }
    }

    private void calling() {
        if (!binding.getPhone().isEmpty())
            startActivity(
                    new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + binding.getPhone()))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
    }

    private boolean delAll() {
        binding.setPhone("");
        binding.setNowLike(new TelModel("", "", ""));
        isSeoul = false;
        return true;
    }

    private boolean lDial(String s){
        if(ShortCache.getShort(mContext)==null){
            //단축번호 설정 창 띄우기
            AlertDialog.Builder oD = new AlertDialog.Builder(mContext, R.style.pDialogStyle);
            oD.setTitle("등록된 단축번호 없음")
                    .setMessage(s+"에 단축번호를 지정하시겠어요?")
                    .setNegativeButton("아니오", (dialog, which) -> {
                        return;
                    })
                    .setPositiveButton("예", (dialog, which) ->
                            startActivity(new Intent(mContext, SetShortForDial.class).putExtra("key",s))).show();
        }else{
            ShortModel r = ShortCache.getShort(mContext);
            if(shortIsEmpty(s, r)){
                //캐시는 있지만 해당 숫자에 없는 경우.
                AlertDialog.Builder oD = new AlertDialog.Builder(mContext, R.style.pDialogStyle);
                oD.setTitle("등록된 단축번호 없음")
                        .setMessage(s+"에 단축번호를 지정하시겠어요?")
                        .setNegativeButton("아니오", (dialog, which) -> {
                            return;
                        })
                        .setPositiveButton("예", (dialog, which) ->
                                startActivity(new Intent(mContext, SetShortForDial.class).putExtra("key",s))).show();
            } else{
                //있는 경우 : 번호로 걸기
                getPhoneUsingShort(s, r);
            }
        }
        return true;
    }

    public ArrayList<TelModel> getItems() {
        ArrayList<TelModel> datas = new ArrayList<>();

        ContentResolver r = mContext.getContentResolver();

        Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] tableC = {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor c = r.query(phoneUri, tableC, null, null, null);

        if (c != null) {
            //find index
            int idIndex = c.getColumnIndex(tableC[0]);
            int nameIndex = c.getColumnIndex(tableC[1]);
            int numberIndex = c.getColumnIndex(tableC[2]);

            while (c.moveToNext()) {
                //using index, get Phone Data
                TelModel phoneBook = new TelModel();
                phoneBook.setId(c.getString(idIndex));
                phoneBook.setName(c.getString(nameIndex));
                phoneBook.setPhone(inputHypun(c.getString(numberIndex).replaceAll("-", "")));

                datas.add(phoneBook);
            }
            c.close();//Data close
        }
        return datas;
    }

    public String inputHypun(String number) {
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
            Log.d("ASDF", "ERROR Phone[CallLogFragment]: " + number);
        }

        return number;
    }

    public TelModel searchBestLike(ArrayList<TelModel> datas, String phone) {
        phone = phone.replaceAll("-", "");
        if (phone.length() == 0) return null;
        int sublen = 9999;
        TelModel result = null;
        for (TelModel telModel : datas) {
            if (telModel.getPhone().replaceAll("-", "").contains(phone) && sublen > telModel.getPhone().length() - phone.length()) {
                sublen = telModel.getPhone().length() - phone.length();
                result = telModel;
            }
        }

        return result;
    }

    private boolean shortIsEmpty(String numPad, ShortModel r){
        String get;
        switch (numPad){
            case "1":
                get = r.getN1()[0];
                break;
            case "2":
                get = r.getN2()[0];
                break;
            case "3":
                get = r.getN3()[0];
                break;
            case "4":
                get = r.getN4()[0];
                break;
            case "5":
                get = r.getN5()[0];
                break;
            case "6":
                get = r.getN6()[0];
                break;
            case "7":
                get = r.getN7()[0];
                break;
            case "8":
                get = r.getN8()[0];
                break;
            case "9":
                get = r.getN9()[0];
                break;
            case "*":
                get = r.getNs()[0];
                break;
            case "0":
                get = r.getN0()[0];
                break;
            case "#":
                get = r.getNh()[0];
                break;
            default:
                get = "";
        }
        return get.isEmpty();
    }

    private void getPhoneUsingShort(String numPad, ShortModel r){
        String get;
        switch (numPad){
            case "1":
                get = r.getN1()[1];
                break;
            case "2":
                get = r.getN2()[1];
                break;
            case "3":
                get = r.getN3()[1];
                break;
            case "4":
                get = r.getN4()[1];
                break;
            case "5":
                get = r.getN5()[1];
                break;
            case "6":
                get = r.getN6()[1];
                break;
            case "7":
                get = r.getN7()[1];
                break;
            case "8":
                get = r.getN8()[1];
                break;
            case "9":
                get = r.getN9()[1];
                break;
            case "*":
                get = r.getNs()[1];
                break;
            case "0":
                get = r.getN0()[1];
                break;
            case "#":
                get = r.getNh()[1];
                break;
            default:
                get = "";
        }
        if (!get.isEmpty())
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + get))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ShortCache.getShort(mContext)!=null)
            binding.setS(ShortCache.getShort(mContext));
    }
}