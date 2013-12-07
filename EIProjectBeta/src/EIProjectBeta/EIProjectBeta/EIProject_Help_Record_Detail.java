package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_Help_Record_Detail extends EIProject_Help_Record {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_his_main_2);
    	TextView title = (TextView) findViewById(R.id.tvHelpHistoryDetailTitle);
    	title.setText(listHistoryMenuItem.get(selRid).get("ItemHelpHisOrgName").toString());
    	ArrayList<HashMap<String, Object>> listItem = getDonateIDetailData(listHistoryMenuItem.get(selRid).get("ItemHelpHisIID").toString());
    	ListView list = (ListView) findViewById(R.id.HelpHistoryDetailListView);
		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.help_his_detail_list_items2,     
	            new String[] {"ItemInvoiceNumber","ItemDateTime"}, 
	            new int[] {R.id.ItemHelpHistoryDetailInvoiceNumber,R.id.ItemHelpHistoryDetailDateTime}
	    );
		list.setAdapter(listItemAdapter);     	
	}
    private ArrayList<HashMap<String, Object>> getDonateIDetailData(String IID) {
    	ArrayList<HashMap<String, Object>> alIData = new ArrayList<HashMap<String, Object>>();
 	    try {
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
			    	
		    		map.put("ItemInvoiceNumber",cursor.getString(1));
			    	map.put("ItemDateTime", cursor.getString(2));
		            map.put("ItemStatusA", R.drawable.danote);
		            alIData.add(map);
		    	}
		    }
 	    } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }    	
		return alIData;
    }    
}
