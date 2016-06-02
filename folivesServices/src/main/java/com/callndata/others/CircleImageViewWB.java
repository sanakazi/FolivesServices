package com.callndata.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircleImageViewWB extends ImageView {

	public CircleImageViewWB(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth();
		int h = getHeight();

		Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
		canvas.drawBitmap(roundBitmap, 0, 0, null);

	}

	public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {

		// Circular shape
		int targetWidth = radius;
		int targetHeight = radius;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = bitmap;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;

	}

}
