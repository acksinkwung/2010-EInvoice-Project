package EIProjectBeta.EIProjectBeta;
import android.content.ContentValues;
import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;

public class todoHelpDB extends ToDoDB 
{
	public todoHelpDB(Context context)
	{
		super(context);
	}
	public void update_invoicedetail(String IID)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String [] IIDArr = IID.split(",");
		for (int n=0; n<IIDArr.length; n++) {	
			String where = P_FIELD_IID + " = ?";
			String[] whereValue = { IID };
			ContentValues cv = new ContentValues();
			cv.put(DETAIL_FIELD_state,"4");
			db.update(DETAIL_TABLE_NAME, cv, where, whereValue);
		}
	}
	public void deleteP(String IID)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String [] IIDArr = IID.split(",");
		for (int n=0; n<IIDArr.length; n++) {	
			String where = P_FIELD_IID + " = ?";
			String[] whereValue = { IID };
			db.delete(P_TABLE_NAME, where, whereValue);
		}
	}
}
