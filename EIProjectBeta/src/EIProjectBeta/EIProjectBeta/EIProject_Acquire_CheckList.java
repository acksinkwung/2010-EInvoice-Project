package EIProjectBeta.EIProjectBeta;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EIProject_Acquire_CheckList extends EIProject_Acquire {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.acquire_main_3);
        Log.d(TAG,receivedStr);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.acquireCertificateRelativeLayout);
        layout.setBackgroundResource(R.drawable.white);
        String [] QRCodeValue = receivedStr.split(",");
		TextView text = (TextView) findViewById(R.id.AcquireDialogItemCertificateNumber);
        text.setText(QRCodeValue[0]);
		text = (TextView) findViewById(R.id.AcquireDialogItemDateTime);
        text.setText(QRCodeValue[1]);
        text = (TextView) findViewById(R.id.AcquireDialogItemTotalMoney);
        text.setText(QRCodeValue[2]);
		text = (TextView) findViewById(R.id.AcquireDialogItemTaxMoney);
        text.setText(QRCodeValue[3]);
        saveDataToDB(QRCodeValue[1],QRCodeValue[4],QRCodeValue[2],QRCodeValue[3]);
	}
	private void saveDataToDB(String time,String IID,String money,String tax_money){
		 todoAcquireDB db=new todoAcquireDB(this);
		 Log.d("acquire",time);
		 Log.d("acquire",IID);
		 Log.d("acquire",money);
		 Log.d("acquire",tax_money);
	     db.insert_record(time,IID,money,tax_money);
	     db.update_State_Money(IID, 3, 0);
	}
}
