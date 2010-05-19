/*
 * Copyright (C) 2008 Esmertec AG.
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.menny.android.anysoftkeyboard.theme;

/**
 * Defines the IDs of branding resources.
 *
 */

// Most of these will change at this point. Numbering should be fixed also before use.
public interface ThemeResourceIDs {

	/**/
	/* Keyboard related*/
	/**/

	// TODO: This is enough or should we add values for popup keyboards too?
	public static final int VALUES_KEYBOARD_KEY_WIDTH = 1;
	public static final int VALUES_KEYBOARD_KEY_HEIGHT = 2;
	public static final int VALUES_KEYBOARD_VERTICAL_GAP = 3;
	public static final int VALUES_KEYBOARD_HORIZONTAL_GAP = 4;

	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_DELETE = 208;
	/**
	 * TODO: Not in use?
    */
	public static final int DRAWABLE_SYM_KEYBOARD_NUMALT = 209;
	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_RETURN = 210;
	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_SEARCH = 211;
	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_SHIFT = 212;
	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_SHIFT_LOCKED = 213;
	/**
    *
    */
	public static final int DRAWABLE_SYM_KEYBOARD_SPACE = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_TAB = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_0 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_1 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_2 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_3 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_4 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_5 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_6 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_7 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_8 = 114;
	/**
   *
   */
	public static final int DRAWABLE_SYM_KEYBOARD_NUM_9 = 114;






	/**/
	/* KeyboardView related*/
	/**/
	//todo same font used in suggestrip?
	public static final int ASSETS_FONTS_KEYBOARD_KEY_TYPEFACE = 5;

	public static final int VALUES_KEYBOARD_LABEL_TEXT_SIZE = 6;
	public static final int VALUES_KEYBOARD_KEY_TEXT_SIZE = 7;

	public static final int VALUES_KEYBOARD_KEY_TEXT_COLOR = 8;
	public static final int VALUES_KEYBOARD_KEY_SHADOW_COLOR = 9;
	//TODO: too much ?
	public static final int VALUES_KEYBOARD_POPUP_KEY_TEXT_COLOR = 10;
	public static final int VALUES_KEYBOARD_POPUP_KEY_SHADOW_COLOR = 11;


	/**
	 * TODO: allow different background for popup keyboards?
	 * Selector resource. keyBackground.
	 * Contains the following drawables
	 * btn_keyboard_key_light_pressed_on, btn_keyboard_key_light_pressed_off,
	 * btn_keyboard_key_light_normal_on, btn_keyboard_key_light_normal_off,
	 * btn_keyboard_key_light_pressed and btn_keyboard_key_light_normal
	 */
	public static final int DRAWABLE_KEYBOARD_KEY_BACKGROUND = 101;

	/**
	 * Background.
	 */
	public static final int DRAWABLE_KEYBOARD_BACKGROUND = 101;
	/**
	 * Selector resource. state_pressed and !state_pressed
	 */
	public static final int DRAWABLE_KEYBOARD_SUGGEST_SCROLL_BACKGROUND = 101;

	/**
	 * Selector resource. Source of Close ImageButton
	 */
	public static final int DRAWABLE_KEYBOARD_KEY_CLOSE = 106;
	/**
     * Background of ImageButton.
     */
	public static final int DRAWABLE_KEYBOARD_KEY_FEEDBACK_MORE_BACKGROUND = 106;
	/**
     *
     */
	public static final int DRAWABLE_KEYBOARD_KEY_FEEDBACK_BACKGROUND = 107;
	/**
	 * TODO: Not in use? Background of Popup keyboard.
	 */
	public static final int DRAWABLE_KEYBOARD_POPUP_PANEL_BACKGROUND = 119;




	/**
	   * Voice recognizition related
	   */
	public static final int DRAWABLE_DIALOG_VOICE_INPUT = 120;
	public static final int DRAWABLE_DIALOG_WAVE_0_0 = 120;
	public static final int DRAWABLE_DIALOG_WAVE_1_3 = 120;
	public static final int DRAWABLE_DIALOG_WAVE_2_3 = 120;
	public static final int DRAWABLE_DIALOG_WAVE_3_3 = 120;
	public static final int DRAWABLE_DIALOG_WAVE_4_3 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_0 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_1 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_2 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_3 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_4 = 120;
	public static final int DRAWABLE_SPEAK_NOW_LEVEL_5 = 120;
	public static final int DRAWABLE_KEYBOARD_KEY_VOICE_BACKGROUND = 120;
	public static final int DRAWABLE_KEYBOARD_KEY_VOICE_WORKING = 120;




	/**/
	/* Suggest strip */
	/**/

	public static final int VALUES_CANDIDATE_PREVIEW_TEXT_COLOR = 11;
	/**
    *
    */
	public static final int DRAWABLE_CANDIDATE_FEEDBACK_BACKGROUND = 115;
	/**
   *
   */
	public static final int DRAWABLE_CANDIDATE_HIGHLIGHT_PRESSED = 116;
	/**
   *
   */
	public static final int DRAWABLE_CANDIDATE_SUGGEST_STRIP_SCROLL_LEFT_ARROW = 117;
	/**
   *
   */
	public static final int DRAWABLE_CANDIDATE_SUGGEST_STRIP_SCROLL_RIGHT_ARROW = 118;
	/**
   *
   */
	public static final int DRAWABLE_CANDIDATE_SUGGEST_STRIP_DIVIDER = 119;
	/**
   * Background of CandidateViewContainer
   */
	public static final int DRAWABLE_CANDIDATE_SUGGEST_STRIP = 120;



	// TODO: font sizes
	// TODO: atd other drawables, possible strings etc.
	// TODO: add mode switch button strings
	// TODO: add padding etc.
	// TODO: add colors for text & shadows

}