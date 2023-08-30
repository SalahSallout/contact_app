package ps.cst.mycontacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends CursorAdapter implements Filterable {
    private Cursor filteredCursor;
    private FilterQueryProvider filterQueryProvider;
    private  ArrayList<Contacts> contacts_;

    public myAdapter(Context context, Cursor c , ArrayList<Contacts> contacts) {
        super(context, c);
        contacts_ = contacts;
        filteredCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.list_item, parent, false);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.tv_contactName);
        TextView num = view.findViewById(R.id.tv_contactNum);
        TextView email = view.findViewById(R.id.tv_contactEmail);
        Button callBtn = view.findViewById(R.id.btn_call);
        Button msgBtn = view.findViewById(R.id.btn_email);
        LinearLayout lvLinear = view.findViewById(R.id.lvLinear);
        name.setText(cursor.getString(cursor.getColumnIndex("name")));
        num.setText(cursor.getString(cursor.getColumnIndex("number")));
        email.setText(cursor.getString(cursor.getColumnIndex("email")));


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < contacts_.size(); i++){
                    if (contacts_.get(i).getNumber().equals(num.getText())){
                        Uri uri = null;
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        String number = contacts_.get(i).getNumber().toString();
                        uri = Uri.parse("tel:" + number);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                }
            }
        });


        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_STREAM, "subject");
                intent.putExtra(Intent.EXTRA_EMAIL, "I'm email body .");
                context.startActivity(Intent.createChooser(intent, "send Email "));
            }
        });


        lvLinear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });


    }

    @Override
    public Cursor getCursor() {
        return filteredCursor;
    }

    @Override
    public int getCount() {
        return filteredCursor.getCount();
    }

    @Override
    public Filter getFilter() {
        Filter myFilter= new Filter() {
            FilterResults fs=new FilterResults();

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Contacts> container =new ArrayList<>();
                for(int i=0; i<contacts_.size(); i++){
                    Contacts c=contacts_.get(i);
                    String str=c.getName()+"";
                    String chr=charSequence+"";
                    if(c.getName().toLowerCase().contains(chr.toLowerCase()) || str.contains(charSequence)){
                        container.add(c);
                    }else if (chr.isEmpty() || str .isEmpty()){
                        container.addAll(contacts_) ;
                    }
                }
                fs.values=container;
                fs.count=container.size();
                return fs;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contacts_=(ArrayList<Contacts>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return myFilter;
    }

}