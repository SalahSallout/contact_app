package ps.cst.mycontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private EditText name, number, email;
    private dbOperation dbOperations;
    private Contacts contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.et_name);
        number = findViewById(R.id.et_number);
        email = findViewById(R.id.et_email);
        Button add = findViewById(R.id.btn_save);

        dbOperations = new dbOperation(this);

        Intent intent = getIntent();
        contact = (Contacts) intent.getSerializableExtra("contact");
        if (contact != null) {
            name.setText(contact.getName());
            number.setText(contact.getNumber());
            email.setText(contact.getEmail());
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName = name.getText().toString();
                String cNum = number.getText().toString();
                String cEmail = email.getText().toString();

                if (contact != null) {
                    contact.setName(cName);
                    contact.setNumber(cNum);
                    contact.setEmail(cEmail);
                    dbOperations.update(contact);
                } else {
                    Contacts newContact = new Contacts(cName, cNum, cEmail);
                    dbOperations.insert(newContact);
                }

                setResult(RESULT_OK);
                startActivity(new Intent(AddActivity.this , MainActivity.class));
                finish();
            }
        });
    }
}
