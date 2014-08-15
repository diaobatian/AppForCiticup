package com.citi.mc.app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.citi.mc.*;

@SuppressLint("NewApi")
public class ContactUsFragment extends Fragment {
	private Button callServerBtn;
	// private Button callProdouctBtn;
	private Button iv1;
	private LinearLayout contactuslinear1, contactuslinear2, contactuslinear3,
			contactuslinear4;
	// Vibrator实现手机震动效果
	private Vibrator vibrator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contactus, container, false);

		vibrator = (Vibrator) getActivity().getSystemService(
				Service.VIBRATOR_SERVICE);

		iv1 = (Button) view.findViewById(R.id.contactback);
		callServerBtn = (Button) view.findViewById(R.id.callserver);
		contactuslinear1 = (LinearLayout) view
				.findViewById(R.id.contactuslinear1);
		contactuslinear2 = (LinearLayout) view
				.findViewById(R.id.contactuslinear2);
		contactuslinear3 = (LinearLayout) view
				.findViewById(R.id.contactuslinear3);
		contactuslinear4 = (LinearLayout) view
				.findViewById(R.id.contactuslinear4);
		contactuslinear1.setOnClickListener(onClickListener);
		contactuslinear2.setOnClickListener(onClickListener);
		contactuslinear3.setOnClickListener(onClickListener);
		contactuslinear4.setOnClickListener(onClickListener);

		MainTabActivity.flag = false;

		callServerBtn.setOnClickListener(onClickListener);

		iv1.setOnClickListener(onClickListener);
		return view;
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.callserver:

				Intent callIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + "+86 02866566666"));
				startActivity(callIntent);

				break;

			case R.id.contactback:

				FragmentManager manager = getFragmentManager();
				FragmentTransaction ft = manager.beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_out,
						R.anim.push_left_zhong);
				Fragment fragment = (Fragment) getFragmentManager()
						.findFragmentById(android.R.id.tabcontent);
				MainTabActivity.layout.setVisibility(View.VISIBLE);
				fragment = new SetFragement();
				ft.replace(android.R.id.tabcontent, fragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();

				break;
			case R.id.contactuslinear1:

				// 震动时长
				vibrator.vibrate(200);
				alertDialog("+86 02866566666");

				break;
			case R.id.contactuslinear2:

				// 震动时长
				vibrator.vibrate(200);
				alertDialog("+86 18602809765");
				
				break;
			case R.id.contactuslinear3:

				// 震动时长
				vibrator.vibrate(200);
				alertDialog("http://www.mobilechat.im");
				
				break;
			case R.id.contactuslinear4:

				// 震动时长
				vibrator.vibrate(200);
				alertDialog("成都市锦江区经天西路17号正成翡翠琉璃1栋2单元11楼");
				
				break;

			default:
				break;
			}
		}
	};
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		MainTabActivity.layout.setVisibility(View.VISIBLE);
	}
	private void alertDialog(final String text) {

		ClipboardManager copy = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		copy.setText(null);
		copy.setText(text);
		Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
	}

	private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		String LOG_TAG = "LOGGING 123";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {

				if (isPhoneCalling) {
					System.out.println("通话结束===============");
					FragmentManager manager = getFragmentManager();
					FragmentTransaction ft = manager.beginTransaction();
					ft.setCustomAnimations(R.anim.push_left_out,
							R.anim.push_left_zhong);
					Fragment fragment = (Fragment) getFragmentManager()
							.findFragmentById(android.R.id.tabcontent);
					fragment = new ContactUsFragment();
					ft.replace(android.R.id.tabcontent, fragment);
					ft.commitAllowingStateLoss();
					isPhoneCalling = false;
				}

			}
		}
	}
}
