package EIProjectBeta.EIProjectBeta;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;




public class EIProject_Check  extends Activity {
private  ImageView qrcodeButton;
private  ImageView ieButton;
private  ImageView neverDoButton;
private  ImageView doneButton;
private ProgressDialog myDialog = null;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_main);
       //取得BUTTON
        qrcodeButton =(ImageView)findViewById(R.id.check_qrButton);
        ieButton     =(ImageView)findViewById(R.id.check_ieButton);
        neverDoButton=(ImageView)findViewById(R.id.check_never);
        doneButton   =(ImageView)findViewById(R.id.check_done);
        //set qrcode Button clickListener
       qrcodeButton.setOnClickListener(new Button.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
        	  Intent intent = new Intent();
        	  intent.setClass(EIProject_Check.this, EIProject_Check_FromQrcode.class);
        	  
        	  /* 呼叫一個新的Activity */
        	  startActivity(intent);
        	  /* 關閉原本的Activity */
        	  EIProject_Check.this.finish();
          }
        });
       
       ieButton.setOnClickListener(new Button.OnClickListener()
       {
         @Override
         public void onClick(View v)
         {
          Intent intent = new Intent();
       	  intent.setClass(EIProject_Check.this, EIProject_Check_FromNet.class);
          myDialog = ProgressDialog.show(EIProject_Check.this, "請稍候片刻", "正在進行對獎中…");
       	  /* 呼叫一個新的Activity */
      	  startActivity(intent);
       	  /* 關閉原本的Activity */
       	  EIProject_Check.this.finish();
         }
       });
       
       neverDoButton.setOnClickListener(new Button.OnClickListener()
       {
         @Override
         public void onClick(View v)
         {
          Intent intent = new Intent();
       	  intent.setClass(EIProject_Check.this, EIProject_Check_neverDO.class);
       	  
       	  /* 呼叫一個新的Activity */
      	  startActivity(intent);
       	  /* 關閉原本的Activity */
       	  EIProject_Check.this.finish();
         }
       });
       
       doneButton.setOnClickListener(new Button.OnClickListener()
       {
         @Override
         public void onClick(View v)
         {
          Intent intent = new Intent();
       	  intent.setClass(EIProject_Check.this, EIProject_Check_done.class);
       	  
       	  /* 呼叫一個新的Activity */
      	  startActivity(intent);
       	  /* 關閉原本的Activity */
       	  EIProject_Check.this.finish();
         }
       });    
	}
	

}
