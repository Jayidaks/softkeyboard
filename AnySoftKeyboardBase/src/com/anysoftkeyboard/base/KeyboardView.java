/*
 * Copyright (C) 2008-2011 Menny Even Danan.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.anysoftkeyboard.base;

import android.content.Context;
import android.util.AttributeSet;

public class KeyboardView extends android.inputmethodservice.KeyboardView {

    public interface OnKeyboardActionListener extends android.inputmethodservice.KeyboardView.OnKeyboardActionListener {
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
	}
}
