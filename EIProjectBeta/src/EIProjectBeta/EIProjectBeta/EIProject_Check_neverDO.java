package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_Check_neverDO extends EIProjectBeta {

    private String TAG = "EIProject_Check_neverDO";
    private int [] count;
    private String [] monthCStr = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
    private int [] dayForMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
    private Map<String,String> storeName;
    private ArrayList<HashMap<String, Object>> alIData;
    private Dialog dialog;
    private TextView tvChartTitle;
    private Bitmap tempYearChart;
    private todoHistoryDB db;
    private Cursor cursor;
    private int selMonth;
	// 圖表相關資訊
	int chartCurrentLevel=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_main_1);
        Bundle bundle = this.getIntent().getExtras();
		ListView list = (ListView) findViewById(R.id.MenuListView);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
        count = new int[(new Date().getMonth()+1)/2+1];
        //計算發票月份
        countData();
        //發票總數
        int countTotal = 0;
   
       // countTotal = cursor.getCount();
        countTotal = count[0]+count[1]+count[2]+count[3]+count[4]+count[5];
        TextView tvHistoryMenuTitle = (TextView)findViewById(R.id.tvHistoryMenuTitle);
        tvHistoryMenuTitle.setText("未對獎發票("+countTotal+")");
       	for (int n=((new Date().getMonth())/2+1)-1; n>=0; n--) {
       		HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemMenu", "99年 "+monthCStr[n*2]+"月~"+monthCStr[n*2+1]+"月("+count[n]+")");
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
				
			    showIDetail((new Date().getMonth())/2-arg2+1);
		        selMonth= ((new Date().getMonth())/2-arg2+1)*2;
				// TODO Auto-generated method stub
				
			}
		});
		
        
    }
    private void showIDetail(int selNum) {
        setContentView(R.layout.history_main_2);
		ListView list = (ListView) findViewById(R.id.IMenuListView);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		alIData = getIData(selNum);
       	for (int n=0; n<alIData.size(); n++) {
       		listItem.add(alIData.get(n));
        }
        
        SimpleAdapter listItemAdaptedr = new SimpleAdapter(this,listItem,
	            R.layout.check_neverdo_item,     
	            new String[] {"ItemDateTime","ItemInvoiceNumber"}, 
	            new int[] {R.id.tvCheckNeverDOTime,R.id.tvCheckNeverDONumber}
	    );
		list.setAdapter(listItemAdaptedr);  
    }
    private ArrayList<HashMap<String, Object>> getIData(int selNum) {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	 
		    todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.selectDetail();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if ((Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2==selNum && cursor.getString(5).equals("0")){
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
    private void countData() {
	    	//get data from DB
	    	todoHistoryDB db = new todoHistoryDB(this);
	    	cursor = db.selectDetail();
	    //計算發票月份
 	    	while(!cursor.isLast()){
 	    		cursor.moveToNext();
 	    			if(cursor.getString(5).equals("0"))
 	    				count[(Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2-1]++;
 	    	}
 	    	
}
 
        
}
