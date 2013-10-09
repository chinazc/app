package com.zerowire.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class RoundImage {

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) { 
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
		bitmap.getHeight(), Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 

		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		final Rect rect = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight()); 
		final RectF rectF = new RectF(rect); 
		final float roundPx = bitmap.getWidth() / 2; 
		final float roundPy = bitmap.getHeight() / 2; 

		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPy, paint); 

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
	}
}

