package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ViewFlipper;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class EIProject_Acquire_Record extends EIProject_Acquire{
	protected static int selRecord;
	protected ViewFlipper viewFlipperAcquireRecord;
	protected static ArrayList<HashMap<String, Object>>  CerMenuData;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquire_his_main_1);
		ListView list = (ListView) findViewById(R.id.AcquireHisMenuListView);
		CerMenuData = getCerMenuData();
		listItem = CerMenuData;
        SimpleAdapter listItemAdapter = new SimpleAdapter(EIProject_Acquire_Record.this,listItem,R.layout.acquire_his_detail_list_items,     
	            new String[] {"ItemAcquireHisDate","ItemAcquireHisMoney"}, 
	            new int[] {R.id.ItemAcquireHisDate,R.id.ItemAcquireHisMoney}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selRecord = arg2;	
				Intent intent = new Intent();
		        intent.setClass(EIProject_Acquire_Record.this,EIProject_Acquire_Record_Detail.class);
		    	startActivity(intent);
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
		
	}
    protected ArrayList<HashMap<String, Object>> getCerIDetailData(String IID) {
    	ArrayList<HashMap<String, Object>> alIData = new ArrayList<HashMap<String, Object>>();
 	    try {
 	    	todoAcquireDB db = new todoAcquireDB(this);
		    Cursor cursor = db.selectDetail();
		    int flag;
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	flag=0;
		    	for (int n=0; n<IID.split(",").length; n++) {
		    		if (cursor.getString(1).equals(IID.split(",")[n])) {
		    			flag=1;
		    		}
		    	}
		    	if (flag==1) {
			    	HashMap<String, Object> map = new HashMap<String, Object>();
			    	
			    	if (Integer.parseInt(cursor.getString(6))!=0) {
			    		map.put("ItemInvoiceNumber", cursor.getString(1).substring(0,2+Integer.parseInt(cursor.getString(6))-1));
			    		map.put("ItemInvoiceNumberLight", cursor.getString(1).substring(2+Integer.parseInt(cursor.getString(6))-1));
			    	} else {
			    		map.put("ItemInvoiceNumber",cursor.getString(1));
			    	}
			    	map.put("ItemDateTime", cursor.getString(2));
		            map.put("ItemCheck", true);
		            map.put("ItemIID",cursor.getString(1));
		            map.put("ItemCR",getInvoiceCR(cursor.getString(1)));
		            String money = "0";
		            
		            if (cursor.getString(6).equals("1")) {
		            	money="$200000";
		            }else if (cursor.getString(6).equals("2")) {
		            	money="$40000";
		            }else if (cursor.getString(6).equals("3")) {
		            	money="$10000";
		            }else if (cursor.getString(6).equals("4")) {
		            	money="$4000";
		            }else if (cursor.getString(6).equals("5")) {
		            	money="$1000";
		            }else if (cursor.getString(6).equals("6")) {
		            	money="$200";
		            }
		            map.put("ItemMoney", money);
		            map.put("ItemStatusA", R.drawable.acquire);
		            alIData.add(map);
		    	}
		    }
 	    } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }    	
		return alIData;
    }
    protected ArrayList<HashMap<String, Object>> getCerMenuData () {
    	ArrayList<HashMap<String, Object>> alIData = new ArrayList<HashMap<String, Object>>();
 	    try {
		    todoAcquireDB db = new todoAcquireDB(this);
		    Cursor cursor = db.selectHistory();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("ItemAcquireHisDate", cursor.getString(1).split(" ")[0]);
			    map.put("ItemAcquireHisIID",cursor.getString(2));
			    map.put("ItemAcquireHisCount",cursor.getString(2).split(",").length);
			    map.put("ItemAcquireHisMoney","$"+cursor.getString(3));
			    map.put("ItemAcquireHisTaxMoney","$"+cursor.getString(4));
		        alIData.add(map);
		    }
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }    	
		return alIData;
    	
    }
}
