package EIProjectBeta.EIProjectBeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class todoLuckyNumberDB extends ToDoDB
{
	public todoLuckyNumberDB(Context context)
	{
		super(context);
	}
	public Cursor selectLucky(int quarter){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+LUCKYNUMBER_TABLE_NAME+" where "+LUCKYNUMBER_FIELD_quarter+" = "+quarter;
		Cursor cursor =db.rawQuery(sql, null);
		return cursor;
	}
	public long insertLucky(String text)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(LUCKYNUMBER_FIELD_TEXT, text);
		long row = db.insert(LUCKYNUMBER_TABLE_NAME, null, cv);
		return row;
	}
	public void updateLucky(int id, String text)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = LUCKYNUMBER_FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(LUCKYNUMBER_FIELD_TEXT, text);
		db.update(LUCKYNUMBER_TABLE_NAME, cv, where, whereValue);
	}
}
