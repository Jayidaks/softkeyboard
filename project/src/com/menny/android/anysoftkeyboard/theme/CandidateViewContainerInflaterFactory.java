package com.menny.android.anysoftkeyboard.theme;

import com.menny.android.anysoftkeyboard.CandidateView;
import com.menny.android.anysoftkeyboard.CandidateViewContainer;
import com.menny.android.anysoftkeyboard.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater.Factory;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CandidateViewContainerInflaterFactory implements Factory {

	private ThemeResources mResources;
	private static final String CANDIDATE_VIEW_CONTAINER_TAG = "com.menny.android.anysoftkeyboard.CandidateViewContainer";
	private static final String CANDIDATE_VIEW_TAG = "com.menny.android.anysoftkeyboard.CandidateView";
	private static final String CANDIDATE_VIEW_LEFT_CONTAINER_TAG = "com.menny.android.anysoftkeyboard.theme.CandidateViewLeftLinearLayout";
	private static final String CANDIDATE_VIEW_RIGHT_CONTAINER_TAG = "com.menny.android.anysoftkeyboard.theme.CandidateViewRightLinearLayout";
	private static final String SUGGEST_STRIP_LEFT_ARROW_IMAGE_BUTTON_TAG = "com.menny.android.anysoftkeyboard.theme.SuggestStripLeftArrowImageButton";
	private static final String SUGGEST_STRIP_RIGHT_ARROW_IMAGE_BUTTON_TAG = "com.menny.android.anysoftkeyboard.theme.SuggestStripRightArrowImageButton";
	private static final String SUGGEST_STRIP_DIVIDER_IMAGE_VIEW_TAG = "com.menny.android.anysoftkeyboard.theme.SuggestStripDividerImageView";
	private static final String LAYOUT_WIDTH_ATTRIBUTE_NAME = "layout_width";
	private static final String LAYOUT_HEIGHT_ATTRIBUTE_NAME = "layout_height";

	public CandidateViewContainerInflaterFactory(ThemeResources resources) {
		mResources = resources;

	}

	public View onCreateView(String name, Context context, AttributeSet attrs) {
		if (CANDIDATE_VIEW_CONTAINER_TAG.equals(name)) {
			CandidateViewContainer candidateView = new CandidateViewContainer(
					context, attrs);
			if (candidateView.getBackground() == null) {
				candidateView
						.setBackgroundDrawable(mResources
								.getDrawable(R.id.theme_drawableKeyboardSuggestStripBackground));
			}
			return candidateView;

		} else if (SUGGEST_STRIP_LEFT_ARROW_IMAGE_BUTTON_TAG.equals(name)) {
			ImageButton button = new ImageButton(context, attrs);
			if (button.getDrawable() == null) {
				button
						.setImageDrawable(mResources
								.getDrawable(R.id.theme_drawableIcSuggestStripScrollLeftArrow));
			}
			if (button.getBackground() == null) {
				button
						.setBackgroundDrawable(mResources
								.getDrawable(R.id.theme_drawableIcSuggestScrollBackground));
			}
			// We need to find the button later! We cannot use attribute id
			// since
			// the layout might come from other package
			button.setId(R.id.candidate_left);
			if (!Utils.isAttributeDefined(attrs, LAYOUT_WIDTH_ATTRIBUTE_NAME)) {
				LayoutParams params = new ViewGroup.LayoutParams(
						Math
								.round(mResources
										.getDimension(
												R.id.theme_dimensionIcSuggestStripScrollLeftArrowWidth,
												36)), LayoutParams.FILL_PARENT);
				if (button.getLayoutParams() != null) {
					//TODO: layoutParams might be null at this stage!
					params.height = button.getLayoutParams().height;
				}
				button.setLayoutParams(params);

			}
			return button;

		} else if (SUGGEST_STRIP_RIGHT_ARROW_IMAGE_BUTTON_TAG.equals(name)) {
			ImageButton button = new ImageButton(context, attrs);
			if (button.getDrawable() == null) {
				button
						.setImageDrawable(mResources
								.getDrawable(R.id.theme_drawableIcSuggestStripScrollRightArrow));
			}
			if (button.getBackground() == null) {
				button
						.setBackgroundDrawable(mResources
								.getDrawable(R.id.theme_drawableIcSuggestScrollBackground));
			}
			// We need to find the button later! We cannot use attribute id
			// since
			// the layout might come from other package
			button.setId(R.id.candidate_right);
			if (!Utils.isAttributeDefined(attrs, LAYOUT_WIDTH_ATTRIBUTE_NAME)) {
				LayoutParams params = new ViewGroup.LayoutParams(
						Math
								.round(mResources
										.getDimension(
												R.id.theme_dimensionIcSuggestStripScrollRightArrowWidth,
												36)), LayoutParams.FILL_PARENT);
				if (button.getLayoutParams() != null) {
					//TODO: layoutParams might be null at this stage!
					params.height = button.getLayoutParams().height;
				}
				button.setLayoutParams(params);
			}

			return button;

		} else if (CANDIDATE_VIEW_TAG.equals(name)) {
			CandidateView view = new CandidateView(context, mResources, attrs);
			if (!Utils.isAttributeDefined(attrs, LAYOUT_HEIGHT_ATTRIBUTE_NAME)) {
				LayoutParams params = new ViewGroup.LayoutParams(
						LayoutParams.WRAP_CONTENT,
						Math.round(mResources.getDimension(
								R.id.theme_dimensionCandidateViewLayoutHeight,
								38)));
				if (view.getLayoutParams() != null) {
					//TODO: layoutParams might be null at this stage!
					params.width = view.getLayoutParams().width;
				}
				view.setLayoutParams(params);
			}

			// We need to find the candidate view later! We cannot rely on
			// attribute id
			// since
			// the layout might come from other package
			view.setId(R.id.candidates);
			return view;

		} else if (SUGGEST_STRIP_DIVIDER_IMAGE_VIEW_TAG.equals(name)) {
			ImageView imageView = new ImageView(context, attrs);
			if (imageView.getDrawable() == null) {
				imageView.setImageDrawable(mResources
						.getDrawable(R.id.theme_drawableIcSuggestStripDivider));
			}
			return imageView;

		} else if (CANDIDATE_VIEW_LEFT_CONTAINER_TAG.equals(name)) {
			LinearLayout layout = new LinearLayout(context, attrs);
			// We need to find the view later! We cannot use attribute id
			// since
			// the layout might come from other package
			layout.setId(R.id.candidate_left_parent);
			return layout;

		} else if (CANDIDATE_VIEW_RIGHT_CONTAINER_TAG.equals(name)) {
			LinearLayout layout = new LinearLayout(context, attrs);
			// We need to find the button later! We cannot use attribute id
			// since
			// the layout might come from other package
			layout.setId(R.id.candidate_right_parent);
			return layout;
		}

		return null;
	}

}
