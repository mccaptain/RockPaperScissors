package com.mcgrath.rockpaperscissors.main;

import java.util.ArrayList;
import java.util.List;

import com.example.rockpaperscissors.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {
	int numClick = 0;
	float prevX = 0;
	float prevY = 0;
	float prev2X = 0;
	float prev2Y = 0;
	float actionX = 0;
	float actionY = 0;
	int corner = 0;
	boolean rock = false;
	boolean paper = false;
	boolean scissors = false;
	ImageView imageView;
	Bitmap bitmap;
	Canvas canvas;
	Paint paint;
	List<Double> drawingX = new ArrayList<Double>();
	List<Double> drawingY = new ArrayList<Double>();
	EditText tv;

	@InjectView( R.id.title ) TextView mTitle;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fragment_main);
//
//		tv = (EditText) findViewById(R.id.editText1);
//		imageView = (ImageView) this.findViewById(R.id.imageView1);
//
//		Display currentDisplay = getWindowManager().getDefaultDisplay();
//		float dw = currentDisplay.getWidth();
//		float dh = currentDisplay.getHeight();
//
//		bitmap = Bitmap.createBitmap((int) dw, (int) dh,
//				Bitmap.Config.ARGB_8888);
//		canvas = new Canvas(bitmap);
//		paint = new Paint();
//		paint.setColor(Color.RED);
//		imageView.setImageBitmap(bitmap);
//
//		imageView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if (numClick == 0) {
//					numClick++;
//					bitmap.eraseColor(Color.TRANSPARENT);
//					canvas.drawBitmap(bitmap, 0, 0, paint);
//					imageView.invalidate();
//				} else if (numClick == 1) {
//					prevX = actionX;
//					prevY = actionY;
//					actionX = event.getX();
//					actionY = event.getY();
//					drawingX.add((double) actionX);
//					drawingY.add((double) actionY);
//					numClick++;
//
//				} else if (numClick > 1) {
//					prev2X = prevX;
//					prev2Y = prevY;
//					prevX = actionX;
//					prevY = actionY;
//					actionX = event.getX();
//					actionY = event.getY();
//					drawingX.add((double) actionX);
//					drawingY.add((double) actionY);
//					// if(Math.abs(prevX - actionX) > 2.0 || Math.abs(prevY -
//					// actionY) > 2.0){
//
//					canvas.drawLine(prevX, prevY, actionX, actionY, paint);
//					imageView.invalidate();
//					// }
//					// numClick = 0;
//					// prevX = 0;
//					// prevY = 0;
//					// prev2X = 0;
//					// prev2Y = 0;
//					// actionX = 0;
//					// actionY = 0;
//				}
//				if (event.getActionMasked() == android.view.MotionEvent.ACTION_UP) {
//					// Toast.makeText(MainActivity.this, "Not Pressed",
//					// Toast.LENGTH_LONG).show();
//					// Toast.makeText(MainActivity.this,
//					// Integer.toString(drawingX.size()),
//					// Toast.LENGTH_LONG).show();
//
//					prevX = 0;
//					prevY = 0;
//					prev2X = 0;
//					prev2Y = 0;
//					actionX = 0;
//					actionY = 0;
//					if (drawingX.size() > 10) {
//						callShapeDetector();
//					}
//
//					corner = 0;
//					numClick = 0;
//					drawingX.clear();
//					drawingY.clear();
//
//				}
//				return true;
//			}
//		});
//
//	}

	
	
	public void callShapeDetector() {
		int index = 0;
		int incr = (int) (Math.floor(drawingX.size() / 4) * 0.7);
		while (index + 10 < drawingX.size()) {
			double prevX = drawingX.get(index);
			double actionX = drawingX.get(index + 10);
			double prevY = drawingY.get(index);
			double actionY = drawingY.get(index + 10);
			double prev2X = drawingX.get(index);
			double action2X = drawingX.get(index + 10);
			double prev2Y = drawingY.get(index);
			double action2Y = drawingY.get(index + 10);
			if (index + incr + 10 < drawingX.size()) {
				prev2X = drawingX.get(index + incr);
				action2X = drawingX.get(index + incr + 10);
				prev2Y = drawingY.get(index + incr);
				action2Y = drawingY.get(index + incr + 10);
			} else if (index + incr + 1 < drawingX.size()) {
				prev2X = drawingX.get(index + incr);
				action2X = drawingX.get(index + incr + 1);
				prev2Y = drawingY.get(index + incr);
				action2Y = drawingY.get(index + incr + 1);
			} else {
				prev2X = 0;
				action2X = 0;
				prev2Y = 0;
				action2Y = 0;
			}

			double ij = (actionX - prevX) * (action2X - prev2X)
					+ (actionY - prevY) * (action2Y - prev2Y);
			double mag1 = Math.sqrt(Math.pow((actionX - prevX), 2)
					+ Math.pow((actionY - prevY), 2));
			double mag2 = Math.sqrt(Math.pow((action2X - prev2X), 2)
					+ Math.pow((action2Y - prev2Y), 2));
			double angle = Math.acos(ij / (mag1 * mag2));
			tv.setText(Double.toString(angle));
			if (180 * angle / 3.14 < 105.1 && 180 * angle / 3.14 > 75.1) {
				corner++;
				// Toast.makeText(MainActivity.this, Integer.toString(corner),
				// Toast.LENGTH_LONG).show();
			}
			index += incr;
		}

		double prevX = drawingX.get(0);
		double actionX = drawingX.get(10);
		double prevY = drawingY.get(0);
		double actionY = drawingY.get(10);
		double prev2X = drawingX.get(drawingX.size() - 10);
		double action2X = drawingX.get(drawingX.size() - 1);
		double prev2Y = drawingY.get(drawingX.size() - 10);
		double action2Y = drawingY.get(drawingX.size() - 1);
		double ij = (actionX - prevX) * (action2X - prev2X) + (actionY - prevY)
				* (action2Y - prev2Y);
		double mag1 = Math.sqrt(Math.pow((actionX - prevX), 2)
				+ Math.pow((actionY - prevY), 2));
		double mag2 = Math.sqrt(Math.pow((action2X - prev2X), 2)
				+ Math.pow((action2Y - prev2Y), 2));
		double angle = Math.acos(ij / (mag1 * mag2));
		tv.setText(Double.toString(180 * angle / 3.14));
		if (180 * angle / 3.14 < 105.1 && 180 * angle / 3.14 > 75.1) {
			corner++;
			// Toast.makeText(MainActivity.this, Integer.toString(corner),
			// Toast.LENGTH_LONG).show();
		}
		if (corner == 4) {
			tv.setText("Paper");
			paper = true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
