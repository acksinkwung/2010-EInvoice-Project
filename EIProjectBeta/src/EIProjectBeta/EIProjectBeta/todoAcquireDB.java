package EIProjectBeta.EIProjectBeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class todoAcquireDB extends ToDoDB
{
	public todoAcquireDB(Context context)
	{
		super(context);
	}
	public long insert_record(String datetime,String IID,String money,String taxmoney)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(HISTORY_FIELD_time, datetime);
		cv.put(HISTORY_FIELD_IID, IID);
		cv.put(HISTORY_FIELD_money, money );
		cv.put(HISTORY_FIELD_taxmoney, taxmoney );
		long row = db.insert(HISTORY_TABLE_NAME, null, cv);
		return row;
	}
}
