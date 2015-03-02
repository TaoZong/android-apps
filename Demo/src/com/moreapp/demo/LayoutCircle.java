package com.moreapp.demo;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.moreapp.demo.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class LayoutCircle extends Fragment {
	View root;
	final String[] types = { "Group", "Contact" };
	ImageButton ibt1, ibt2;
	String resString, url;
	Bitmap d = null;
	ArrayList<Contact> contacts;
	ArrayList<Group> groups;
	ListView listview;
	Spinner spinner;
	boolean editflag = false, flag = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_layout_circle, container,
				false);
		ibt1 = (ImageButton) root.findViewById(R.id.imageButton1);
		ibt2 = (ImageButton) root.findViewById(R.id.imageButton2);
		listview = (ListView) root.findViewById(R.id.listView1);
		listview.setDividerHeight(1);
		spinner = (Spinner) root.findViewById(R.id.spinner1);
		
		
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		setGroupList();
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (types[pos].equals("Group")) {
					flag = false;
					d = null;
					listview.setAdapter(null);
					listview.setDividerHeight(1);
					contacts = new ArrayList<Contact>();
					ibt1.setImageResource(R.drawable.add);
					setGroupList();
				} else if (types[pos].equals("Contact")) {
					flag = true;
					d = null;
					listview.setAdapter(null);
					listview.setDividerHeight(0);
					contacts = new ArrayList<Contact>();
					ibt1.setImageResource(R.drawable.edit);
					setContactList();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ibt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					if (editflag) {
						ibt1.setImageResource(R.drawable.edit);
						editflag = false;
						listview.invalidateViews();
					} else {
						ibt1.setImageResource(R.drawable.save);
						editflag = true;
						listview.invalidateViews();
					}
				} else {
					createNewGroup();
				}
			}
		});

		ibt2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					Intent intent = new Intent(getActivity(),
							SearchContact.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							SearchGroup.class);
					startActivity(intent);
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(!flag) {
					String cid = groups.get(position).id;
					boolean isAdmin = groups.get(position).isAdmin;
					boolean isFounder = groups.get(position).isFounder;
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							GroupMessage.class);
					Bundle bundle = new Bundle();
					bundle.putString("cid", cid);
					bundle.putBoolean("isAdmin", isAdmin);
					bundle.putBoolean("isFounder", isFounder);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				else {
					String uid = contacts.get(position).id;
					String name = contacts.get(position).full_name;
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							ContactMessage.class);
					Bundle bundle = new Bundle();
					bundle.putString("uid", uid);
					bundle.putString("name", name);
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
			}

		});

		return root;
	}

	void setGroupList() {
		groups = new ArrayList<Group>();
		listview.setAdapter(null);	
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/sociality/circle/index");
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("CIRCLE LIST STATUS:", resString);
				} catch (Exception e) {

				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {

		}
		JSONObject jobject;
		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray = jobject.getJSONArray("circles");
				if (jarray.length() > 0) {
					for (int i = 0; i < jarray.length(); i++) {
						JSONArray jarray2 = jarray.getJSONObject(i)
								.getJSONArray("admins");
						String id = jarray.getJSONObject(i).getString("id");
						String full_name = jarray.getJSONObject(i).getString(
								"full_name");
						boolean isFounder = jarray.getJSONObject(i).getBoolean(
								"is_founder");
						boolean isAdmin = jarray.getJSONObject(i).getBoolean(
								"is_admin");
						Member[] members = new Member[jarray2.length()];
						if (jarray2.length() > 0) {
							for (int j = 0; j < jarray2.length(); j++) {
								String id2 = jarray2.getJSONObject(j)
										.getString("id");
								url = jarray2.getJSONObject(j).getString(
										"avatar100");
								String full_name2 = jarray2.getJSONObject(j)
										.getString("full_name");
								boolean isFounder2 = jarray2.getJSONObject(j)
										.getBoolean("is_founder");
								boolean isAdmin2 = jarray2.getJSONObject(j)
										.getBoolean("is_admin");
								d = null;
								Thread t2 = new Thread(new Runnable() {
									public void run() {
										try {
											InputStream is = (InputStream) new URL(
													url).getContent();
											d = BitmapFactory.decodeStream(is);
											is.close();
										} catch (Exception e) {
											Log.d("EXCEPTION:", e.toString());
										}
									}
								});

								t2.start();
								try {
									t2.join();
								} catch (InterruptedException e) {

								}

								Member m = new Member(id2, full_name2, d,
										isFounder2, isAdmin2);
								members[j] = m;
							}
						}

						Group g = new Group(id, full_name, members, isFounder,
								isAdmin);
						groups.add(g);

					}
				}
			}
		} catch (Exception e) {

		}
		GroupAdapter adapter = new GroupAdapter(getActivity(),
				R.layout.group_list_row, groups);
		listview = (ListView) root.findViewById(R.id.listView1);
		listview.setAdapter(adapter);

	}

	public void setContactList() {
		contacts = new ArrayList<Contact>();
		listview.setAdapter(null);
		Thread t = new Thread(new Runnable() {
			public void run() {
				HttpGet httpget = new HttpGet(SignUpAndSignIn.urlHead
						+ "/sociality/contact/index");
				try {
					HttpResponse response = SignUpAndSignIn.client
							.execute(httpget);
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);
					Log.d("CONTACT LIST STATUS:", resString);
				} catch (Exception e) {

				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {

		}
		JSONObject jobject;

		try {
			jobject = new JSONObject(resString);
			if (jobject.get("status").toString().equals("success")) {
				JSONArray jarray1 = jobject.getJSONArray("requests");
				JSONArray jarray2 = jobject.getJSONArray("contacts");

				if (jarray1.length() > 0) {
					String id, full_name, relationship;
					for (int i = 0; i < jarray1.length(); i++) {
						id = jarray1.getJSONObject(i).getString("id");
						full_name = jarray1.getJSONObject(i).getString(
								"full_name");
						relationship = jarray1.getJSONObject(i)
								.getString("tag");
						url = jarray1.getJSONObject(i).getString("avatar100");
						d = null;
						if (!url.equals("no_avatar")) {
							Thread t2 = new Thread(new Runnable() {
								public void run() {
									try {
										InputStream is = (InputStream) new URL(
												url).getContent();
										d = BitmapFactory.decodeStream(is);
										is.close();
									} catch (Exception e) {
										Log.d("EXCEPTION:", e.toString());
									}
								}
							});

							t2.start();
							try {
								t2.join();
							} catch (InterruptedException e) {

							}
						}

						Contact c = new Contact(d, id, full_name, null,
								relationship);
						contacts.add(c);
					}
				}

				if (jarray2.length() > 0) {
					String id, full_name, relationship;
					for (int i = 0; i < jarray2.length(); i++) {
						id = jarray2.getJSONObject(i).getString("id");
						full_name = jarray2.getJSONObject(i).getString(
								"full_name");
						relationship = jarray2.getJSONObject(i)
								.getString("tag");
						url = jarray2.getJSONObject(i).getString("avatar100");
						d = null;
						if (!url.equals("no_avatar")) {
							Thread t2 = new Thread(new Runnable() {
								public void run() {
									try {
										InputStream is = (InputStream) new URL(
												url).getContent();
										d = BitmapFactory.decodeStream(is);
										is.close();
									} catch (Exception e) {
										Log.d("EXCEPTION:", e.toString());
									}
								}
							});

							t2.start();
							try {
								t2.join();
							} catch (InterruptedException e) {

							}
						}

						Contact c = new Contact(d, id, full_name, relationship,
								null);
						contacts.add(c);
					}
				}
			}
		} catch (Exception e) {

		}

		ContactAdapter adapter = new ContactAdapter(getActivity(),
				R.layout.contact_list_row, contacts);
		listview = (ListView) root.findViewById(R.id.listView1);
		listview.setAdapter(adapter);
	}

	public class Contact {
		Bitmap avatar;
		String id;
		String full_name;
		String relationship;
		String tag;

		public Contact(Bitmap avatar, String id, String full_name,
				String relationship, String tag) {
			this.avatar = avatar;
			this.id = id;
			this.full_name = full_name;
			this.relationship = relationship;
			this.tag = tag;
		}
	}

	public class ContactHolder {
		ImageView img;
		TextView full_name;
		TextView relationship;
		Button bt1, bt2, bt3;
	}

	public class ContactAdapter extends ArrayAdapter<Contact> {
		Context context;
		int layoutResourceId;
		ArrayList<Contact> data;

		public ContactAdapter(Context context, int layoutResourceId,
				ArrayList<Contact> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			ContactHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new ContactHolder();
				holder.img = (ImageView) row.findViewById(R.id.imageView1);
				holder.full_name = (TextView) row.findViewById(R.id.textView1);
				holder.relationship = (TextView) row
						.findViewById(R.id.textView2);
				holder.bt1 = (Button) row.findViewById(R.id.button1);
				holder.bt2 = (Button) row.findViewById(R.id.button2);
				holder.bt3 = (Button) row.findViewById(R.id.button3);
				row.setTag(holder);

				final Contact p = data.get(position);
				if (p.avatar != null)
					holder.img.setImageBitmap(p.avatar);
				holder.full_name.setText(p.full_name);
			} else {
				holder = (ContactHolder) row.getTag();
			}

			final Contact p = data.get(position);
			if (p.relationship != null) {
				if (editflag)
					holder.bt3.setVisibility(View.VISIBLE);
				else
					holder.bt3.setVisibility(View.GONE);
				holder.relationship.setText(p.relationship);
				holder.bt1.setVisibility(View.GONE);
				holder.bt2.setVisibility(View.GONE);
			} else {
				holder.relationship.setText("requiring to add you as " + p.tag);
				holder.bt1.setVisibility(View.VISIBLE);
				holder.bt2.setVisibility(View.VISIBLE);
				holder.bt3.setVisibility(View.GONE);
			}
			holder.bt2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					acceptRequest(p.id);
					data.get(position).relationship = p.tag;
					notifyDataSetChanged();
				}
			});

			holder.bt1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					declineRequest(p.id);
					data.remove(position);
					notifyDataSetChanged();

				}
			});

			holder.bt3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeContact(p.id);
					data.remove(position);
					notifyDataSetChanged();
				}
			});

			return row;

		}

	}

	void acceptRequest(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/contact/accept");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("ACCEPT REQUEST STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	void declineRequest(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/contact/decline");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DECLINE REQUEST STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	void removeContact(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/contact/remove");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("uid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("REMOVE REQUEST STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}

	void createNewGroup() {
		final Dialog d = new Dialog(getActivity());
		d.setTitle("Enter your group name");
		d.setContentView(R.layout.dialog_new_group);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final EditText txt = (EditText) d.findViewById(R.id.editText1);
        
		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String inputName = txt.getText().toString();
				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
									+ "/sociality/circle/create");

							MultipartEntityBuilder entity = MultipartEntityBuilder
									.create();
							entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
							entity.addTextBody("full_name", inputName, ContentType.APPLICATION_JSON);
							post.setEntity(entity.build());
							HttpResponse response = SignUpAndSignIn.client
									.execute(post);

							// Get response
							HttpEntity resEntity = response.getEntity();
							resString = EntityUtils.toString(resEntity);

							Log.d("DISMISS GROUP STATUS:", resString);
						} catch (Exception e) {
							// e.printStackTrace();
						}

					}

				});
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
				setGroupList();
				txt.setVisibility(View.GONE);
				d.dismiss();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				txt.setVisibility(View.GONE);
				d.dismiss();
			}
		});
		d.show();

	}

	class Group {
		String id;
		String name;
		Member[] members;
		ArrayList<Member> admins;
		boolean isFounder;
		boolean isAdmin;

		Group(String id, String name, Member[] members, boolean isFounder,
				boolean isAdmin) {
			this.id = id;
			this.name = name;
			this.members = members;
			this.isFounder = isFounder;
			this.isAdmin = isAdmin;
			admins = new ArrayList<Member>();
			if (members.length > 0) {
				for (int i = 0; i < members.length; i++) {
					if (members[i].isAdmin) {
						admins.add(members[i]);
					}
				}
			}
		}
	}

	class Member {
		String id;
		String name;
		Bitmap avatar;
		boolean isFounder;
		boolean isAdmin;

		Member(String id, String name, Bitmap avatar, boolean isFounder,
				boolean isAdmin) {
			this.id = id;
			this.name = name;
			this.avatar = avatar;
			this.isFounder = isFounder;
			this.isAdmin = isAdmin;
		}
	}

	public class GroupHolder {
		ImageView img1, img2, img3, img4, img5, img6;
		TextView full_name;
		Button bt1, bt2;
	}

	public class GroupAdapter extends ArrayAdapter<Group> {
		Context context;
		int layoutResourceId;
		ArrayList<Group> data;

		public GroupAdapter(Context context, int layoutResourceId,
				ArrayList<Group> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			GroupHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new GroupHolder();
				holder.full_name = (TextView) row.findViewById(R.id.textView1);
				holder.img1 = (ImageView) row.findViewById(R.id.imageView01);
				holder.img2 = (ImageView) row.findViewById(R.id.imageView02);
				holder.img3 = (ImageView) row.findViewById(R.id.imageView03);
				holder.img4 = (ImageView) row.findViewById(R.id.imageView04);
				holder.img5 = (ImageView) row.findViewById(R.id.imageView05);
				holder.img6 = (ImageView) row.findViewById(R.id.imageView06);
				holder.bt1 = (Button) row.findViewById(R.id.button1);
				holder.bt2 = (Button) row.findViewById(R.id.button2);
				row.setTag(holder);
			} else {
				holder = (GroupHolder) row.getTag();
			}
			final Group g = data.get(position);
			holder.full_name.setText(g.name);
			int num = g.admins.size();
			holder.img1.setImageBitmap(null);
			holder.img2.setImageBitmap(null);
			holder.img3.setImageBitmap(null);
			holder.img4.setImageBitmap(null);
			holder.img5.setImageBitmap(null);
			holder.img6.setImageBitmap(null);
			if (num > 0) {
				if(g.admins.get(0).avatar != null) holder.img1.setImageBitmap(g.admins.get(0).avatar);
				else holder.img1.setImageResource(R.drawable.profilepic);
				if (num > 1) {
					if(g.admins.get(1).avatar != null) holder.img2.setImageBitmap(g.admins.get(1).avatar);
					else holder.img2.setImageResource(R.drawable.profilepic);
					if (num > 2) {
						if(g.admins.get(2).avatar != null) holder.img3.setImageBitmap(g.admins.get(2).avatar);
						else holder.img3.setImageResource(R.drawable.profilepic);
						if (num > 3) {
							if(g.admins.get(3).avatar != null) holder.img4.setImageBitmap(g.admins.get(3).avatar);
							else holder.img4.setImageResource(R.drawable.profilepic);
							if (num > 4) {
								if(g.admins.get(4).avatar != null) holder.img5.setImageBitmap(g.admins.get(4).avatar);
								else holder.img5.setImageResource(R.drawable.profilepic);
								if (num > 5) {
									if(g.admins.get(5).avatar != null) holder.img6.setImageBitmap(g.admins.get(5).avatar);
									else holder.img6.setImageResource(R.drawable.profilepic);
								}
							}
						}
					}
				}
			}
			if (g.isFounder) {
				holder.bt1.setVisibility(View.VISIBLE);
				holder.bt2.setVisibility(View.GONE);
			} else {
				holder.bt1.setVisibility(View.GONE);
				holder.bt2.setVisibility(View.VISIBLE);
			}
			holder.bt1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismissGroup(g.id);
					data.remove(g);
					notifyDataSetChanged();
				}
			});

			holder.bt2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					quitGroup(g.id);
					data.remove(g);
					notifyDataSetChanged();
				}
			});

			return row;

		}
	}

	void dismissGroup(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/disband");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("DISMISS GROUP STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}
	
	void quitGroup(final String id) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					HttpPost post = new HttpPost(SignUpAndSignIn.urlHead
							+ "/sociality/circle/quit");

					MultipartEntityBuilder entity = MultipartEntityBuilder
							.create();
					entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addTextBody("cid", id, ContentType.APPLICATION_JSON);
					post.setEntity(entity.build());
					HttpResponse response = SignUpAndSignIn.client
							.execute(post);

					// Get response
					HttpEntity resEntity = response.getEntity();
					resString = EntityUtils.toString(resEntity);

					Log.d("QUIT GROUP STATUS:", resString);
				} catch (Exception e) {
					// e.printStackTrace();
				}

			}

		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}
	
	public void onStart () {
		super.onStart();
		if(!flag) setGroupList();
	}
	
	public void onResume () {
		super.onResume();
		if(!flag) setGroupList();
	}

}
