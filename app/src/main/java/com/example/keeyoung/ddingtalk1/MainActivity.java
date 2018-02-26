package com.example.keeyoung.ddingtalk1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mCallItem = null;
    public Contact acontact;
    public Contact arrayacontact;
    public Contact acontacts;
    public ArrayList<Contact> contactlist;
    public ArrayList<Contact> contactlists;
    public ContactsAdapter adapter;
    ContactsAdapter adapters;
    static int number;
    Intent intent;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //new loadContactsData().execute("");
        mCallItem = (GridView) findViewById(R.id.grid_main);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //사용자의 os버전이 마시멜로 이상인지를 체크함
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {

            //사용자의 단말기의 권한 중 "전화걸기" 권한이 있는지 체크한다.
            int permissionResult = checkSelfPermission(Manifest.permission.READ_CONTACTS);

            //READ 권한이 없을때.
            if (permissionResult == PackageManager.PERMISSION_DENIED) {

                //사용자가 READ권한을 한번이라도 거부한 적이 있는지를 조사
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"연락처 조회\" 권한이 필요합니다. 계속하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                    }
                                }
                            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();

                    //최초로 권한을 요청할때.
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1000);
                }

                //READ권한이 있을때
            } else {

                new loadContactsData().execute("");
            }

            //사용자의 os버전이 마시멜로 이하일때
        } else {

            new loadContactsData().execute("");

        }
    }

    //사용자가 권한을 허용햇는지 거부햇는지 체크
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {

            //요청한 권한을 사용자가 "허용"했다면 인텐트를 띄움.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    new loadContactsData().execute("");
                }
            } else {
                Toast.makeText(MainActivity.this, "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    new loadContactsData().execute("");
                }
            } else {
                Toast.makeText(MainActivity.this, "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener =
            new GridView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                    Contact phonenumber = (Contact) parent
                            .getItemAtPosition(position);

                    if (phonenumber == null) {
                        return;
                    }

                    uri = Uri.parse("tel:" + phonenumber
                            .getPhonenum().replaceAll("-", ""));
                    intent = new Intent(Intent.ACTION_CALL, uri);
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                        int permissionResult = checkSelfPermission(Manifest.permission.CALL_PHONE);
                        if (permissionResult == PackageManager.PERMISSION_DENIED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setTitle("권한이 필요합니다.")
                                        .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속하시겠습니까?")
                                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                                }
                                            }
                                        }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }).create().show();
                            } else {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1001);
                            }
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivity(intent);
                    }
                }
            };


    public void getContactList() {

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = getContentResolver().query(uri, projection, null,
                null, sortOrder);

        number = contactCursor.getCount();
        contactlist = new ArrayList<Contact>();
        acontacts = new Contact(number);
        acontacts.setNumber(number);
        acontacts.setcount();
        if (contactCursor.moveToFirst()) {
            do {
                String phonenumber = contactCursor.getString(1).replaceAll("-",
                        "");
                if (phonenumber.length() == 10) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 6) + "-"
                            + phonenumber.substring(6);
                } else if (phonenumber.length() > 8) {
                    phonenumber = phonenumber.substring(0, 3) + "-"
                            + phonenumber.substring(3, 7) + "-"
                            + phonenumber.substring(7);
                } else if(phonenumber.length() < 9){
                    if(phonenumber.length() == 8) {
                        phonenumber = phonenumber.substring(0, 4) + "-"
                                + phonenumber.substring(4);
                    }
                }
                acontact = new Contact();
                acontact.setPhotoid(contactCursor.getLong(0));
                acontact.setPhonenum(phonenumber);
                acontact.setName(contactCursor.getString(2));
                contactlist.add(acontact);
            } while (contactCursor.moveToNext());
        }
    }

    private class ContactsAdapter extends ArrayAdapter<Contact> {

        private int resId;
        private ArrayList<Contact> contactlist;
        private LayoutInflater Inflater;
        private Context context;

        //objects안에는 번호랑 아이디랑 이름 다들어가 있음.
        public ContactsAdapter(Context context, int textViewResourceId,
                               List<Contact> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            resId = textViewResourceId;
            contactlist = (ArrayList<Contact>) objects;
            Inflater = (LayoutInflater) ((Activity) context)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder holder;
            if (v == null) {
                v = Inflater.inflate(resId, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) v.findViewById(R.id.appname);
                holder.iv_photoid = (ImageView) v.findViewById(R.id.appicon);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            Contact acontact = contactlist.get(position);

            if (acontact != null) {
                holder.tv_name.setText(acontact.getName());
                Bitmap bm = openPhoto(acontact.getPhotoid());
                if (bm != null) {
                    holder.iv_photoid.setImageBitmap(bm);
                } else {
                    holder.iv_photoid.setImageDrawable(getResources()
                            .getDrawable(R.drawable.sample5));
                }
            }
            return v;
        }

        private Bitmap openPhoto(long contactId) {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    contactId);
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(context.getContentResolver(),
                            contactUri);

            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }

            return null;
        }

        private class ViewHolder {
            ImageView iv_photoid;
            TextView tv_name;
        }
    }

    private class loadContactsData extends AsyncTask<String, Void, Void> {

        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        //UI 스레드상에서 실행되며 doInBackground 메소드 전에 호출됨.
        //초기화등 작업을 수행함.
        protected void onPreExecute() {
            Dialog.setMessage("주소록 정보를 로딩 중입니다.");
            Dialog.show();
        }

        //메인쓰레드가 아닌 백그라운드에서 이 메소드만 실행됨.
        //이 메소드에 포함된 코드는 백그라운드 스레드 상에서 처리되며 이곳에서 UI 처리를 하면 안됩니다.
        //AsyncTask의 execute 메소드를 호출시 전달한 인자를 파라메터로 받게 됩니다.
        protected Void doInBackground(String... urls) {
            getContactList();
            return null;
        }

        //doInBackground 메소드 종료 후  호출됩니다.
        //doInBackground 메소드에서 리턴한 값을 onPostExecute 메소드에서 파라메터로 전달받습니다.
        //백그라운드 스레드에서 작업 종료 후, 결과를 메인 스레드에서 통보해 줄 수 있고(onPostExecute)
        protected void onPostExecute(Void unused) {
            try {
                Dialog.dismiss();

                //setChoiceMode은 선택할 수 있는 항목을 정할 수 있다.
                //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                adapters = new ContactsAdapter(MainActivity.this,
                        R.layout.callview, contactlist);
                mCallItem.setAdapter(adapters);
                mCallItem.setFastScrollEnabled(true);
                mCallItem.setOnItemClickListener(gridviewOnItemClickListener);

                EditText findText = (EditText)findViewById(R.id.input);
                findText.addTextChangedListener(new TextWatcher(){
                    public void afterTextChanged(Editable s) {
                        findName(s.toString());
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                if (Error != null) {
                    Toast.makeText(MainActivity.this, Error, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(parent, "Source: " + Content, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    final static char[] chosungWord   = {
            0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145,
            0x3146, 0x3147, 0x3148, 0x3149, 0x314a,
            0x314b, 0x314c, 0x314d, 0x314e
    };

    public String hangulToOnlyChosung(String s)
    {
        int chosungNum, tempNum;
        String resultString = "";

        for (int i = 0; i < s.length(); i++)
        {
            char ch = s.charAt(i);
            if (ch != ' ')
            {
                if (ch >= 0xAC00 && ch <= 0xD7A3)
                {
                    tempNum = ch - 0xAC00;
                    chosungNum = tempNum / (21 * 28);

                    resultString += chosungWord[chosungNum];
                }
                else
                {
                    resultString += ch;
                }
            }
        }
        return resultString;
    }

    public void findName(String findString)
    {
        contactlists = contactlist;
        try {
            if (findString.length() > 0)
            {
                String onlyChosungString = hangulToOnlyChosung(findString);
                contactlist.clear();
                for (int i = 0; i < number; i++)
                {
                   if(acontact.arrayname[i] != null){
                        String onlyChosungNameString = hangulToOnlyChosung(acontact.arrayname[i]);
                        if (onlyChosungNameString.matches(onlyChosungString+".*"))
                        {
                            if(acontact.arrayname[i] != null) {
                                arrayacontact = new Contact();
                                arrayacontact.setarrayPhotoid(acontact.arrayphotoid[i]);
                                arrayacontact.setarrayPhonenum(acontact.arrayphonenum[i]);
                                arrayacontact.setarrayName(acontact.arrayname[i]);
                                contactlist.add(arrayacontact);
                            }
                        }
                    }
                }
                adapters.notifyDataSetChanged();
            }
            else
            {
                arrayacontact = new Contact();
                contactlist.clear();
                arrayacontact.setcount();
                for (int i = 0; i < number; i++)
                {
                    if(acontact.arrayname[i] != null){
                            if(acontact.arrayname[i] != null) {
                                arrayacontact = new Contact();
                                arrayacontact.setarrayPhotoid(acontact.arrayphotoid[i]);
                                arrayacontact.setarrayPhonenum(acontact.arrayphonenum[i]);
                                arrayacontact.setarrayName(acontact.arrayname[i]);
                                contactlist.add(arrayacontact);
                            }
                        }
                }
                adapters.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
