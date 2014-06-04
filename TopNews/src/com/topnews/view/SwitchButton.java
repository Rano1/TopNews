package com.topnews.view;

import com.topnews.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.View.MeasureSpec;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CompoundButton;

public class SwitchButton extends CompoundButton {
	private static final int TOUCH_MODE_IDLE = 0;
	private static final int TOUCH_MODE_DOWN = 1;
	private static final int TOUCH_MODE_DRAGGING = 2;

	private static final int SANS = 1;
	private static final int SERIF = 2;
	private static final int MONOSPACE = 3;
	/** Switch的运动轨迹 ，既背景 */
	private Drawable mThumbDrawable;
	/** Switch的=操作按钮，即开关按钮 */
	private Drawable mTrackDrawable;
	/** Switch的中现实的 on和off情况下字体大小 */
	private int mThumbTextPadding;
	/** Switch控件的最小宽度 */
	private int mSwitchMinWidth;
	/** Switch控件的padding属性值 */
	private int mSwitchPadding;
	/** Switch控件的选中情况下的字符串 */
	private CharSequence mTextOn;
	/** Switch控件的未选中情况下的字符串 */
	private CharSequence mTextOff;
	/** Switch控件的触摸时候的模式 */
	private int mTouchMode;
	private int mTouchSlop;
	private float mTouchX;
	private float mTouchY;
	private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
	private int mMinFlingVelocity;

	private float mThumbPosition;
	private int mSwitchWidth;
	private int mSwitchHeight;
	private int mThumbWidth;

	private int mSwitchLeft;
	private int mSwitchTop;
	private int mSwitchRight;
	private int mSwitchBottom;
	/** text的画笔工具 */
	private TextPaint mTextPaint;
	private ColorStateList mTextColors;
	private Layout mOnLayout;
	private Layout mOffLayout;

	private Context mContext;

	@SuppressWarnings("hiding")
	private final Rect mTempRect = new Rect();

	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

	public SwitchButton(Context context) {
		this(context, null);
		mContext = context;
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.switchStyle);
		mContext = context;
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		Resources res = getResources();
		mTextPaint.density = res.getDisplayMetrics().density;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SwitchButton, defStyle, 0);// 从配置文件中获取相关配置
		mThumbDrawable = a.getDrawable(R.styleable.SwitchButton_thumb);// 获取配置的轨迹资源
		mTrackDrawable = a.getDrawable(R.styleable.SwitchButton_track);// 获取配置的开关资源
		mTextOn = a.getText(R.styleable.SwitchButton_textOn);// 获取配置中 选中情况下的显示文字
																// on
		mTextOff = a.getText(R.styleable.SwitchButton_textOff);// 获取配置的
																// 为选中情况下的显示文字
																// off
		mThumbTextPadding = a.getDimensionPixelSize(
				R.styleable.SwitchButton_thumbTextPadding, 0);// 获取配置的按钮字体大小
		mSwitchMinWidth = a.getDimensionPixelSize(
				R.styleable.SwitchButton_switchMinWidth, 0);// 获取配置的最小宽度
		mSwitchPadding = a.getDimensionPixelSize(
				R.styleable.SwitchButton_switchPadding, 0);// 获取配置的padding属性

		int appearance = a.getResourceId(
				R.styleable.SwitchButton_switchTextAppearance, 0);// 获取配置文件中获取显示字体格式
		// 判断设定格式是否存在，存在的话就赋值
		if (appearance != 0) {
			setSwitchTextAppearance(context, appearance);
		}
		a.recycle();// 回收配置文件资源
		ViewConfiguration config = ViewConfiguration.get(context);
		mTouchSlop = config.getScaledTouchSlop();
		mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
		refreshDrawableState();
		setChecked(isChecked());
	}

	/**
	 * 设置on,off显示的字体大小及格式
	 * */
	public void setSwitchTextAppearance(Context context, int resid) {
		mContext = context;
		TypedArray appearance = context.obtainStyledAttributes(resid,
				R.styleable.TextAppearance);
		ColorStateList colors;
		int ts;
		colors = appearance
				.getColorStateList(R.styleable.TextAppearance_textColor);
		if (colors != null) {
			mTextColors = colors;
		} else {
			mTextColors = getTextColors();
		}
		ts = appearance.getDimensionPixelSize(
				R.styleable.TextAppearance_textSize, 0);
		if (ts != 0) {
			if (ts != mTextPaint.getTextSize()) {
				mTextPaint.setTextSize(ts);
				requestLayout();
			}
		}
		int typefaceIndex, styleIndex;
		typefaceIndex = appearance.getInt(R.styleable.TextAppearance_typeface,
				-1);
		styleIndex = appearance
				.getInt(R.styleable.TextAppearance_textStyle, -1);
		setSwitchTypefaceByIndex(typefaceIndex, styleIndex);
		appearance.recycle();// 回收配置文件资源
	}

	/** 根据自定义的配置属性，设置字体风格 */
	private void setSwitchTypefaceByIndex(int typefaceIndex, int styleIndex) {
		Typeface tf = null;
		switch (typefaceIndex) {
		case SANS:
			tf = Typeface.SANS_SERIF;// 默认的无衬线字体风格
			break;
		case SERIF:
			tf = Typeface.SERIF;// 默认的衬线字体风格
			break;
		case MONOSPACE:
			tf = Typeface.MONOSPACE;// 默认的等宽字体字体风格
			break;
		}
		setSwitchTypeface(tf, styleIndex);
	}

	/** 设置字体风格 */
	public void setSwitchTypeface(Typeface tf, int style) {
		if (style > 0) {
			if (tf == null) {
				tf = Typeface.defaultFromStyle(style);
			} else {
				tf = Typeface.create(tf, style);
			}
			setSwitchTypeface(tf);
			int typefaceStyle = tf != null ? tf.getStyle() : 0;
			int need = style & ~typefaceStyle;
			mTextPaint.setFakeBoldText((need & Typeface.BOLD) != 0);
			mTextPaint.setTextSkewX((need & Typeface.ITALIC) != 0 ? -0.25f : 0);
		} else {
			mTextPaint.setFakeBoldText(false);
			mTextPaint.setTextSkewX(0);
			setSwitchTypeface(tf);
		}
	}

	public void setSwitchTypeface(Typeface tf) {
		if (mTextPaint.getTypeface() != tf) {
			mTextPaint.setTypeface(tf);
			requestLayout();
			invalidate();
		}
	}

	/** 获取选中状态下的显示文字 */
	public CharSequence getTextOn() {
		return mTextOn;
	}

	/** 设定选中状态下的显示文字 */
	public void setTextOn(CharSequence textOn) {
		mTextOn = textOn;
		requestLayout();
	}

	/** 获取非选中状态下的显示文字 */
	public CharSequence getTextOff() {
		return mTextOff;
	}

	/** 设定非选中状态下的显示文字 */
	public void setTextOff(CharSequence textOff) {
		mTextOff = textOff;
		requestLayout();
	}

	/** 测量控件宽高，供绘图时使用。 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		if (mOnLayout == null) {
			mOnLayout = makeLayout(mTextOn);
		}
		if (mOffLayout == null) {
			mOffLayout = makeLayout(mTextOff);
		}
		mTrackDrawable.getPadding(mTempRect);
		final int maxTextWidth = Math.max(mOnLayout.getWidth(),
				mOffLayout.getWidth());
		final int switchWidth = Math.max(mSwitchMinWidth, maxTextWidth * 2
				+ mThumbTextPadding * 4 + mTempRect.left + mTempRect.right);
		final int switchHeight = mTrackDrawable.getIntrinsicHeight();
		mThumbWidth = maxTextWidth + mThumbTextPadding * 2;
		switch (widthMode) {
		case MeasureSpec.AT_MOST:
			widthSize = Math.min(widthSize, switchWidth);
			break;

		case MeasureSpec.UNSPECIFIED:
			widthSize = switchWidth;
			break;

		case MeasureSpec.EXACTLY:
			break;
		}
		switch (heightMode) {
		case MeasureSpec.AT_MOST:
			heightSize = Math.min(heightSize, switchHeight);
			break;
		case MeasureSpec.UNSPECIFIED:
			heightSize = switchHeight;
			break;

		case MeasureSpec.EXACTLY:
			break;
		}
		mSwitchWidth = switchWidth;
		mSwitchHeight = switchHeight;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int measuredHeight = getMeasuredHeight();
		if (measuredHeight < switchHeight) {
			setMeasuredDimension(getMeasuredWidth(), switchHeight);
		}
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		populateAccessibilityEvent(event);
		return false;
	}

	public void populateAccessibilityEvent(AccessibilityEvent event) {
		if (isChecked()) {
			CharSequence text = mOnLayout.getText();
			if (TextUtils.isEmpty(text)) {
				text = mContext.getString(R.string.switch_on);
			}
			event.getText().add(text);
		} else {
			CharSequence text = mOffLayout.getText();
			if (TextUtils.isEmpty(text)) {
				text = mContext.getString(R.string.switch_off);
			}
			event.getText().add(text);
		}
	}

	private Layout makeLayout(CharSequence text) {
		return new StaticLayout(text, mTextPaint, (int) Math.ceil(Layout
				.getDesiredWidth(text, mTextPaint)),
				Layout.Alignment.ALIGN_NORMAL, 1.f, 0, true);
	}

	/**
	 * @描述：return true 说明开关在(x,y)目标区域内
	 */
	private boolean hitThumb(float x, float y) {
		mThumbDrawable.getPadding(mTempRect);
		final int thumbTop = mSwitchTop - mTouchSlop;
		final int thumbLeft = mSwitchLeft + (int) (mThumbPosition + 0.5f)
				- mTouchSlop;
		final int thumbRight = thumbLeft + mThumbWidth + mTempRect.left
				+ mTempRect.right + mTouchSlop;
		final int thumbBottom = mSwitchBottom + mTouchSlop;
		return x > thumbLeft && x < thumbRight && y > thumbTop
				&& y < thumbBottom;
	}

	/** 传递触摸屏触摸事件 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		mVelocityTracker.addMovement(ev);
		final int action = ev.getActionMasked();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();
			if (isEnabled() && hitThumb(x, y)) {
				mTouchMode = TOUCH_MODE_DOWN;
				mTouchX = x;
				mTouchY = y;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			switch (mTouchMode) {
			case TOUCH_MODE_IDLE:
				return true;
			case TOUCH_MODE_DOWN: {
				final float x = ev.getX();
				final float y = ev.getY();
				if (Math.abs(x - mTouchX) > mTouchSlop
						|| Math.abs(y - mTouchY) > mTouchSlop) {
					mTouchMode = TOUCH_MODE_DRAGGING;
					getParent().requestDisallowInterceptTouchEvent(true);
					mTouchX = x;
					mTouchY = y;
					return true;
				}
				break;
			}
			case TOUCH_MODE_DRAGGING: {
				final float x = ev.getX();
				final float dx = x - mTouchX;
				float newPos = Math.max(0,
						Math.min(mThumbPosition + dx, getThumbScrollRange()));
				if (newPos != mThumbPosition) {
					mThumbPosition = newPos;
					mTouchX = x;
					invalidate();
				}
				return true;
			}
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			if (mTouchMode == TOUCH_MODE_DRAGGING) {
				stopDrag(ev);
				return true;
			}
			mTouchMode = TOUCH_MODE_IDLE;
			mVelocityTracker.clear();
			break;
		}
		}
		return super.onTouchEvent(ev);
	}

	private void cancelSuperTouch(MotionEvent ev) {
		MotionEvent cancel = MotionEvent.obtain(ev);
		cancel.setAction(MotionEvent.ACTION_CANCEL);
		super.onTouchEvent(cancel);
		cancel.recycle();
	}

	private void stopDrag(MotionEvent ev) {
		mTouchMode = TOUCH_MODE_IDLE;

		boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP
				&& isEnabled();

		cancelSuperTouch(ev);

		if (commitChange) {
			boolean newState;
			mVelocityTracker.computeCurrentVelocity(1000);
			float xvel = mVelocityTracker.getXVelocity();
			if (Math.abs(xvel) > mMinFlingVelocity) {
				newState = xvel > 0;
			} else {
				newState = getTargetCheckedState();
			}
			animateThumbToCheckedState(newState);
		} else {
			animateThumbToCheckedState(isChecked());
		}
	}

	private void animateThumbToCheckedState(boolean newCheckedState) {

		setChecked(newCheckedState);
	}

	private boolean getTargetCheckedState() {
		return mThumbPosition >= getThumbScrollRange() / 2;
	}

	// 设置选中的状态（选中:true 非选中: false）
	@Override
	public void setChecked(boolean checked) {
		super.setChecked(checked);
		mThumbPosition = checked ? getThumbScrollRange() : 0;
		invalidate();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		mThumbPosition = isChecked() ? getThumbScrollRange() : 0;

		int switchRight = getWidth() - getPaddingRight();
		int switchLeft = switchRight - mSwitchWidth;
		int switchTop = 0;
		int switchBottom = 0;
		switch (getGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
		default:
		case Gravity.TOP:
			switchTop = getPaddingTop();
			switchBottom = switchTop + mSwitchHeight;
			break;

		case Gravity.CENTER_VERTICAL:
			switchTop = (getPaddingTop() + getHeight() - getPaddingBottom())
					/ 2 - mSwitchHeight / 2;
			switchBottom = switchTop + mSwitchHeight;
			break;

		case Gravity.BOTTOM:
			switchBottom = getHeight() - getPaddingBottom();
			switchTop = switchBottom - mSwitchHeight;
			break;
		}

		mSwitchLeft = switchLeft;
		mSwitchTop = switchTop;
		mSwitchBottom = switchBottom;
		mSwitchRight = switchRight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int switchLeft = mSwitchLeft;
		int switchTop = mSwitchTop;
		int switchRight = mSwitchRight;
		int switchBottom = mSwitchBottom;
		mTrackDrawable.setBounds(switchLeft, switchTop, switchRight,
				switchBottom);
		mTrackDrawable.draw(canvas);

		canvas.save();

		mTrackDrawable.getPadding(mTempRect);
		int switchInnerLeft = switchLeft + mTempRect.left;
		int switchInnerTop = switchTop + mTempRect.top;
		int switchInnerRight = switchRight - mTempRect.right;
		int switchInnerBottom = switchBottom - mTempRect.bottom;
		canvas.clipRect(switchInnerLeft, switchTop, switchInnerRight,
				switchBottom);

		mThumbDrawable.getPadding(mTempRect);
		final int thumbPos = (int) (mThumbPosition + 0.5f);
		int thumbLeft = switchInnerLeft - mTempRect.left + thumbPos;
		int thumbRight = switchInnerLeft + thumbPos + mThumbWidth
				+ mTempRect.right;

		mThumbDrawable
				.setBounds(thumbLeft, switchTop, thumbRight, switchBottom);
		mThumbDrawable.draw(canvas);

		if (mTextColors != null) {
			mTextPaint.setColor(mTextColors.getColorForState(
					getDrawableState(), mTextColors.getDefaultColor()));
		}
		mTextPaint.drawableState = getDrawableState();

		Layout switchText = getTargetCheckedState() ? mOnLayout : mOffLayout;

		canvas.translate(
				(thumbLeft + thumbRight) / 2 - switchText.getWidth() / 2,
				(switchInnerTop + switchInnerBottom) / 2
						- switchText.getHeight() / 2);
		switchText.draw(canvas);

		canvas.restore();
	}

	@Override
	public int getCompoundPaddingRight() {
		int padding = super.getCompoundPaddingRight() + mSwitchWidth;
		if (!TextUtils.isEmpty(getText())) {
			padding += mSwitchPadding;
		}
		return padding;
	}

	private int getThumbScrollRange() {
		if (mTrackDrawable == null) {
			return 0;
		}
		mTrackDrawable.getPadding(mTempRect);
		return mSwitchWidth - mThumbWidth - mTempRect.left - mTempRect.right;
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		int[] myDrawableState = getDrawableState();
		if (mThumbDrawable != null)
			mThumbDrawable.setState(myDrawableState);
		if (mTrackDrawable != null)
			mTrackDrawable.setState(myDrawableState);
		invalidate();
	}

	@Override
	protected boolean verifyDrawable(Drawable who) {
		return super.verifyDrawable(who) || who == mThumbDrawable
				|| who == mTrackDrawable;
	}

}
