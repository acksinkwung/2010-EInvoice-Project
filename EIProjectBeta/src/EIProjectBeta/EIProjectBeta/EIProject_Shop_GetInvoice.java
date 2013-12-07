package EIProjectBeta.EIProjectBeta;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;

/* 引用Camera類別 */
import android.hardware.Camera;

/* 引用PictureCallback作為取得拍照後的事件 */
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.TextView;

public class EIProject_Shop_GetInvoice extends Activity implements SurfaceHolder.Callback
{
	private Camera mCamera01;
	private String TAG = "Camera";
	private SurfaceView mSurfaceView01;
	private SurfaceHolder mSurfaceHolder01;
	private Intent intent;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_camera);
		intent = this.getIntent();
		mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
		mSurfaceHolder01 = mSurfaceView01.getHolder();
		mSurfaceHolder01.addCallback(EIProject_Shop_GetInvoice.this);
		mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	private void initCamera()
	{
		try
		{
			mCamera01 = Camera.open();
			Camera.Parameters parameters = mCamera01.getParameters();
		    parameters.setPictureFormat(PixelFormat.JPEG);
		    parameters.setPreviewSize(320,240);
		    parameters.setPictureSize(320,240);
		    
		    mCamera01.setParameters(parameters);
		    mCamera01.setPreviewDisplay(mSurfaceHolder01);
		    
		    mCamera01.startPreview();      
		    Camera.PreviewCallback cb = new Camera.PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height,
							0, 0, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height); 
					BinaryBitmap bbitmap = new BinaryBitmap(new HybridBinarizer(source));
					Result result = new Result("", null, null, null);
					try {
						result = new MultiFormatReader().decode(bbitmap);
						String decryptedText = result.getText();
				        if(decryptedText!="")
				        {
				            if (decryptedText.indexOf("Exception")>-1)
				            {
				            }else{
				            	mCamera01.stopPreview();
				            	TextView text = (TextView) findViewById(R.id.tvStatus);
				                text.setText("掃描成功!");
				                text.setTextColor(Color.WHITE);
				        		
				            	Bundle bundle = new Bundle();
				            	bundle.putString("qrcode",decryptedText);
				            	intent.putExtras(bundle);
				            	EIProject_Shop_GetInvoice.this.setResult(RESULT_OK,intent);
				            	EIProject_Shop_GetInvoice.this.finish();
				            }
				        }
					}catch (Exception e) {
					    Log.e(TAG,e.toString());
					}

				}
		      };
		      mCamera01.setPreviewCallback(cb);
	
		}
		catch(Exception e)
		{
        }
      
    
	}
	@Override
	public void surfaceCreated(SurfaceHolder surfaceholder)
	{
		initCamera();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceholder)
	{
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}
}