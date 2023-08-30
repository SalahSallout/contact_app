package ps.cst.mycontacts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private myAdapter ad;
    private ListView lv;
    private dbOperation dbOperations;
    private Cursor cursor;
private ArrayList<Contacts> mArrayListContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = findViewById(R.id.btn_add);
        androidx.appcompat.widget.SearchView search = findViewById(R.id.search);
        lv = findViewById(R.id.lv);


        dbOperations = new dbOperation(this);
        cursor = dbOperations.getCursor();
         mArrayListContacts = new ArrayList<Contacts>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          Contacts contacts = new Contacts();
            contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
            contacts.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contacts.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            contacts.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            mArrayListContacts.add(contacts);
        }

        ad = new myAdapter(this, cursor , mArrayListContacts);
        lv.setAdapter(ad);





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });



        search.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ad.getFilter().filter(newText);
                return false;
            }
        });


        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {


                return false;
            }
        });

        registerForContextMenu(lv);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Contacts newContact = (Contacts) data.getSerializableExtra("newContact");
            long l = dbOperations.insert(newContact);
            ad.notifyDataSetChanged();
            ad.changeCursor(dbOperations.getCursor());
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Contacts contacts = new Contacts();

        if (item.getItemId() == R.id.it_del) {
            contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
            contacts.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contacts.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            contacts.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));
            dbOperations.delete(contacts);
            cursor = dbOperations.getCursor();
            ad = new myAdapter(this, cursor , mArrayListContacts);
            lv.setAdapter(ad);

            return true;
        } else if (item.getItemId() == R.id.it_edit) {
            contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
            contacts.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            contacts.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            contacts.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));


            Intent intentEdit = new Intent(MainActivity.this , EditContact.class);
            intentEdit.putExtra("name" ,contacts.getName() );
            intentEdit.putExtra("email" ,contacts.getEmail() );
            intentEdit.putExtra("number" ,contacts.getNumber() );
            intentEdit.putExtra("_id" ,contacts.getId() );
            startActivity(intentEdit);


            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        cursor.moveToPosition(position);
        @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex("number"));
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }
}