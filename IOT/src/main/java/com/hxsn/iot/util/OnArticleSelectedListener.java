package com.hxsn.iot.util;

import android.os.Bundle;

public interface OnArticleSelectedListener {
	public void onArticleSelected(String topFragment, String fragment, Bundle bundle);
	public void onArticleSelectedBack(String fragment, Bundle bundle);
}
