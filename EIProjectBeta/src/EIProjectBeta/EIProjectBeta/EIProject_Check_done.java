package EIProjectBeta.EIProjectBeta;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_Check_done extends EIProjectBeta {

    private int [] count;
    private String [] monthCStr = {"","一","二","三","四","五","六","七","八","九","十","十一","十二"};
   
    private ArrayList<HashMap<String, Object>> alIData;
    
    private todoHistoryDB db;
    private Cursor cursor;
   
    private static int register;
  
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main_1);  
      
        
		ListView list = (ListView) findViewById(R.id.MenuListView);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
        
        count = new int[(new Date().getMonth()+1)/2+1];
        //計算發票月份
        countData();
        //發票總數
        int countTotal = 0;
   
        countTotal = count[0]+count[1]+count[2]+count[3]+count[4]+count[5];
        //設定title
        TextView tvHistoryMenuTitle = (TextView)findViewById(R.id.tvHistoryMenuTitle);
        tvHistoryMenuTitle.setText("對獎記錄("+countTotal+")");
       	
        for (int n=((new Date().getMonth())/2+1)-1; n>=0; n--) {
       		HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemMenu", "99年 "+monthCStr[n*2+1]+"月~"+monthCStr[n*2+2]+"月("+count[n]+")");
            listItem.add(map);
        }
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.menu_list_items,     
	            new String[] {"ItemMenu"}, 
	            new int[] {R.id.ItemMenu}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				  Intent intent = new Intent();
            	  intent.setClass(EIProject_Check_done.this, EIProject_Check_ShowDoneDetail.class);
            	  Bundle bundle = new Bundle();
            	  bundle.putInt("sixth", (new Date().getMonth())/2+1-arg2);
            	  intent.putExtras(bundle);
            	  /* 呼叫一個新的Activity */
           	  startActivity(intent);
				
		    
				
				
			}
		});
		
    }
 
    private void countData() {
	    	//get data from DB
	    	todoHistoryDB db = new todoHistoryDB(this);
	    	cursor = db.select_doneInvoice();
	    //計算發票月份
 	    	while(!cursor.isLast()){
 	    		cursor.moveToNext();
 	    				count[(Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2-1]++;
 	    	}
 	    	
}
 
        
}
