package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_History_Detail extends EIProject_History {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		storeName = new TreeMap<String,String>() ;
        showIDetail(selMonth/2);        
	}
	private ArrayList<HashMap<String, Object>> getIData(int selNum) {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	 
		    todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.selectDetail();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if ((Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2==selNum){
		    	HashMap<String, Object> map = new HashMap<String, Object>();  
		    	map.put("ItemInvoiceNumber", cursor.getString(1));
	            map.put("ItemDateTime", cursor.getString(2));
	            map.put("ItemStoreName", cursor.getString(3));
	            map.put("ItemMoney", "$"+cursor.getString(4));
	            map.put("ItemStatus", R.drawable.star);
	            alIData.add(map);
		    	}
		    }
	
        return alIData;
	}
	private void showIDetail(int selNum) {
        setContentView(R.layout.history_main_2);
		ListView list = (ListView) findViewById(R.id.IMenuListView);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		alIData = getIData(selNum);
       	for (int n=0; n<alIData.size(); n++) {
       		listItem.add(alIData.get(n));
        }
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.detail_list_items,     
	            new String[] {"ItemDateTime","ItemStoreName", "ItemInvoiceNumber","ItemMoney","ItemStatus"}, 
	            new int[] {R.id.ItemDateTime,R.id.ItemStoreName,R.id.ItemInvoiceNumber,R.id.ItemMoney,R.id.ItemStatus}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					Dialog dialog = new Dialog(EIProject_History_Detail.this);
	                dialog.setContentView(R.layout.detail_list_items_dialog);
               
	                int selNum = arg2;
	                TextView text = (TextView) dialog.findViewById(R.id.HistoryDialogItemInvoiceNumber);
	                text.setText((String)((alIData.get(selNum)).get("ItemInvoiceNumber").toString()));
	        		text = (TextView) dialog.findViewById(R.id.HistoryDialogItemStoreName);
	                text.setText((String)((alIData.get(selNum)).get("ItemStoreName").toString()));
	                text = (TextView) dialog.findViewById(R.id.HistoryDialogItemDateTime);
	                text.setText((String)((alIData.get(selNum)).get("ItemDateTime").toString()));
	        		text = (TextView) dialog.findViewById(R.id.HistoryDialogItemMoney);
	                text.setText((String)((alIData.get(selNum)).get("ItemMoney").toString()));
	                dialog.setCanceledOnTouchOutside(true);
	                dialog.show();
				}catch(Exception e){
					Log.e(TAG,e.toString());
				}
				// TODO Auto-generated method stub
				
			}
		});		
	}
    
}
