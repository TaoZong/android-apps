package com.moreapp.demo;

import java.util.ArrayList;

import com.moreapp.demo.R;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Message> mMessages;

	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super();
		this.mContext = context;
		this.mMessages = messages;
	}

	@Override
	public int getCount() {
		return mMessages.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) this.getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.sms_list_row, parent, false);
			holder.message = (TextView) convertView
					.findViewById(R.id.message_text);
			holder.name = (TextView) convertView.findViewById(R.id.name_text);
			holder.time = (TextView) convertView.findViewById(R.id.time_text);
			convertView.setTag(holder);

			

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		
		holder.message.setText(message.message);
		holder.name.setText(message.sender_name);
		holder.time.setText(message.send_time);
		
		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();
		LayoutParams lp2 = (LayoutParams) holder.name.getLayoutParams();
		

		if (message.isMine) {
			holder.message
					.setBackgroundResource(R.drawable.speech_bubble_green);
			lp.gravity = Gravity.RIGHT;
			lp.rightMargin = 40;
			lp2.gravity = Gravity.RIGHT;
			lp2.rightMargin = 5;

		}
		// If not mine then it is from sender to show orange background and
		// align to left
		else {
			holder.message
					.setBackgroundResource(R.drawable.speech_bubble_orange);
			lp.gravity = Gravity.LEFT;
			lp.leftMargin = 40;
			lp2.gravity = Gravity.LEFT;
			lp2.leftMargin = 5;

		}
		
		
		

		return convertView;
	}

	private static class ViewHolder {
		TextView message;
		TextView time;
		TextView name;
	}

}
