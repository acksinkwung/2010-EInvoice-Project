package EIProjectBeta.EIProjectBeta;



import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class EIProject_Check_win_detail extends EIProjectBeta {

    private int [] count;
    private String [] monthCStr = {"","一","二","三","四","五","六","七","八","九","十","十一","十二"};
   
    private ArrayList<HashMap<String, Object>> win_InvoiceData;
    private ArrayList<HashMap<String, Object>> lose_InvoiceData;
    private todoHistoryDB db;
    private Cursor cursor;
   
    private static int register;
    //
    private static final int SWIPE_MIN_DISTANCE = 120; 
    private static final int SWIPE_MAX_OFF_PATH = 250; 
    private static final int SWIPE_THRESHOLD_VELOCITY = 200; 
    private GestureDetector gestureDetector; 
    View.OnTouchListener gestureListener; 
    private Animation slideLeftIn; 
    private Animation slideLeftOut; 
    private Animation slideRightIn; 
    private Animation slideRightOut; 
    ViewFlipper flipper;
	//
    private todoLuckyNumberDB myToDoDB;
	private Cursor myCursor;
	private ListView sListView;
	private ListView hListView;
	private TextView L_title;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_check_win_main);  
        // 設定畫面切換動畫
        flipper=(ViewFlipper)findViewById(R.id.details);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in); 
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out); 
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in); 
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out); 
        gestureDetector = new GestureDetector(new MyGestureDetector());
        
        Bundle bundle = this.getIntent().getExtras();
        int sixth = bundle.getInt("sixth");
        Log.e("***sixth***",String.valueOf(sixth));
        //取得Title
        TextView win_tv = (TextView)findViewById(R.id.doneMainTitle);
        TextView lose_tv = (TextView)findViewById(R.id.lose_Title);
		//取得顯示中獎List
        ListView win_list = (ListView) findViewById(R.id.doneMainLV);
		ArrayList<HashMap<String, Object>> win_listItem = new ArrayList<HashMap<String, Object>>();  
		//
		sListView = (ListView) this.findViewById(R.id.s_numberSet);
        hListView = (ListView) this.findViewById(R.id.h_numberSet);
        L_title = (TextView)this.findViewById(R.id.lucky_Number_title);
		//設定TITLE月份
        switch(sixth){
        	case 1:
        		win_tv.setText("99年 "+(monthCStr[sixth])+"月~"+(monthCStr[sixth+1])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth])+"月~"+(monthCStr[sixth+1])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth])+"月~"+(monthCStr[sixth+1])+"月");
        		break;
        	case 2:
        		win_tv.setText("99年 "+(monthCStr[sixth+1])+"月~"+(monthCStr[sixth+2])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth+1])+"月~"+(monthCStr[sixth+2])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth+1])+"月~"+(monthCStr[sixth+2])+"月");
        		break;
        	case 3:
        		win_tv.setText("99年 "+(monthCStr[sixth+2])+"月~"+(monthCStr[sixth+3])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth+2])+"月~"+(monthCStr[sixth+3])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth+2])+"月~"+(monthCStr[sixth+3])+"月");
        		break;
        	case 4:
        		win_tv.setText("99年 "+(monthCStr[sixth+3])+"月~"+(monthCStr[sixth+4])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth+3])+"月~"+(monthCStr[sixth+4])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth+3])+"月~"+(monthCStr[sixth+4])+"月");
        		break;
        	case 5:
        		win_tv.setText("99年 "+(monthCStr[sixth+4])+"月~"+(monthCStr[sixth+5])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth+4])+"月~"+(monthCStr[sixth+5])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth+4])+"月~"+(monthCStr[sixth+5])+"月");
        		break;
        	case 6:
        		win_tv.setText("99年 "+(monthCStr[sixth+5])+"月~"+(monthCStr[sixth+6])+"月");
        		lose_tv.setText("99年 "+(monthCStr[sixth+5])+"月~"+(monthCStr[sixth+6])+"月");
        		L_title.setText("99年 "+(monthCStr[sixth+5])+"月~"+(monthCStr[sixth+6])+"月");
        		break;
        }
		
		
		//get wining Invoice Data
		win_InvoiceData = getWinIData(sixth);
		//assign data to ListItem
       	for (int n=0; n<win_InvoiceData.size(); n++) {
       		win_listItem.add(win_InvoiceData.get(n));
        }
        //set up Adapter
        SimpleAdapter win_listItemAdaptedr = new SimpleAdapter(this,win_listItem,
	            R.layout.detail_check_done_item,     
	            new String[] {"ItemDateTime","ItemInvoiceNumber","ItemMoney"}, 
	            new int[] {R.id.ItemDateTime,R.id.ItemInvoiceNumber,R.id.ItemMoney}
	    );
        //data繫結
		win_list.setAdapter(win_listItemAdaptedr);  
		
		
		ListView lose_list = (ListView) findViewById(R.id.IMenuListView);
		ArrayList<HashMap<String, Object>> lose_listItem = new ArrayList<HashMap<String, Object>>();  	
		lose_InvoiceData = getNoWinIData(sixth);
		
       	for (int n=0; n<lose_InvoiceData.size(); n++) {
       		lose_listItem.add(lose_InvoiceData.get(n));
        }
       	
        SimpleAdapter lose_listItemAdaptedr = new SimpleAdapter(this,lose_listItem,
	            R.layout.check_neverdo_item,     
	            new String[] {"ItemDateTime","ItemInvoiceNumber"}, 
	            new int[] {R.id.tvCheckNeverDOTime,R.id.tvCheckNeverDONumber}
	    );
		lose_list.setAdapter(lose_listItemAdaptedr);  
//		
		//
		 myToDoDB = new todoLuckyNumberDB(this);
	        /* 取得DataBase裡的資料 */
	        myCursor = myToDoDB.selectLucky(sixth);

	        //設定特獎adapter
	        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.luckynumber_item,
	            myCursor, new String[]
	            { todoLuckyNumberDB.LUCKYNUMBER_FIELD_SIID}, new int[]
	            { R.id.lucky_Number_item });
	        //設定頭獎adapter
	        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, R.layout.luckynumber_item,
	            myCursor, new String[]
	            { todoLuckyNumberDB.LUCKYNUMBER_FIELD_HIID}, new int[]
	            { R.id.lucky_Number_item });
	        //資料繫結
	        sListView.setAdapter(adapter);
	        hListView.setAdapter(adapter2);
        
		
		
    }
 // 進行手指左右移動View的Class
    public class MyGestureDetector extends SimpleOnGestureListener { 
        @Override 
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) { 
            try { 
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) 
                    return false; 
                // right to left swipe 
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
                  //  Log.v(LOGID,"right to left swipe detected"); 
                    flipper.setInAnimation(slideLeftIn); 
                    flipper.setOutAnimation(slideLeftOut); 
                    flipper.showNext(); 
                    

                } // left to right swipe  
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { 
                   // Log.v(LOGID,"left to right swipe detected");                     
                    flipper.setInAnimation(slideRightIn); 
                    flipper.setOutAnimation(slideRightOut); 
                    flipper.showPrevious(); 
                  
                } 
            } catch (Exception e) { 
                // nothing 
            } 
            return false; 
        } 
    } 

    // This doesn't work 
    @Override 
    public boolean onTouchEvent(MotionEvent event) { 
        if (gestureDetector.onTouchEvent(event)){ 
           // Log.v(LOGID,"screen touched"); 
            return true; 
        } 
        else{ 
            return false; 
        } 
    } 
   
   
    private ArrayList<HashMap<String, Object>> getWinIData(int selNum) {
    	
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	 
		    todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.select_winInvoice();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if ((Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2==selNum){
		    	HashMap<String, Object> map = new HashMap<String, Object>();  
		    	map.put("ItemInvoiceNumber", cursor.getString(1));
	            map.put("ItemDateTime", cursor.getString(2));
	            map.put("ItemStoreName", cursor.getString(3));
	            map.put("ItemStatus", R.drawable.star);
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
	            alIData.add(map);
		    	}
		    }
	
        return alIData;
	}
    private ArrayList<HashMap<String, Object>> getNoWinIData(int selNum) {
    	
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	 
		    todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.select_noWinInvoice();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if ((Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2==selNum){
		    	HashMap<String, Object> map = new HashMap<String, Object>();  
		    	map.put("ItemInvoiceNumber", cursor.getString(1));
	            map.put("ItemDateTime", cursor.getString(2));
	   
	            map.put("ItemStatus", R.drawable.star);
	            alIData.add(map);
		    	}
		    }
	
        return alIData;
	}
   
        
}
