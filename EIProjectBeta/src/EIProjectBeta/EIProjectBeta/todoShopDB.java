package EIProjectBeta.EIProjectBeta;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class todoShopDB extends ToDoDB
{
	public todoShopDB(Context context)
	{
		super(context);
	}
	public void updateP(int id, String text)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = P_FIELD_id + " = ?";
		String[] whereValue ={ Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(P_FIELD_IID, text);
		db.update(P_TABLE_NAME, cv, where, whereValue);
	}
}
