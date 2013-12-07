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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_Help_Select_Detail extends EIProject_Help {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        alIData = getIData();
		if (alIData==null) {
			setContentView(R.layout.help_main_noinvoice);
		} else {
        setContentView(R.layout.help_main_2);
		ListView list = (ListView) findViewById(R.id.HelpDetailListView);
		
       	TextView title = (TextView) findViewById(R.id.tvHelpDetailTitle);
        title.setText("99ж~ "+monthCStr[(((new Date().getMonth())/2))*2]+"ды~"+monthCStr[(((new Date().getMonth())/2))*2+1]+"ды");
        if (alIData!=null) {
	        SimpleAdapter listItemAdapter = new SimpleAdapter(this,alIData,
		            R.layout.help_detail_list_items,     
		            new String[] {"ItemInvoiceNumber","ItemDateTime","ItemCheck"}, 
		            new int[] {R.id.ItemHelpInvoiceNumber,R.id.ItemHelpDateTime,R.id.ItemHelpCheck}
		    );
			list.setAdapter(listItemAdapter);  
        }

		Button btnAcquire = (Button)findViewById(R.id.btnHelp);
		btnAcquire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	    		ListView list = (ListView) findViewById(R.id.HelpDetailListView);
	    		selIData = new  ArrayList<HashMap<String, Object>>();
		    	for (int n = 0; n < alIData.size(); n++) {
		    	
		    		CheckBox checkbox = (CheckBox) list.getChildAt(n).findViewById(R.id.ItemHelpCheck);	   
		    		
		    		if(checkbox.isChecked())	
					{
		    			selIData.add(alIData.get(n));
					}
		    	}
				Intent intent = new Intent();
		        intent.setClass(EIProject_Help_Select_Detail.this,EIProject_Help_Select_Org.class);
		    	startActivity(intent);
			}
		});
		}

	}    
    protected ArrayList<HashMap<String, Object>> getIData() {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	    try {

		    Cursor cursor = db.selectDetail();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	
		    	if (Integer.parseInt(cursor.getString(6))!=0 && ((Integer.parseInt((cursor.getString(2).split("/")[1]))-1)/2)==(((new Date().getMonth())/2))) {
			    	HashMap<String, Object> map = new HashMap<String, Object>();  
			    	if (!(cursor.getString(5).equals("4"))) {
				    	map.put("ItemInvoiceNumber",cursor.getString(1));
				    	map.put("ItemIID", cursor.getString(1));
				    	map.put("ItemDateTime", cursor.getString(2));
			            map.put("ItemCheck", true);
			            map.put("ItemCR",getInvoiceCR(cursor.getString(1)));
			            alIData.add(map);
			    	}

		    	}
		    }
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }
        return alIData;
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}  
}
