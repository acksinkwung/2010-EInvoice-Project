package EIProjectBeta.EIProjectBeta;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class EIProject_Help extends EIProjectBeta {

    protected String TAG = "EIProject_Help";
    protected String [] monthCStr = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
    protected static ArrayList<HashMap<String, Object>> alIData;
    protected static ArrayList<HashMap<String, Object>> selIData;
    protected static ArrayList<HashMap<String, Object>> alOrgIData;
    protected static ArrayList<HashMap<String, Object>> listHistoryMenuItem;
    protected static int count;
    protected static int selNum;
    protected static int selINum;
    protected static todoHelpDB db;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_main_1);  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button btnHistoryHelp = (Button)findViewById(R.id.btnHistoryHelp);
        btnHistoryHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setClass(EIProject_Help.this,EIProject_Help_Record.class);
		    	startActivity(intent);
			}
		});
		alOrgIData = new  ArrayList<HashMap<String, Object>>();
		alOrgIData = getOrgIData();
		db = new todoHelpDB(this);
	}
	private  ArrayList<HashMap<String, Object>>  getOrgIData() {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	    try {
			Cursor cursor = db.selectOrg();
			while(!cursor.isLast()){
			 	cursor.moveToNext();
		    	HashMap<String, Object> map = new HashMap<String, Object>();
		    	map.put("ItemOrgId", cursor.getString(1) );
		    	map.put("ItemOrgName", cursor.getString(2) );
		    	map.put("ItemOrgIntro", cursor.getString(3) );
		    	alIData.add(map);
		    }
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }
        return alIData;		
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				Intent intent = new Intent();
				intent.setClass(EIProject_Help.this,EIProject_Help_Select_Detail.class);
		    	startActivity(intent);
		}
		return true;
	}    
}
