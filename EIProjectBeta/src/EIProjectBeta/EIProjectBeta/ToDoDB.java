package EIProjectBeta.EIProjectBeta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDB extends SQLiteOpenHelper
{
	protected final static String DATABASE_NAME = "invoice_db";
	protected final static int DATABASE_VERSION = 1;
	
	protected final static String P_TABLE_NAME = "P_IID";
	protected final static String P_FIELD_id = "_id";
	protected final static String P_FIELD_IID= "IID";
	protected final static String P_FIELD_C= "C";
	protected final static String P_FIELD_R= "R";
	
	protected final static String DETAIL_TABLE_NAME = "IID_Detail";
	protected final static String DETAIL_FIELD_id = "_id";
	protected final static String DETAIL_FIELD_IID= "IID";
	protected final static String DETAIL_FIELD_time= "time";
	protected final static String DETAIL_FIELD_storeID= "store_ID";
	protected final static String DETAIL_FIELD_amount = "amount";
	protected final static String DETAIL_FIELD_state = "state";
	protected final static String DETAIL_FIELD_money = "money";
	
	protected final static String LUCKYNUMBER_TABLE_NAME = "lucky_number";
	protected final static String LUCKYNUMBER_FIELD_id = "_id";
	protected final static String LUCKYNUMBER_FIELD_TEXT = "todo_text";
	protected final static String LUCKYNUMBER_FIELD_quarter = "quarter";
	protected final static String LUCKYNUMBER_FIELD_SIID = "S_IID";
	protected final static String LUCKYNUMBER_FIELD_HIID = "H_IID";

	protected final static String HISTORY_TABLE_NAME = "certificate";
	protected final static String HISTORY_FIELD_id = "_id";
	protected final static String HISTORY_FIELD_certifacate_id= "certificate_id";
	protected final static String HISTORY_FIELD_time= "time";
	protected final static String HISTORY_FIELD_IID= "IID";
	protected final static String HISTORY_FIELD_money = "money";
	protected final static String HISTORY_FIELD_taxmoney = "taxmoney";   
	
	protected final static String DONATE_TABLE_NAME = "donate";
	protected final static String DONATE_FIELD_id = "_id";
	protected final static String DONATE_FIELD_datetime = "datetime";
	protected final static String DONATE_FIELD_IID = "IID";
	protected final static String DONATE_FIELD_target = "target";
	  
	protected final static String ORG_TABLE_NAME = "org_Info";
	protected final static String ORG_FIELD_id = "_id";
	protected final static String ORG_FIELD_orgInfo_id = "orgInfo_id";
	protected final static String ORG_FIELD_name = "name";
	protected final static String ORG_FIELD_introduce = "introduce";
	 
	public ToDoDB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
	public Cursor selectP()
	{
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.query(P_TABLE_NAME, null, null, null, null, null, null);
	    return cursor;
	}
	public Cursor selectDetail()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DETAIL_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}	
	public Cursor selectLucky()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(LUCKYNUMBER_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}
//do History query	
	public Cursor selectHistory()
	{
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.query(HISTORY_TABLE_NAME, null, null, null, null, null, null);
	    return cursor;
	}
	public Cursor select_nerverDoInvoice(){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+DETAIL_TABLE_NAME+" where "+DETAIL_FIELD_state+" = "+"0";
		Cursor cursor =db.rawQuery(sql, null);
	    return cursor;
	}
	
	public Cursor selectOrg()
	{
		SQLiteDatabase db = this.getReadableDatabase();		  
		Cursor cursor = db.query(ORG_TABLE_NAME, null, null, null, null, null, null);		 
		return cursor;
	}
	public Cursor selectDenote()
	{
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.query(DONATE_TABLE_NAME, null, null, null, null, null, null);
	    return cursor;
	}
	public long insertP(String IID,String C,String R)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues cv = new ContentValues();
	    cv.put(P_FIELD_IID, IID);
	    cv.put(P_FIELD_C, C);
	    cv.put(P_FIELD_R, R);
	    long row = db.insert(P_TABLE_NAME, null, cv);
	    return row;
	}
	public long insertDetail(String IID,String time,String storeID,String amount)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues cv = new ContentValues();
	    cv.put(DETAIL_FIELD_IID, IID);
	    cv.put(DETAIL_FIELD_time, time);
	    cv.put(DETAIL_FIELD_storeID, storeID);
	    cv.put(DETAIL_FIELD_amount, amount);
	    cv.put(DETAIL_FIELD_state,0);
	    cv.put(DETAIL_FIELD_money,1);
	    long row = db.insert(DETAIL_TABLE_NAME, null, cv);
	    return row;
	}
	public long insertDonate(String datetime,String IID,String target)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues cv = new ContentValues();
	    cv.put(DONATE_FIELD_datetime, datetime);
	    cv.put(DONATE_FIELD_IID, IID);
	    cv.put(DONATE_FIELD_target, target);
	    long row = db.insert(DONATE_TABLE_NAME, null, cv);
	    return row;
	}
	public void deleteP(int id)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    String where = P_FIELD_id + " = ?";
	    String[] whereValue =
	    { Integer.toString(id) };
	    db.delete(P_TABLE_NAME, where, whereValue);
	}
	public void deleteDetail(int id)
	{
	    SQLiteDatabase db = this.getWritableDatabase();
	    String where = DETAIL_FIELD_id + " = ?";
	    String[] whereValue =
	    { Integer.toString(id) };
	    db.delete(DETAIL_TABLE_NAME, where, whereValue);
	}
	public void deleteLucky(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String where = LUCKYNUMBER_FIELD_id + " = ?";
		String[] whereValue ={ Integer.toString(id) };
		db.delete(LUCKYNUMBER_TABLE_NAME, where, whereValue);
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
