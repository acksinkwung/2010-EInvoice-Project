package EIProjectBeta.EIProjectBeta;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class todoHistoryDB extends ToDoDB
{
	public todoHistoryDB(Context context)
	{
		super(context);
	}
	public Cursor selectDetail()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select IID_Detail._id, IID_Detail.IID, IID_Detail.time, Store_Info.name, IID_Detail.amount, IID_Detail.state, IID_Detail.money from  IID_Detail inner join Store_Info on  IID_Detail.store_ID= Store_Info.storeInfo_id ", null);
		return cursor;
	}
	public Cursor select_nerverDoInvoice(){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+DETAIL_TABLE_NAME+" where "+DETAIL_FIELD_state+" = "+"0"; 
		Cursor cursor =db.rawQuery(sql, null);
	    return cursor;
	}
	public Cursor select_doneInvoice(){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+DETAIL_TABLE_NAME+" where state = 1 or state = 2 ";
		Cursor cursor =db.rawQuery(sql, null);
	    return cursor;
	}
	public Cursor select_winInvoice(){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+DETAIL_TABLE_NAME+" where state = 2 ";
		Cursor cursor =db.rawQuery(sql, null);
	    return cursor;
	}
	public Cursor select_noWinInvoice(){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+DETAIL_TABLE_NAME+" where state = 1 or state = 0 ";
		Cursor cursor =db.rawQuery(sql, null);
	    return cursor;
	}

}
