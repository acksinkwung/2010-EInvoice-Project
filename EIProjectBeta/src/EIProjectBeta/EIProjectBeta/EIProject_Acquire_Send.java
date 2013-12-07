	package EIProjectBeta.EIProjectBeta;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class EIProject_Acquire_Send extends EIProject_Acquire implements SurfaceHolder.Callback {
	// QRCode相關孌數
	private SurfaceView msvDrawQRCode;
	private SurfaceHolder mshDrawQRCode;
	private Bitmap bm; 
	private LuminanceSource source; 
	private Result result; 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acquire_main_qrcode);
        msvDrawQRCode = (SurfaceView) findViewById(R.id.svAcquireDrawQRCode);
    	// 繫結SurfaceView，取得SurfaceHolder物件 
        mshDrawQRCode = msvDrawQRCode.getHolder();
        mshDrawQRCode.addCallback(this);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				initCamera();
		}
		return true;
	}
	private void initCamera() {
		Intent intent = new Intent();
        intent.setClass(EIProject_Acquire_Send.this,EIProject_Acquire_GetCertificate.class);
    	startActivityForResult(intent, CAMERA_REQUEST_CODE);
	}
	/*private void initCamera() {
	    // 開啟系統內建Camera
	    File pic = new File(SDCard_path +"/s_qr.jpg");
	    try {
	        pic.delete();
	    }catch(Exception e){
		    Log.e(TAG,e.toString());
	    }
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    File out = new File(SDCard_path +"/s_qr.jpg");
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
		    Log.e(TAG,e.toString());
		}
		decryptedText = result.getText();
		return decryptedText;
	}
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
	    Matrix matrix = new Matrix();

        // rotate the Bitmap
        matrix.postRotate(0);
 
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0,
        		bm.getWidth(), bm.getHeight(), matrix, true);
	    mCanvas01.drawBitmap(resizedBitmap,QRCodeX,QRCodeY, mPaint01);
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
        	receivedStr = strQR;
    		Intent intent = new Intent();
            intent.setClass(EIProject_Acquire_Send.this,EIProject_Acquire_CheckList.class);
        	startActivity(intent);
			EIProject_Acquire_Send.this.finish();
		}		
    		/*
	        String strQRTestFile = SDCard_path+"/s_qr.jpg";     
		    File myImageFile = new File(strQRTestFile); 
		    if(myImageFile.exists())
		    {
			    Bitmap myBmp = BitmapFactory.decodeFile(strQRTestFile); 
		        String strQR = decodeQRImage(myBmp);    	
		        if(strQR!="")
		        {
		            // 測試用，顯示簽章號碼
		            if (strQR.indexOf("Exception")>-1)
		            {
		        		builder = new AlertDialog.Builder(this);
		        		builder.setTitle("訊息");
		        		builder.setNegativeButton("知道了!", null);
		                builder.setMessage("未知的錯誤");
		                builder.show();
		            }
		            else
		            {
		            	receivedStr = strQR;
		            	
		        		Intent intent = new Intent();
		                intent.setClass(EIProject_Acquire_Send.this,EIProject_Acquire_CheckList.class);
		            	startActivity(intent);
		            }
		        } 
    		 */
		   
        
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		AndroidQREncode(sendStr,4);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		AndroidQREncode(sendStr,4);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        
	}

}
