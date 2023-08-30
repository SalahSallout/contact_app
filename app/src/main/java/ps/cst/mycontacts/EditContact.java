package ps.cst.mycontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditContact extends AppCompatActivity {
EditText et_name , et_number , et_email;
Button btn_save;
Contacts contacts;

    private dbOperation dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        btn_save = findViewById(R.id.btn_save);
        et_email = findViewById(R.id.et_email);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        contacts = new Contacts();
        contacts.setId(getIntent().getExtras().getInt("_id"));
        contacts.setNumber(getIntent().getExtras().getString("number"));
        contacts.setEmail(getIntent().getExtras().getString("email"));
        contacts.setName(getIntent().getExtras().getString("name"));
        et_email.setText(contacts.getEmail());
        et_number.setText(contacts.getNumber());
        et_name.setText(contacts.getName());
        dbOperations = new dbOperation(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = et_name.getText().toString();
                String cNum = et_number.getText().toString();
                String cEmail = et_email.getText().toString();

                if (contacts != null) {
                    contacts.setName(cName);
                    contacts.setNumber(cNum);
                    contacts.setEmail(cEmail);
                    dbOperations.update(contacts);
                }

                setResult(RESULT_OK);
                startActivity(new Intent(EditContact.this , MainActivity.class));
                finish();
            }
        });


    }
}