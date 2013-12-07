package EIProjectBeta.EIProjectBeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog.Builder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class EIProjectBeta extends Activity {

	public static final int CAMERA_REQUEST_CODE = 11;
	public static final int VOICE_RECOGNITION_REQUEST_CODE = 12;
	public String SDCard_path;
    public int ViewDirect; 
    public DisplayMetrics dm;
    protected Builder builder;
	// ������k���ʬ����ܼ�
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public GestureDetector gestureDetector;
	public OnTouchListener gestureListener;
	public Animation slideLeftIn;
	public Animation slideLeftOut;
	public Animation slideRightIn;
	public Animation slideRightOut;
    public ViewFlipper viewFlipper;    
	private String TAG = "EIProject";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // �����ε{�����ù�����A���ϥμ��D�C
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ���o�ù����j�p
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels<=dm.heightPixels) {
        	ViewDirect = 1;		// ��
        }else{
        	ViewDirect = 2;		// ��
        }
        
        setContentView(R.layout.main);          
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.firstMainRelativeLayout);
        layout.setBackgroundResource(R.drawable.white);
		// �P�_ SD Card ���L���J
        File vSDCard = null;
        if( !(Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED))) {
        	// ���o SD Card ��m
        	vSDCard = Environment.getExternalStorageDirectory();
        }
    	// �P�_�ؿ��O�_�s�b
    	File vPath = new File( vSDCard.getParent() + vSDCard.getName() + "/EIProject" );
    	if( !vPath.exists()) {
    	  vPath.mkdirs();   
    	}
    	// �]�wSDCard���ڥؿ�
    	SDCard_path = vSDCard.getParent() + vSDCard.getName() + "/EIProject" ;
    	builder = new Builder(EIProjectBeta.this);
        builder.setTitle("�T��");
  	    builder.setNegativeButton("���D�F!", null);	 
        // �]�w�e�������ʵe
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
		slideRightIn = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,R.anim.push_right_out);
		gestureDetector = new GestureDetector(new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
		//importDataBase();
    }
 // �i�������k����View��Class
    class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideLeftIn);
					viewFlipper.setOutAnimation(slideLeftOut);
					viewFlipper.showNext();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					viewFlipper.setInAnimation(slideRightIn);
					viewFlipper.setOutAnimation(slideRightOut);
					viewFlipper.showPrevious();
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}    
    private void importDataBase() {
    	File dbFile = new File(Environment.getExternalStorageDirectory()+"/invoice_db");
    	File exportDir = new File(Environment.getDataDirectory() , "/data/EIProjectBeta.EIProjectBeta/databases");
    	if (!exportDir.exists()) {
    		exportDir.mkdirs();
    	}
    	File file = new File(exportDir, dbFile.getName());
    	try {
    		file.createNewFile();
    		this.copyFile(dbFile, file);
    	} catch (Exception e) {
    		Log.e(TAG,e.toString());
    	}
    }
    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
        if (inChannel != null)
            inChannel.close();
        if (outChannel != null)
            outChannel.close();
        }
    }

    private void exportDataBase() {
    	File dbFile = new File(Environment.getDataDirectory() + "/data/EIProjectBeta.EIProjectBeta/databases/invoice_db");
    	File exportDir = new File(Environment.getExternalStorageDirectory(),"exportdata");
    	if (!exportDir.exists()) {
    		exportDir.mkdirs();
    	}
    	File file = new File(exportDir, dbFile.getName());
    	try {
    		file.createNewFile();
    		this.copyFile(dbFile, file);
    	} catch (Exception e) {
    		Log.e(TAG,e.toString());
    	}
    }
    // �إߥؿ�
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//�Ѽ�1:�s��id, �Ѽ�2:itemId, �Ѽ�3:item����, �Ѽ�4:item�W��
        menu.add(0, 0, 0, "�ʪ�");
        menu.getItem(0).setIcon(R.drawable.shopicon);
        menu.add(0, 1, 0, "�O��");
        menu.getItem(1).setIcon(R.drawable.historyicon);
        menu.add(0, 2, 0, "���");
        menu.getItem(2).setIcon(R.drawable.invoiceicon);
        menu.add(0, 3, 0, "���");
        menu.getItem(3).setIcon(R.drawable.moneyicon);
        menu.add(0, 4, 0, "����");
        menu.getItem(4).setIcon(R.drawable.helpicon);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //�̾�itemId�ӧP�_�ϥΪ��I����@��item
    	startActivity(item.getItemId(),EIProjectBeta.this);
    	return super.onOptionsItemSelected(item);
    }
    public void startActivity(int id,Context context) {
        Intent intent = new Intent();
        
    	if (id==0) {
        	intent.setClass(context,EIProject_Shop.class);
        }
    	if (id==1) {
        	intent.setClass(context,EIProject_History.class);
        }
    	if (id==2) {
    		intent.setClass(context,EIProject_Check.class);
        }
    	if (id==3) {
    		intent.setClass(context,EIProject_Acquire.class);
        }
    	if (id==4) {
    		intent.setClass(context,EIProject_Help.class);
        }
    	startActivityForResult(intent,id);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
    		String voiceInfo = (data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)).get(0);
    		
    		builder.setMessage(voiceInfo);
    		
    		int id = -1;
    		if (voiceInfo.indexOf("��")>-1 || voiceInfo.indexOf("��")>-1) {
    			id = 0;
    		}
    		if (voiceInfo.indexOf("��")>-1) {
    			id = 1;
    		}
    		if (voiceInfo.indexOf("��")>-1) {
    			id = 2;
    		}
    		if (voiceInfo.indexOf("�s")>-1 || voiceInfo.indexOf("510")>-1 || voiceInfo.indexOf("��")>-1) {
    			id = 3;
    		}
    		if (voiceInfo.indexOf("��")>-1 || voiceInfo.indexOf("��")>-1) {
    			id = 4;
    		}
    		
    		if (id!=-1) { 
    			startActivity(id,EIProjectBeta.this);
    		}
    		builder.show();
    		
        } else {
		   
        }
    }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//openOptionsMenu();
		exportDataBase();
		// �]�wView Touch��Event
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				new Handler().postDelayed(new Runnable() { public void run() { openOptionsMenu(); } }, 100); 
				//startVoiceRecognitionActivity();
		}
		
		return true;
	}
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        
    }
	public String getInvoiceCR(String IID) {
		String result="";
	    try {
	    	todoShopDB db = new todoShopDB(this);
	    	Cursor cursor = db.selectP();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if (cursor.getString(1).equals(IID)) {
		    		result = cursor.getString(2)+","+cursor.getString(3); 
		    		break;
		    	}
			}
	    } catch (Exception e) {
	    	Log.e(TAG,e.toString());
	    }
	    return result; 
	}

    
}