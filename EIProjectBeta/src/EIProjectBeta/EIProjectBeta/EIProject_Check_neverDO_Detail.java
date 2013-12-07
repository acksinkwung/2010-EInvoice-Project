package EIProjectBeta.EIProjectBeta;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.app.Dialog;
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
import android.widget.ViewFlipper;

public class EIProject_Check_neverDO_Detail extends EIProjectBeta {

    private String TAG = "EIProject_Check_neverDO";
    private int [] count;
    private String [] monthCStr = {"一","二","三","四","五","六","七","八","九","十","十一","十二"};
    private int [] dayForMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
    private Map<String,String> storeName;
    private ArrayList<HashMap<String, Object>> alIData;
    private Dialog dialog;
    private TextView tvChartTitle;
    private Bitmap tempYearChart;
    private todoHistoryDB db;
    private Cursor cursor;
	// 圖表相關資訊
	int chartCurrentLevel=0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get Data from DB
        db = new todoHistoryDB(this);
        cursor = db.selectDetail();
        
        if (ViewDirect==1) {
	        setContentView(R.layout.history_main_1);  
			ListView list = (ListView) findViewById(R.id.MenuListView);
			ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
	        
	        count = new int[(new Date().getMonth()+1)/2+1];
	        //計算發票月份
	        countData();
	        //發票總數
	        int countTotal = 0;
	        countTotal = cursor.getCount();
	        
	        TextView tvHistoryMenuTitle = (TextView)findViewById(R.id.tvHistoryMenuTitle);
	        tvHistoryMenuTitle.setText("全部發票列表("+countTotal+")");
	       	for (int n=((new Date().getMonth())/2+1)-1; n>=0; n--) {
	       		HashMap<String, Object> map = new HashMap<String, Object>();  
	            map.put("ItemMenu", "99年 "+monthCStr[n*2]+"月~"+monthCStr[n*2+1]+"月("+count[n]+")");
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
					storeName = new TreeMap<String,String>() ;
			        showIDetail((new Date().getMonth()+1)/2+1-arg2-1);
					// TODO Auto-generated method stub
					
				}
			});
        }else if (ViewDirect==2) {
        	setContentView(R.layout.history_main_charts);
        	TextView tvChartTitle = (TextView) findViewById(R.id.tvChartsTitle);
        	tvChartTitle.setText("在今年每個月的花費情況");

            ImageView ivCharts = (ImageView) findViewById(R.id.ivCharts);           
            String mUrl="";
   	        mUrl = "http://chart.apis.google.com/chart?chf=bg,s,000000";
    	    mUrl+="&chxl=1:|Month|3:|Money";
    	    mUrl+="&chxs=0,C2BDDD,11.5,0,l,676767|1,C2BDDD,11.5,0,l,676767|2,FF9900,11.5,0,l,676767|3,FF9900,11.5,-0.5,l,676767";
    	    mUrl+="&chxt=x,x,y,y";
    	    mUrl+="&chs="+dm.widthPixels+"x"+dm.heightPixels;
    	    mUrl+="&cht=lc";
    	    mUrl+="&chco=E9225E";
    	    mUrl+="&chls=2";
    	    mUrl+="&chds=0,100000";
    	    mUrl+="&chma=40,20,20,30";
    	    mUrl+=getXYValue(0,0);    	        
    	    mUrl+=getData(0,0);
    	    tempYearChart = getURLBitmap(mUrl);
            ivCharts.setImageBitmap(tempYearChart);
        }
	}
	
    private String getData(int type,int argv) {
    	// 取得用於產生圖表的資料
    	String data="&chd=t:";
	    int count[] = new int[12];
		try {
	    	FileInputStream fstream = new FileInputStream( "/data/data/EIProjectBeta.EIProjectBeta/IID_Detail.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String readData;
			while ((readData = br.readLine())!=null) {
				String [] strData = readData.split(",");
				count[Integer.parseInt(strData[1].split("/")[1])-1]+=Integer.parseInt(strData[3]);	
			}
	    	in.close();
		} catch (Exception e) {
			Log.e(TAG,e.toString());
		}
		data = data + count[0]; 
		for (int n=1;n<12;n++){
			data = data + "," + count[n];
		}
    	return data;
    }
    private String getXYValue(int type,int argv) {
    	String str;
    	if (type==0) {
    		str = "&chxr=0,1,12|2,0,100000";
    	} else {
	    	int day = dayForMonth[argv];
	    	if (day==2) {
	    		
	    	}
	    	str = "&chxr=0,1,"+day+"|2,0,100000";
    	}
    	return str;
    }
    private Bitmap getURLBitmap(String urlPic) {
    	// 透過URL取得Bitmap的物件
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
	private ArrayList<HashMap<String, Object>> getIData(int selNum) {
		ArrayList<HashMap<String, Object>> alIData =  new ArrayList<HashMap<String, Object>>();
 	 
		    todoHistoryDB db = new todoHistoryDB(this);
		    Cursor cursor = db.selectDetail();
		    while(!cursor.isLast()){
		    	cursor.moveToNext();
		    	if ((Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2-1==selNum){
		    	HashMap<String, Object> map = new HashMap<String, Object>();  
		    	map.put("ItemInvoiceNumber", cursor.getString(1));
	            map.put("ItemDateTime", cursor.getString(2));
	            map.put("ItemStoreName", cursor.getString(3));
	            map.put("ItemMoney", "$"+cursor.getString(4));
	            map.put("ItemStatus", R.drawable.star);
	            alIData.add(map);
		    	}
		    }
	
        return alIData;
	}
	private void showIDetail(int selNum) {
        setContentView(R.layout.history_main_2);
		ListView list = (ListView) findViewById(R.id.IMenuListView);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		alIData = getIData(selNum);
       	for (int n=0; n<alIData.size(); n++) {
       		listItem.add(alIData.get(n));
        }
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,
	            R.layout.detail_list_items,     
	            new String[] {"ItemDateTime","ItemStoreName", "ItemInvoiceNumber","ItemMoney","ItemStatus"}, 
	            new int[] {R.id.ItemDateTime,R.id.ItemStoreName,R.id.ItemInvoiceNumber,R.id.ItemMoney,R.id.ItemStatus}
	    );
		list.setAdapter(listItemAdapter);  
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					dialog = new Dialog(EIProject_Check_neverDO_Detail.this);
	                dialog.setContentView(R.layout.detail_list_items_dialog);
               
	                int selNum = arg2;
	                TextView text = (TextView) dialog.findViewById(R.id.HistoryDialogItemInvoiceNumber);
	                text.setText((String)((alIData.get(selNum)).get("ItemInvoiceNumber").toString()));
	        		text = (TextView) dialog.findViewById(R.id.HistoryDialogItemStoreName);
	                text.setText((String)((alIData.get(selNum)).get("ItemStoreName").toString()));
	                text = (TextView) dialog.findViewById(R.id.HistoryDialogItemDateTime);
	                text.setText((String)((alIData.get(selNum)).get("ItemDateTime").toString()));
	        		text = (TextView) dialog.findViewById(R.id.HistoryDialogItemMoney);
	                text.setText((String)((alIData.get(selNum)).get("ItemMoney").toString()));
	                dialog.setCanceledOnTouchOutside(true);
	                dialog.show();
				}catch(Exception e){
					Log.e(TAG,e.toString());
				}
				// TODO Auto-generated method stub
				
			}
		});		
	}

    private void countData() {
 	    	//get data from DB
 	    	todoHistoryDB db = new todoHistoryDB(this);
		    cursor = db.selectDetail();
		    //計算發票月份
	 	    	while(!cursor.isLast()){
	 	    		cursor.moveToNext();   		
	 	    		count[(Integer.parseInt(cursor.getString(2).split("/")[1])+1)/2-1]++;
	 	    	}
    }
    
}
