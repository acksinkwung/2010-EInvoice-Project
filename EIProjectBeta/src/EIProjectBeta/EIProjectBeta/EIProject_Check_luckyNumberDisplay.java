package EIProjectBeta.EIProjectBeta;



import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;




public class EIProject_Check_luckyNumberDisplay  extends Activity {
	  private todoLuckyNumberDB myToDoDB;
	  private Cursor myCursor;
	  private ListView sListView;
	  private ListView hListView;
	  private TextView title;
	  
	  private String [] monthCStr = {"","一","二","三","四","五","六","七","八","九","十","十一","十二"};
	  
	  
	  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luckynumber);
        
        //取得月份參數
        Bundle bundle = this.getIntent().getExtras();
        int sixth = bundle.getInt("selNum");
        
        Log.e("selNum",String.valueOf(sixth));
        //取得ListView,TextView,ImageView物件
        sListView = (ListView) this.findViewById(R.id.s_numberSet);
        hListView = (ListView) this.findViewById(R.id.h_numberSet);
        title = (TextView)this.findViewById(R.id.lucky_Number_title);
        ImageView btnLeft=(ImageView)findViewById(R.id.left);
        
      //設定TITLE月份
        switch(sixth){
        	case 1:
        		title.setText("99年 "+(monthCStr[sixth])+"月~"+(monthCStr[sixth+1])+"月");
        		break;
        	case 2:
        		title.setText("99年 "+(monthCStr[sixth+1])+"月~"+(monthCStr[sixth+2])+"月");
        		break;
        	case 3:
        		title.setText("99年 "+(monthCStr[sixth+2])+"月~"+(monthCStr[sixth+3])+"月");
        		break;
        	case 4:
        		title.setText("99年 "+(monthCStr[sixth+3])+"月~"+(monthCStr[sixth+4])+"月");
        		break;
        	case 5:
        		title.setText("99年 "+(monthCStr[sixth+4])+"月~"+(monthCStr[sixth+5])+"月");
        		break;
        	case 6:
        		title.setText("99年 "+(monthCStr[sixth+5])+"月~"+(monthCStr[sixth+6])+"月");
        		break;
        }
        
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
        //設定返回button
        btnLeft.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View view) {  
            	Intent intent = new Intent();
          	  	intent.setClass(EIProject_Check_luckyNumberDisplay.this, EIProject_Check_done.class);
            	startActivity(intent);
            }  
        });  

	}
	

}
