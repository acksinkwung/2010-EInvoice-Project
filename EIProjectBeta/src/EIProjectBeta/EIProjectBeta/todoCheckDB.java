package EIProjectBeta.EIProjectBeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class todoCheckDB extends ToDoDB
{
	public todoCheckDB(Context context)
	{
		super(context);
	}
	public void update_State_Money(String IID, int state,int money)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = DETAIL_FIELD_IID + " = ?";
		String[] whereValue = { IID };
		ContentValues cv = new ContentValues();
		cv.put(DETAIL_FIELD_state,String.valueOf(state) );
		cv.put(DETAIL_FIELD_money,String.valueOf(money));
		db.update(DETAIL_TABLE_NAME, cv, where, whereValue);
	}
}
