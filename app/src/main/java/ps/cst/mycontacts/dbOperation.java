package ps.cst.mycontacts;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class dbOperation {
    private SQLiteDatabase db;

    public dbOperation(Context context) {
        SqlOpenHelper sqlOpenHelper = new SqlOpenHelper(context, "contact", null, 1);
        db = sqlOpenHelper.getWritableDatabase();
    }

    public long insert(Contacts contact) {
        ContentValues cv = new ContentValues();
        cv.put("name", contact.getName());
        cv.put("number", contact.getNumber());
        cv.put("email", contact.getEmail());
        long result = db.insert("contact", null, cv);
        return result;
    }

    public long delete(Contacts contact) {
        String[] strings = new String[]{String.valueOf(contact.getId())};
        long result = db.delete("contact", "_id=?", strings);
        return result;
    }

    public long update(Contacts contact) {
        ContentValues cv = new ContentValues();
        cv.put("name", contact.getName());
        cv.put("number", contact.getNumber());
        cv.put("email", contact.getEmail());
        String[] strings = new String[]{String.valueOf(contact.getId())};
        long result = db.update("contact", cv, "_id=?", strings);
        return result;
    }

    public Cursor getCursor() {
        Cursor cursor = db.rawQuery("SELECT * FROM contact", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
}