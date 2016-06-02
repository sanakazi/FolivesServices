package com.callndata.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class ObservableListView extends ListView {

	private ListViewListener listViewListener = null;

	public ObservableListView(Context context) {
		super(context);
	}

	public ObservableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ObservableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollViewListener(ListViewListener listViewListener) {
		this.listViewListener = listViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {

		View view = (View) getChildAt(getChildCount() - 1);
		int diff = (view.getBottom() - (getHeight() + getScrollY()));
		if (diff == 0) { // if diff is zero, then the bottom has been reached
			if (listViewListener != null) {
				listViewListener.onScrollEnded(this, x, y, oldx, oldy);
			}
		}
		super.onScrollChanged(x, y, oldx, oldy);
	}
}
