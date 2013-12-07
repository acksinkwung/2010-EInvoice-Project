package EIProjectBeta.EIProjectBeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EIProject_Shop_Invoice extends EIProject_Shop {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_invoice);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.shopInvoiceRelativeLayout);
        layout.setBackgroundResource(R.drawable.white);
    	TextView text = (TextView) findViewById(R.id.shop_invoice_invoiceNumber);
        text.setText(IID);
        text = (TextView) findViewById(R.id.shop_invoice_datetime);
        Date date = new Date(datetime); 
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); 
        text.setText(df.format(date).toString());
        text = (TextView) findViewById(R.id.shop_invoice_money);
        text.setText("總共花費　$"+amount);
        
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}
	
}
