package EIProjectBeta.EIProjectBeta;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;




public class EIProject_Check_FromQrcode  extends Activity {
private ProgressDialog progressDialog = null;
private static String[] s_number; 
private static String[] h_number; 	
private Bitmap bm; 
private LuminanceSource source; 
private Result result; 
//private static boolean flag=false;
private static int sixth;//把年分為六份,1~2月 -> sixth = 1
// Blind Signature相關孌數
public static final int CAMERA_REQUEST_CODE = 11;
public String SDCard_path;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sixth=1;
      
    	initCamera();

	}

	 protected void onActivityResult(int requestCode, int resultCode, Intent data)
	    {	try{
	    	/*String strQRTestFile = "/sdcard/EIProject/luckyNumber_qr.jpg";
	        Bitmap myBmp = BitmapFactory.decodeFile(strQRTestFile); 
	            //儲存deQRcode的值
	        String strQR = decodeQRImage(myBmp);
	        
	   		
	           Log.e("CheckQRCode",strQR);
	          */
	    	Bundle bundle;
	    	if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
	    		String[] LuckyNumber_temp;
	            bundle=data.getExtras();
	            String strQR = bundle.getString("qrcode");   		

	   		LuckyNumber_temp = strQR.split(";");
	   		
	   		//s_number = quater,s_luckyNumber1,s_luckyNumber2,s_luckyNumber3
	   		s_number = LuckyNumber_temp[0].split(",");
	   		//h_number = h_luckyNumber1,h_luckyNumber2,h_luckyNumber3
	   		h_number = LuckyNumber_temp[1].split(",");
	   		
	   		
	   		sixth = Integer.parseInt(s_number[0]);   
	   		if(check()){
	    		Intent intent = new Intent();	  
	      	  	intent.setClass(EIProject_Check_FromQrcode.this, EIProject_Check_win.class);
	      	  	bundle = new Bundle();
		       	bundle.putInt("sixth", sixth);
		       	intent.putExtras(bundle); 
	      	 
	     	  startActivity(intent);
	      	
	     	  EIProject_Check_FromQrcode.this.finish();
	    	}else{
	    		  Intent intent = new Intent();
		       	  intent.setClass(EIProject_Check_FromQrcode.this, EIProject_Check_nowin.class);
		       	  bundle = new Bundle();
		       	  bundle.putInt("sixth", sixth);
		       	  intent.putExtras(bundle);
		        	  
		        	
		       	  startActivity(intent);
		        	 
		       	  EIProject_Check_FromQrcode.this.finish();
	    	}
			    }
	    }catch(Exception e){
	    	EIProject_Check_FromQrcode.this.finish();
	    }
	   //***************************
    	EIProject_Check_FromQrcode.this.finish();	
	  
	   	
	    }
		private void initCamera() {
			Intent intent = new Intent();
	        intent.setClass(EIProject_Check_FromQrcode.this,EIProject_Check_GetCInvoiceQrcode.class);
	    	startActivityForResult(intent, CAMERA_REQUEST_CODE);
		}
	 /*private void initCamera() {
	    // 開啟系統內建Camera

	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	   
	    File out = new File("/sdcard/EIProject/luckyNumber_qr.jpg");
	    
	    Uri uri = Uri.fromFile(out);
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	    startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }*/
	private String decodeQRImage(Bitmap myBmp )
	{
	    // QRCode解碼
		source = new RGBLuminanceSource(myBmp); 
		String  decryptedText =  "ERROR";
		BinaryBitmap bbitmap = new BinaryBitmap(new HybridBinarizer(source));  
		result = new Result("", null, null, null);
		try {
			result = new MultiFormatReader().decode(bbitmap);
		}catch (Exception e) {
		    Log.v("****Error", e.toString());
		    EIProject_Check_FromQrcode.this.finish();
		}
		decryptedText = result.getText();
		Log.v("********DeCodeResult",result.getText());
		return decryptedText;
	}
	
	
	private boolean check(){
		boolean regist=true;
		try{
		todoCheckDB Checkdb=new todoCheckDB(this);
		todoHistoryDB HistoryDB = new todoHistoryDB(this);
		Cursor cursor = HistoryDB.select_nerverDoInvoice();
		String number = new String();//記錄發票號碼
		
		ArrayList<String> mArrayList = new ArrayList<String>(); 
	     for(cursor.moveToFirst(); cursor.moveToNext(); cursor.isAfterLast()) { 
	         // The Cursor is now set to the right position 
	         mArrayList.add(cursor.getString(1)); 
	     } 
	     Log.v("InvoiceARRAY",mArrayList.toString());
	     
	     for(int i=1;i<s_number.length;i++){
	    	 for(int j=0;j<mArrayList.size();j++){
	    		 
	    		 
	    		 if(s_number[i].equals(mArrayList.get(j).substring(2))){
	    			 regist = true;	
	    			 Log.e("luckyNumber",s_number[i]);
		    		 Log.v("Number",mArrayList.get(j).substring(2));
	    		 }
	    		 
	    	 }
	     }
	     boolean flag;
	     for (int j=0;j<mArrayList.size();j++) {
		    	Checkdb.update_State_Money(mArrayList.get(j), 1, 0);
	     }
	     for(int i=0;i<h_number.length;i++)
			{	  
			  for(int j=0;j<mArrayList.size();j++)
			  {
				flag = false;
			    number = mArrayList.get(j);

			    for(int k=0;k<6;k++)
		    	{
			    	Log.d("test1",h_number[i].substring(k));
			    	Log.d("test2",mArrayList.get(j).substring(k+2));
			    	if (h_number[i].substring(k).equals(mArrayList.get(j).substring(k+2)) && flag==false) {
			    		Checkdb.update_State_Money(mArrayList.get(j), 2, k+1);
			    		flag = true;
			    		
			    	}
		    	}

		   	}
		}
			      
	
	
		
		return regist;
		}catch(Exception e){
			EIProject_Check_FromQrcode.this.finish();
			return regist;
		}
		
		
	}
		

}
