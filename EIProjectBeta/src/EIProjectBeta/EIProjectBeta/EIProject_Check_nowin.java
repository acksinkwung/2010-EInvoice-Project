package EIProjectBeta.EIProjectBeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;




public class EIProject_Check_nowin  extends Activity {
private String [] monthCStr = {"","一","二","三","四","五","六","七","八","九","十","十一","十二"};
private static int sixth=5;
// Blind Signature相關孌數
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_nowin_main);
      
        	Bundle bundle = this.getIntent().getExtras();
        	sixth = bundle.getInt("sixth");
        	Log.e("**SIXTH***",String.valueOf(sixth));
        	TextView text = (TextView)this.findViewById(R.id.check_text);
        	text.setText("無中獎發票");
        	ImageButton bn =(ImageButton) this.findViewById(R.id.check_button);
        	ImageView iv = (ImageView) this.findViewById(R.id.checkImage);
        	iv.setImageResource(R.drawable.nowin);
        	bn.setOnClickListener(new View.OnClickListener() {  
        		public void onClick(View view) {  
        			
        		  Intent intent = new Intent();
              	  intent.setClass(EIProject_Check_nowin.this, EIProject_Check_nowin_detail.class);
              	  Bundle bundle = new Bundle();
              	  bundle.putInt("sixth", sixth);
              	  intent.putExtras(bundle);
              	  /* 呼叫一個新的Activity */
             	  startActivity(intent);

  				
        			
        			
        		}  
        	
        });  
 	
	}

		
		
		
	
	
	

}
