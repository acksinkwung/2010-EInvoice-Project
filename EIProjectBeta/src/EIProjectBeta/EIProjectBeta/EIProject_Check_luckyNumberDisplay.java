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
	  
	  private String [] monthCStr = {"","�@","�G","�T","�|","��","��","�C","�K","�E","�Q","�Q�@","�Q�G"};
	  
	  
	  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luckynumber);
        
        //���o����Ѽ�
        Bundle bundle = this.getIntent().getExtras();
        int sixth = bundle.getInt("selNum");
        
        Log.e("selNum",String.valueOf(sixth));
        //���oListView,TextView,ImageView����
        sListView = (ListView) this.findViewById(R.id.s_numberSet);
        hListView = (ListView) this.findViewById(R.id.h_numberSet);
        title = (TextView)this.findViewById(R.id.lucky_Number_title);
        ImageView btnLeft=(ImageView)findViewById(R.id.left);
        
      //�]�wTITLE���
        switch(sixth){
        	case 1:
        		title.setText("99�~ "+(monthCStr[sixth])+"��~"+(monthCStr[sixth+1])+"��");
        		break;
        	case 2:
        		title.setText("99�~ "+(monthCStr[sixth+1])+"��~"+(monthCStr[sixth+2])+"��");
        		break;
        	case 3:
        		title.setText("99�~ "+(monthCStr[sixth+2])+"��~"+(monthCStr[sixth+3])+"��");
        		break;
        	case 4:
        		title.setText("99�~ "+(monthCStr[sixth+3])+"��~"+(monthCStr[sixth+4])+"��");
        		break;
        	case 5:
        		title.setText("99�~ "+(monthCStr[sixth+4])+"��~"+(monthCStr[sixth+5])+"��");
        		break;
        	case 6:
        		title.setText("99�~ "+(monthCStr[sixth+5])+"��~"+(monthCStr[sixth+6])+"��");
        		break;
        }
        
        myToDoDB = new todoLuckyNumberDB(this);
        /* ���oDataBase�̪���� */
        myCursor = myToDoDB.selectLucky(sixth);

        //�]�w�S��adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.luckynumber_item,
            myCursor, new String[]
            { todoLuckyNumberDB.LUCKYNUMBER_FIELD_SIID}, new int[]
            { R.id.lucky_Number_item });
        //�]�w�Y��adapter
        SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, R.layout.luckynumber_item,
            myCursor, new String[]
            { todoLuckyNumberDB.LUCKYNUMBER_FIELD_HIID}, new int[]
            { R.id.lucky_Number_item });
        //���ô��
        sListView.setAdapter(adapter);
        hListView.setAdapter(adapter2);
        //�]�w��^button
        btnLeft.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View view) {  
            	Intent intent = new Intent();
          	  	intent.setClass(EIProject_Check_luckyNumberDisplay.this, EIProject_Check_done.class);
            	startActivity(intent);
            }  
        });  

	}
	

}
