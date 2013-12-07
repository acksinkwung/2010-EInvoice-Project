package EIProjectBeta.EIProjectBeta;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class EIProject_Acquire extends EIProjectBeta {
	protected String TAG = "EIProject_Acquire";
	protected String [] monthCStr = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
	protected static boolean [] isCheck;
	protected static ArrayList<HashMap<String, Object>> listItem;
	protected static int selListNum;
	protected static String sendStr;
	protected static String receivedStr;
	protected static int selINum;
	protected static String IID;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquire_main_1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button btnHistoryAcquire = (Button)findViewById(R.id.btnHistoryAcquire);
        btnHistoryAcquire.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setClass(EIProject_Acquire.this,EIProject_Acquire_Record.class);
		    	startActivity(intent);
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				Intent intent = new Intent();
		        intent.setClass(EIProject_Acquire.this,EIProject_Acquire_Select.class);
		    	startActivity(intent);
				break;
		}
		return true;
		
	}
}
