package com.payment.simaspay.FlashizSDK;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.dimo.PayByQR.EULAFragmentListener;
import com.payment.simaspay.services.Utility;

import simaspay.payment.com.simaspay.R;

public class MyCustomEULA extends Fragment implements View.OnClickListener {
	private EULAFragmentListener mListener;

	public static MyCustomEULA newInstance() {
		MyCustomEULA fragment = new MyCustomEULA();
		return fragment;
	}

	public MyCustomEULA() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.qr_terms_conditions,
				container, false);

		WebView tc = (WebView) rootView.findViewById(R.id.term_cond);

		TextView disclosure = (TextView) rootView
				.findViewById(R.id.terms_conditions);
		/*
		 * disclosure.setText(Html.fromHtml(getResources().getString(
		 * R.string.flashiz_tc)));
		 */
		TextView textView = (TextView) rootView
				.findViewById(R.id.terms_conditions_1);

		disclosure
				.setText("1. Pay by QR merupakan layanan pembayaran berbasis kode QR atas pembelanjaan di pedagang/merchant yang berpartisipasi, dimana dalam pelaksanaannya pengguna UANGKU melakukan pemindaian kode QR uang tersedia pada pedagang/merchant menggunakan aplikasi UANGKU.");

		textView.setText("2. Segala ketentuan mengenai layanan pembayaran pedagang/merchant dengan Pay by QR mengacu kepada Syarat dan Ketentuan Penggunaan UANGKU.");

		disclosure.setTypeface(Utility.RegularTextFormat(getActivity()));
		textView.setTypeface(Utility.RegularTextFormat(getActivity()));

//		tc.loadUrl("file:///android_asset/flashiz_tc.html");
//		tc.setBackgroundColor(0x00000000);

		Button btnAccept = (Button) rootView.findViewById(R.id.agreeButton);
		Button btnDecline = (Button) rootView.findViewById(R.id.decline);

		btnAccept.setOnClickListener(this);
		btnDecline.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try {
			mListener = (EULAFragmentListener) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(getActivity().toString()
					+ " must implement EULAFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.agreeButton) {
			mListener.setEULAState(true);
		} else if (v.getId() == R.id.decline) {
			mListener.setEULAState(false);
		}
	}
}
