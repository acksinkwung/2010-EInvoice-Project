package EIProjectBeta.EIProjectBeta;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class EIProject_Shop extends EIProjectBeta implements SurfaceHolder.Callback {
	private SurfaceView msvDrawQRCode;
	private SurfaceHolder mshDrawQRCode;
	// 使用者資料孌數
	private String UserID="A123456789";
	private static BigInteger UserC;
	private static BigInteger UserR;
	private static String blindsign;
	// QRCode相關孌數
	private Bitmap bm; 
	//private LuminanceSource source; 
	//private Result result; 
	protected static String IID;
	protected static String datetime;
	protected static String amount;
	// Blind Signature相關孌數
	private BigInteger e;
	private BigInteger n;
	private String TAG = "EIProject_Shop";
    private Builder builder;
    // 體感變數
    private SensorManager mSensorManager;
    private boolean isSetBasePosi;
    private float fBasePosiX;
    private int useMode;
    private todoShopDB db;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.shop_main);
        msvDrawQRCode = (SurfaceView) findViewById(R.id.svDrawQRCode);
    	// 繫結SurfaceView，取得SurfaceHolder物件 
        mshDrawQRCode = msvDrawQRCode.getHolder();
        mshDrawQRCode.addCallback(this);
    	// 設定訊息Dialog資訊
    	builder = new AlertDialog.Builder(this);
        builder.setTitle("訊息");
  	    builder.setNegativeButton("知道了!", null);	 
    	// 體感感應器
    	mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	isSetBasePosi = false;
    	useMode = 0;
    	blindsign=getCoinAndRandomNum();
    }
	private void initCamera() {
		Intent intent = new Intent();
        intent.setClass(EIProject_Shop.this,EIProject_Shop_GetInvoice.class);
    	startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}

    private void readPubData() {
        // 讀取Public相關資料
 	    try {
	  	    FileInputStream fstream = new FileInputStream(  SDCard_path + "/public.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String [] strData = br.readLine().split(",");  
		    e = new BigInteger(strData[0]);
		    n = new BigInteger(strData[1]);
		    in.close();
        } catch (Exception e) {
	        Log.e(TAG,e.toString());
	    }
    }
    private void saveDataToDB(String IID,String C,String R,String time,String storeID,String amount){
    	db=new todoShopDB(this);
    	db.insertP(IID, C, R);
		Date date = new Date(time); 
		date.setMonth(date.getMonth()-2);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); 
    	db.insertDetail(IID, df.format(date), storeID, amount);
    	
    }
    private String getCoinAndRandomNum() {
       // 取得C和R的資訊
	   readPubData();
	   BigInteger b = new BigInteger("0");
	   try {
	       String c = (UserID+","+(new Date().toGMTString()));
		   byte [] msg = c.getBytes("UTF8");	
		   BigInteger m = new BigInteger(msg);
		   UserC = m;
	       SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		   byte [] randomBytes = new byte[10];
		   BigInteger r = null;
		   BigInteger gcd = null;
		   BigInteger one = new BigInteger("1");
	       //check that gcd(r,n) = 1 && r < n && r > 1
	 	   do {
	           random.nextBytes(randomBytes);
		       r = new BigInteger(1, randomBytes);
		       gcd = r.gcd(n);
		   }while(!gcd.equals(one) || r.compareTo(n)>=0 || r.compareTo(one)<=0);
		   //********************* BLIND ************************************
		   b = ((r.modPow(e,n)).multiply(m));
	 	   UserR = r;
	 	  Log.d(TAG,"B:" + b.toString());
	    }catch(Exception e){
	 	   Log.e(TAG,e.toString());
	    }
	    return b.toString();
	}
	private boolean VeritySignature(String strQR) {
	    readPubData();
	    try {
		    BigInteger m = UserC;
		    BigInteger r = UserR;   		 
		    Log.d(TAG,"C:" + m.toString());
		    Log.d(TAG,"R:" + r.toString());
		    BigInteger s = new BigInteger(strQR);
		    Log.d(TAG,"S:" + s.toString());
		    //check that s is equal to a signature of m:
		    s = r.modInverse(n).multiply(s);
		    Log.d(TAG,"S/R:" + s.toString());
		    //check that s is equal to a signature of m:
	        BigInteger check = s.modPow(e,n);           
	        if (m.equals(check)) {
	            return true;
		    }
	    } catch (Exception e) {
		    Log.e(TAG,e.toString());
		}
	    return false;
	}  
	/*
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
		    Log.e(TAG,e.toString());
		}
		decryptedText = result.getText();
		return decryptedText;
	}*/
	private void AndroidQREncode(String strEncoding, int qrcodeVersion)
	{
	    // 自訂產生QR Code的函數
	    try
	    { 
	       // 建構QRCode編碼物件 
	       com.swetake.util.Qrcode testQrcode = new com.swetake.util.Qrcode();
	       // L','M','Q','H' 
	       testQrcode.setQrcodeErrorCorrect('L');
	       // "N","A" or other
	       testQrcode.setQrcodeEncodeMode('N');
	       // 0-20 
	       testQrcode.setQrcodeVersion(qrcodeVersion);
	       // getBytes
	       byte[] bytesEncoding = strEncoding.getBytes("utf-8");
	       // 將字串透過calQrcode函數轉換成boolean陣列
	       boolean[][] bEncoding = testQrcode.calQrcode(bytesEncoding);
	       // 依據編碼後的boolean陣列，繪圖
	       drawQRCode(bEncoding,getResources().getColor(R.drawable.black));
	   }
	   catch (Exception e)
	   {
	       Log.e(TAG,e.toString());
	   }
    }  
	private void drawQRCode(boolean[][] bRect,int colorFill)
	{
		// 繪製QRCode資訊圖片 
	    int intPadding = 5;
	    // 欲在SurfaceView上繪圖，需先lock鎖定SurfaceHolder 
	    Canvas mCanvas01 = mshDrawQRCode.lockCanvas();
	    int QRCodeSize = Math.min(dm.heightPixels, dm.widthPixels)-30;
	    int QRCodeX = dm.widthPixels/2-(QRCodeSize+30)/2;
	    int QRCodeY = dm.heightPixels/2-(QRCodeSize+30)/2;
	    int size = (int)((QRCodeSize-10)/(bRect.length));
	    QRCodeSize = size * (bRect.length)+10 ;
	    bm = Bitmap.createBitmap(QRCodeSize,QRCodeSize,Config.ARGB_8888);
	    Canvas mCanvasBm = new Canvas(bm);
	    // 設定畫布繪製顏色
	    mCanvas01.drawColor(getResources().getColor(R.drawable.black));
	    mCanvasBm.drawColor(getResources().getColor(R.drawable.white));
	    // 建立畫筆 
	    Paint mPaint01 = new Paint();
	    // 設定畫筆顏色及樣式
	    mPaint01.setStyle(Paint.Style.FILL);
	    mPaint01.setColor(colorFill);
	    mPaint01.setStrokeWidth(1.0F);
	    // 逐一載入2維boolean陣列	
	    for (int i=0;i<bRect.length;i++)
	    {
	      for (int j=0;j<bRect.length;j++)
	      {
	        if (bRect[j][i])
	        {
	          // 依據陣列值，繪出條碼方塊
	          mCanvasBm.drawRect(new Rect(intPadding+j*size, intPadding+i*size, intPadding+j*size+size, intPadding+i*size+size), mPaint01);
	        }
	      }
	    }
	    mCanvas01.drawBitmap(bm,QRCodeX,QRCodeY, mPaint01);
	    mshDrawQRCode.unlockCanvasAndPost(mCanvas01);
	    saveQRCodeToFile();
	  }
	private void saveQRCodeToFile() {
		// 儲存QRCode圖檔
	    try {                    
	        // 輸出的圖檔位置
	        FileOutputStream fos = new FileOutputStream( SDCard_path +"/qr.jpg" );
	        // 將 Bitmap 儲存成 PNG / JPEG 檔案格式
	        bm.compress( Bitmap.CompressFormat.JPEG, 100, fos );
	        // 釋放
	        fos.close();
	    } catch (Exception e){
	        Log.e(TAG,e.toString());
	    }
	}
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle=data.getExtras();
            String strQR = bundle.getString("qrcode");
		    String [] QRCodeValue = strQR.split(",");
		    if (VeritySignature(QRCodeValue[0]) == true) {
		    	IID=QRCodeValue[1];
		        datetime=(new Date().toGMTString());
		        amount=QRCodeValue[3];
		     	saveDataToDB(QRCodeValue[1],UserC.toString(),UserR.toString(),(new Date().toGMTString()),QRCodeValue[2],QRCodeValue[3]);
				Intent intent = new Intent();
				intent.setClass(EIProject_Shop.this,EIProject_Shop_Invoice.class);
				startActivity(intent);
				blindsign=getCoinAndRandomNum();
				EIProject_Shop.this.finish();
		   }		   
        }
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				initCamera();
				break;
		}
		return true;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		AndroidQREncode(blindsign,5);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// QRCode Version 4
  		AndroidQREncode(blindsign,5);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(mSensorListener);
	}
	@Override
	protected void onResume() {
		// 體感感應器
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(
				mSensorListener,
				sensors.get(0),
				SensorManager.SENSOR_DELAY_NORMAL
				);
		super.onResume();
	}
	public SensorEventListener mSensorListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onSensorChanged(SensorEvent event) {
			// 判斷何時該切換相機模式
			
			try {
				if (isSetBasePosi==false) {
					fBasePosiX = event.values[SensorManager.DATA_X];
					isSetBasePosi = true;
				}
				float fPitchAngle = event.values[SensorManager.DATA_X];
				if (((fPitchAngle-fBasePosiX)<-150 || (fPitchAngle-fBasePosiX)>150) && useMode==0) {
					useMode=1;
				}
				if ((fPitchAngle-fBasePosiX)>-50 && (fPitchAngle-fBasePosiX)<50 && useMode==1) {
					useMode=0;
					//initCamera();
					
				}
				//Log.d(TAG,String.valueOf(fPitchAngle-fBasePosiX));
			}catch(Exception e){
				Log.e(TAG,e.toString());
			}
			
		}
	};

}
