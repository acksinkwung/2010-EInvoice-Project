package EIProjectBeta.EIProjectBeta;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EIProject_History extends EIProjectBeta {

    protected String TAG = "EIProject_History";
    protected String [] monthCStr = {"","","","","き","せ","","","","","",""};
    protected int [] dayForMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
    protected static int [] count;
    protected static int selMonth;
    protected int [] countChart;
    protected static Map<String,String> storeName;
    protected static ArrayList<HashMap<String, Object>> alIData;
    private Bitmap tempYearChart[];
    private ImageView ivCharts;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get Data from DB
        todoHistoryDB db = new todoHistoryDB(this);
        Cursor cursor = db.selectDetail();
        
        if (ViewDirect==1) {
	        setContentView(R.layout.history_main_1);  
			ListView list = (ListView) findViewById(R.id.MenuListView);
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
	        
	        count = new int[(new Date().getMonth()+1)/2+1];
	        //璸衡祇布る
	        countData();
	        //祇布羆计
	        int countTotal = 0;
	        countTotal = cursor.getCount();
	        
	        TextView tvHistoryMenuTitle = (TextView)findViewById(R.id.tvHistoryMenuTitle);
	        tvHistoryMenuTitle.setText("场祇布("+countTotal+")");
	       	for (int n=((new Date().getMonth())/2+1)-1; n>=0; n--) {
	       		HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("ItemMenu", "99 "+monthCStr[n*2]+"る~"+monthCStr[n*2+1]+"る("+count[n]+")");
	            listItem.add(map);
	        }
	        
	        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
		            R.layout.menu_list_items,     
		            new String[] {"ItemMenu"}, 
		            new int[] {R.id.ItemMenu}
		    );
			list.setAdapter(listItemAdapter);  
			list.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
			        selMonth= ((new Date().getMonth())/2+1-arg2)*2;
					Intent intent = new Intent();
			        intent.setClass(EIProject_History.this,EIProject_History_Detail.class);
			    	startActivity(intent);
					// TODO Auto-generated method stub
					
				}
			});
        }else if (ViewDirect==2) {
        	setContentView(R.layout.history_main_charts);
        	TextView tvChartTitle = (TextView) findViewById(R.id.tvChartsTitle);
        	tvChartTitle.setText("さ–る禣薄猵");
        	try{
        	genChart();
        	}catch(Exception e){
        		EIProject_History.this.finish();
        	}
       	}
	}

    private String getData(int type,int argv) {
    	// 眔ノ玻ネ瓜戈
    	String data="&chd=t:";
    	if (type==0) {
    		countChart = new int[12];
			try {
			    todoAcquireDB db = new todoAcquireDB(this);
			    Cursor cursor = db.selectDetail();
			    while(!cursor.isLast()){
			    	cursor.moveToNext();
			    	countChart[Integer.parseInt(cursor.getString(2).split("/")[1])-1]+=Integer.parseInt(cursor.getString(4));	
				}
	
			} catch (Exception e) {
				Log.e(TAG,e.toString());
			}
			data = data + countChart[0]; 
			for (int n=1;n<12;n++){
				data = data + "," + countChart[n];
			}
    	}
    	return data;
    }
    private void genChart() {
    	ivCharts = (ImageView) findViewById(R.id.ivCharts);           
        String mUrl="";
	    mUrl = "http://chart.apis.google.com/chart?chf=bg,s,000000";
	    mUrl+="&chxs=0,C2BDDD,18,0,l,676767|1,FF9900,18,0,l,676767";
	    mUrl+="&chxt=x,y";
	    mUrl+="&chs="+dm.widthPixels+"x"+dm.heightPixels;
	    mUrl+="&cht=lc";
	    mUrl+="&chco=E9225E";
	    mUrl+="&chls=8";
	    mUrl+="&chg=30,30";
	    mUrl+="&chds=0,100000";
	    mUrl+="&chma=40,20,20,30";
	    mUrl+=getXYValue(0,0);    	        
	    mUrl+=getData(0,0);
	    tempYearChart = new Bitmap[13];
	    tempYearChart[0] = getURLBitmap(mUrl);
	    String d = "bb";
	    for (int n=1; n<=12; n++) {
	    	if (n>=8) {
	    		d = "bbbr";
	    	}
	    	tempYearChart[n] = getURLBitmap(mUrl+="&chem=y;s=bubble_icon_text_small;d=dollar,"+d+","+countChart[n-1]+",FFFFFF;dp="+(n-1)+";ds=0");
	    }
        ivCharts.setImageBitmap(tempYearChart[0]);
    }
    private String getXYValue(int type,int argv) {
    	String str = "";
    	if (type==0) {
    		str = "&chxr=0,1,12|1,0,100000";
    	}
    	return str;
    }
    protected Bitmap getURLBitmap(String urlPic) {
    	// 硓筁URL眔Bitmapン
    	URL imageUrl = null;
    	Bitmap bitmap = null;
    	try {
    		imageUrl = new URL(urlPic);
    	}catch(Exception e){
    		Log.e(TAG,e.toString());
    	}
    	try
    	{
    		HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
    		conn.connect();
    		InputStream is = conn.getInputStream();
    		bitmap = BitmapFactory.decodeStream(is);
    		is.close();
    	}catch(Exception e){
    		Log.e(TAG,e.toString());
    	}
    	return bitmap;
    }
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	try{
    	int n = (int)((event.getX()-80)/40)+1;
    	if (n>=0 && n<=12) {
    		ivCharts.setImageBitmap(tempYearChart[n]);
    	}
		return true;
    	}catch(Exception e){
    		return true;
    	}
	}
    private void countData() {
 	    	//get data from DB
 	    	todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.selectDetail();;
				//璸衡祇布る
	 	    	while(!cursor .isLast()){
	 	    		cursor.moveToNext();   		
	 	    		count[(Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2-1]++;
	 	    	}
    }
    
}
