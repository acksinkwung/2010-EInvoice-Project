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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class EIProject_Help_Record extends EIProject_Help {
	protected static int selRid;
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.help_his_main_1);
		ListView list = (ListView) findViewById(R.id.HelpHisMenuListView);
		listHistoryMenuItem = getHelpHisMenuData();
        SimpleAdapter listItemAdapter = new SimpleAdapter(EIProject_Help_Record.this,listHistoryMenuItem,R.layout.help_his_detail_list_items,     
	            new String[] {"ItemHelpHisDateTime","ItemHelpHisOrgName","ItemHelpHisAmount"}, 
	            new int[] {R.id.ItemHelpHisDateTime,R.id.ItemHelpHisOrgName,R.id.ItemHelpHisAmount}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {		
				selRid = arg2;
				Intent intent = new Intent();
		        intent.setClass(EIProject_Help_Record.this,EIProject_Help_Record_Detail.class);
		    	startActivity(intent);
			}
		});
	    
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
		
	}
    private String getOrgName(String donateID) {
    	for (int n=0; n<alOrgIData.size(); n++) {
    		if (alOrgIData.get(n).get("ItemOrgId").toString().equals(donateID)) {
    			return alOrgIData.get(n).get("ItemOrgName").toString();
    		}
    	}
		return "";
    }
    private ArrayList<HashMap<String, Object>> getHelpHisMenuData () {
    	ArrayList<HashMap<String, Object>> alIData = new ArrayList<HashMap<String, Object>>();
 	    try {
		    todoHelpDB db = new todoHelpDB(this);
		    Cursor cursor = db.selectDenote();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("ItemHelpHisDateTime", cursor.getString(1));
			    map.put("ItemHelpHisAmount", cursor.getString(2).split(",").length+"±i");
			    map.put("ItemHelpHisOrgName", getOrgName(cursor.getString(3)));
			    map.put("ItemHelpHisIID", cursor.getString(2));
			    alIData.add(map);
		    }
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }    	
		return alIData;
    	
    }

}
