package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_Acquire_Select extends EIProject_Acquire{
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquire_main_2);
		TextView title = (TextView) findViewById(R.id.tvAcquireMenuTitle);
		int n = ((new Date().getMonth())/2)-1;
		title.setText("99ж~ "+monthCStr[n*2]+"ды~"+monthCStr[n*2+1]+"ды");
		ListView list = (ListView) findViewById(R.id.AcquireMenuListView);
		listItem = getIData(n);
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.acquire_detail_list_items,     
	            new String[] {"ItemInvoiceNumber","ItemInvoiceNumberLight","ItemDateTime", "ItemStatus"}, 
	            new int[] {R.id.ItemAcquireInvoiceNumber,R.id.ItemAcquireInvoiceNumberLight,R.id.ItemAcquireDateTime,R.id.ItemAcquireStatus}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selINum = arg2;
				sendStr = getQRCodeStr(arg2);
				Intent intent = new Intent();
		        intent.setClass(EIProject_Acquire_Select.this,EIProject_Acquire_Send.class);
		    	startActivity(intent);			
			}
		});

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
		
	}
	protected ArrayList<HashMap<String, Object>> getIData(int selNum) {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	    try {
		    todoAcquireDB db = new todoAcquireDB(this);
		    Cursor cursor = db.selectDetail();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	
		    	if ((Integer.parseInt((cursor.getString(2).split("/")[1]))+1)/2-1==selNum && Integer.parseInt(cursor.getString(5))==2) {
			    	HashMap<String, Object> map = new HashMap<String, Object>();
				    	if (Integer.parseInt(cursor.getString(6))!=0) {
				    		map.put("ItemInvoiceNumber", cursor.getString(1).substring(0,2+Integer.parseInt(cursor.getString(6))-1));
				    		map.put("ItemInvoiceNumberLight", cursor.getString(1).substring(2+Integer.parseInt(cursor.getString(6))-1));
				    	} else {
				    		map.put("ItemInvoiceNumber",cursor.getString(1));
				    	}
				    	map.put("ItemDateTime", cursor.getString(2));
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
			            map.put("ItemStatus", R.drawable.star);
			            map.put("ItemStatusA", R.drawable.acquire);
			            alIData.add(map);
		    	}
		    }
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }
        return alIData;
	}
    private String getQRCodeStr(int selNum) {
    	
    	String IID = "";
    	String IID_C = "";
    	String IID_R = "";
    	try {
	    	for (int n = 0; n < listItem.size(); n++) { 
	    		if (n==selNum) {
		    		if (IID !="" && IID_C!="" && IID_R!="") {
						IID = IID + ",";
						IID_C = IID_C + ",";
						IID_R = IID_R + ",";
					}
					
		    	}
	    	}
    	} catch(Exception e) {
    		Log.e(TAG,e.toString());
    	}
    	IID = IID + listItem.get(selNum).get("ItemIID");
		IID_C = IID_C + listItem.get(selNum).get("ItemCR").toString().split(",")[0];
		IID_R = IID_R + listItem.get(selNum).get("ItemCR").toString().split(",")[1];
    	Log.d("*********************************",String.valueOf(IID.getBytes()[0]) + String.valueOf(IID.getBytes()[1]) + IID.substring(2) + "10001" + IID_C + "10001"+ IID_R);
    	return String.valueOf(IID.getBytes()[0]) + String.valueOf(IID.getBytes()[1]) + IID.substring(2) + "10001" + IID_C + "10001"+ IID_R;

    }
}
