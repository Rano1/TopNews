package com.topnews.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topnews.bean.ChannelItem;
import com.topnews.db.SQLHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChannelDao implements ChannelDaoInface {
	private SQLHelper helper = null;

	public ChannelDao(Context context) {
		helper = new SQLHelper(context);
	}

	@Override
	public boolean addCache(ChannelItem item) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		long id = -1;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("name", item.getName());
			values.put("id", item.getId());
			values.put("orderId", item.getOrderId());
			values.put("selected", item.getSelected());
			id = database.insert(SQLHelper.TABLE_CHANNEL, null, values);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteCache(String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLHelper.TABLE_CHANNEL, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		// TODO Auto-generated method stub
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.update(SQLHelper.TABLE_CHANNEL, values, whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public Map<String, String> viewCache(String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase database = null;
		Cursor cursor = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(true, SQLHelper.TABLE_CHANNEL, null, selection,
					selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				for (int i = 0; i < cols_len; i++) {
					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return map;
	}

	@Override
	public List<Map<String, String>> listCache(String selection,String[] selectionArgs) {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(false, SQLHelper.TABLE_CHANNEL, null, selection,selectionArgs, null, null, null, null);
			int cols_len = cursor.getColumnCount();
			while (cursor.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < cols_len; i++) {

					String cols_name = cursor.getColumnName(i);
					String cols_values = cursor.getString(cursor
							.getColumnIndex(cols_name));
					if (cols_values == null) {
						cols_values = "";
					}
					map.put(cols_name, cols_values);
				}
				list.add(map);
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	public void clearFeedTable() {
		String sql = "DELETE FROM " + SQLHelper.TABLE_CHANNEL + ";";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
		revertSeq();
	}

	private void revertSeq() {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ SQLHelper.TABLE_CHANNEL + "'";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
	}

}
