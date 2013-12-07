package EIProjectBeta.EIProjectBeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EIProject_Check_FromNet  extends Activity {
	private static String[] s_number; 
	private static String[] h_number;
	private static String[] LuckyNumber_temp;
	private static int sixth=5;
	private static String strQR;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_main);
        sixth=5; 
       try{
    	    strQR =getINumber();
    	    LuckyNumber_temp = strQR.split(";");
      		s_number = LuckyNumber_temp[0].split(",");
      	   	h_number = LuckyNumber_temp[1].split(",");
      	//	sixth = Integer.parseInt(s_number[0]);
      		Log.e("***sixth***",String.valueOf(sixth));
      	  
      	   	if(check()){
        		Intent intent = new Intent();	  
          	  	intent.setClass(EIProject_Check_FromNet.this, EIProject_Check_win.class);
          	  	Bundle bundle = new Bundle();
    	       	bundle.putInt("sixth", sixth);
    	       	intent.putExtras(bundle); 
         	  	startActivity(intent);
         	 	EIProject_Check_FromNet.this.finish();
        	}else{
        		Intent intent = new Intent();
    	       	intent.setClass(EIProject_Check_FromNet.this, EIProject_Check_nowin.class);
    	       	Bundle bundle = new Bundle();
    	       	bundle.putInt("sixth", sixth);
    	       	intent.putExtras(bundle);
    	       	startActivity(intent);	        	 
    	       	EIProject_Check_FromNet.this.finish();
        	}
    	  
       }catch(Exception e){
    	   Log.e("error",e.toString());
   	 	   EIProject_Check_FromNet.this.finish();
       }    
	}
	private String getINumber() {
		StringBuffer document= new StringBuffer();
		URL url;
	    StringBuffer clean = new StringBuffer();
		try {
			url = new URL("http://invoice.etax.nat.gov.tw/etaxinfo_1.htm");
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null)
				document.append(line + "\n");
				reader.close();

				boolean add = true;
				for(int i = 0 ; i < document.length() ; i++)
				{         
	                if(document.charAt(i) == '<')
	                    add = false;
	                else if(document.charAt(i) == '>')
	                    add = true;    
	                else if(add == true)
	                {
	                    clean.append(document.charAt(i));    
	                }     
                      
				}       
	 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				EIProject_Check_FromNet.this.finish();
			}
			String contant = new String(clean);
			int t1 = contant.indexOf("99年");

			String PrizeNO = String.valueOf(Integer.parseInt(contant.substring(t1+5,t1+6))/2);
			//存入特獎號碼
			int s = contant.indexOf("特獎");
			PrizeNO += "," + contant.substring(s+2, s+2+26);
		       
			//存入頭獎號碼
			int h = contant.indexOf("頭獎");
			PrizeNO += ";" + contant.substring(h+2, h+2+26);      
	   return PrizeNO.replace("、", ",");
	}
	private boolean check(){
		boolean regist=false;
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
	    			 Log.e("**luckyNumber**",s_number[i]);
	    			 Log.e("**luckyNumber**",mArrayList.get(j).substring(2));
	    			 regist = true;	
	    		 }
	    		 
	    	 }
	     }

	     for(int i=0;i<h_number.length;i++)
			{	  
			  for(int j=0;j<mArrayList.size();j++)
			  {
			    number = mArrayList.get(j);
			    for(int k=0;k<6;k++)
		    	{
			    	if (h_number[i].substring(k).equals(mArrayList.get(j).substring(k+2))) {
			    		Checkdb.update_State_Money(mArrayList.get(j), 2, k);
			    	}
		    	}
		   	}
		}
	     /*
	     for(int i=0;i<h_number.length;i++)
			{	  
			  for(int j=0;j<mArrayList.size();j++)
			  {
			    number = mArrayList.get(j);
				    	  
			    	for(int k=5;k>0;k--)
			    	 {
			    		
			    	  if(number.substring(k+2).equals(h_number[i].substring(k)))
			    	  {
			    		  Log.e("****",number.substring(k+2));
				    		Log.v("****",h_number[i].substring(k));
			    		//  winTitle.setText("中6獎");
			    		  if(number.substring(k+1).equals(h_number[i].substring(k-1))){
			    			  Log.e("****",number.substring(k+1));
					    		Log.v("****",h_number[i].substring(k-1));
			    			//  winTitle.setText("中5獎");
			    			  if(number.substring(k).equals(h_number[i].substring(k-2))){
			    				  Log.e("****",number.substring(k));
						    		Log.v("****",h_number[i].substring(k-2));
			    			//	  winTitle.setText("中4獎");
			    				  if(number.substring(k-1).equals(h_number[i].substring(k-3))){
			    					  Log.e("****",number.substring(k-1));
							    		Log.v("****",h_number[i].substring(k-3));
			    			//		  winTitle.setText("中3獎");
			    					  if(number.substring(k-2).equals(h_number[i].substring(k-4))){
			    						  Log.e("****",number.substring(k-2));
			  				    		Log.v("****",h_number[i].substring(-4));
			    				//		  winTitle.setText("中2獎");
			    						  if(number.substring(k-3).equals(h_number[i].substring(k-5))){
			    							  Log.e("****",number.substring(k-3));
			    					    		Log.v("****",h_number[i].substring(k-5));
			    				//			  winTitle.setText("中1獎");
			    			    		  }  
						    		  }  
					    		  }
				    		  }
			    		  }
			    		  
			    		  regist = true;
			    		  
			    		 
			    	  }
			    		  
			    		 
			    		
			    		
			    	  }
			    	
			    	}
			    }
			      
	*/
	
	   
		return true;
		
	}
	
	class WebFile {
	    // Saved response.
	    private java.util.Map<String,java.util.List<String>> responseHeader = null;
	    private java.net.URL responseURL = null;
	    private int responseCode = -1;
	    private String MIMEtype  = null;
	    private String charset   = null;
	    private Object content   = null; 
	    /** Open a web file. */
	    public WebFile( String urlString )
	        throws java.net.MalformedURLException, java.io.IOException {
	        // Open a URL connection.
	        final java.net.URL url = new java.net.URL( urlString );
	        final java.net.URLConnection uconn = url.openConnection( );
	        if ( !(uconn instanceof java.net.HttpURLConnection) )
	            throw new java.lang.IllegalArgumentException(
	                "URL protocol must be HTTP." );
	        final java.net.HttpURLConnection conn =  (java.net.HttpURLConnection)uconn; 
	        // Set up a request.
	        conn.setConnectTimeout( 10000 );    // 10 sec
	        conn.setReadTimeout( 10000 );       // 10 sec
	        conn.setInstanceFollowRedirects( true );
	        conn.setRequestProperty( "User-agent", "spider" ); 
	        // Send the request.
	        conn.connect( ); 
	        // Get the response.
	        responseHeader    = conn.getHeaderFields( );
	        responseCode      = conn.getResponseCode( );
	        responseURL       = conn.getURL( );
	        final int length  = conn.getContentLength( );
	        final String type = conn.getContentType( );
	        if ( type != null ) {
	            final String[] parts = type.split( ";" );
	            MIMEtype = parts[0].trim( );
	            for ( int i = 1; i < parts.length && charset == null; i++ ) {
	                final String t  = parts[i].trim( );
	                final int index = t.toLowerCase( ).indexOf( "charset=" );
	                if ( index != -1 )
	                    charset = t.substring( index+8 );
	            }
	        } 
	        // Get the content.
	        final java.io.InputStream stream = conn.getErrorStream( );
	        if ( stream != null )
	            content = readStream( length, stream );
	        else if ( (content = conn.getContent( )) != null &&
	            content instanceof java.io.InputStream )
	            content = readStream( length, (java.io.InputStream)content );
	        conn.disconnect( );
	    } 
	    /** Read stream bytes and transcode. */
	    private Object readStream( int length, java.io.InputStream stream )
	        throws java.io.IOException {
	        final int buflen = Math.max( 1024, Math.max( length, stream.available() ) );
	        byte[] buf   = new byte[buflen];;
	        byte[] bytes = null; 
	        for ( int nRead = stream.read(buf); nRead != -1; nRead = stream.read(buf) ) {
	            if ( bytes == null ) {
	                bytes = buf;
	                buf   = new byte[buflen];
	                continue;
	            }
	            final byte[] newBytes = new byte[ bytes.length + nRead ];
	            System.arraycopy( bytes, 0, newBytes, 0, bytes.length );
	            System.arraycopy( buf, 0, newBytes, bytes.length, nRead );
	            bytes = newBytes;
	        } 
	        if ( charset == null )
	            return bytes;
	        try {
	            return new String( bytes, charset );
	        }
	        catch ( java.io.UnsupportedEncodingException e ) { }
	        return bytes;
	    } 
	    /** Get the content. */
	    public Object getContent( ) {
	        return content;
	    }
	    /** Get the response code. */
	    public int getResponseCode( ) {
	        return responseCode;
	    }
	    /** Get the response header. */
	    public java.util.Map<String,java.util.List<String>> getHeaderFields( ) {
	        return responseHeader;
	    }
	    /** Get the URL of the received page. */
	    public java.net.URL getURL( ) {
	        return responseURL;
	    }
	    /** Get the MIME type. */
	    public String getMIMEType( ) {
	        return MIMEtype;
	    }
	}
	class HTMLFilterClass
	{    
	    public HTMLFilterClass(){
	    }	
	    public String filter(StringBuffer input)
	    {
	        return new String(privateHelpMethod(new String(input)));    
	    }
	    public String filter(String input)
	    {
	        return new String(privateHelpMethod(input));    
	    }   
	    private  String privateHelpMethod(String input)
	    { 
	        StringBuffer clean = new StringBuffer();
	        boolean add = true;
	        for(int i = 0 ; i < input.length() ; i++)
	        {
                if(input.charAt(i) == '<')
	                add = false;
	            else if(input.charAt(i) == '>')
	                add = true;    
	            else if(add == true)
	            {
	                clean.append(input.charAt(i));      
	            }                           
	        }       
	        return new String(clean);
	    }
	}
}
