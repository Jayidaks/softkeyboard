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
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

public class Keyboard extends android.inputmethodservice.Keyboard {

	public static class Row extends android.inputmethodservice.Keyboard.Row {
    	public Row(Keyboard parent) {
            super(parent);
        }
        
        public Row(Resources res, Keyboard parent, XmlResourceParser parser) {
            super(res, parent, parser);
        }
    }

    public static class Key extends android.inputmethodservice.Keyboard.Key {
        
        public Key(Row parent) {
        	super(parent);
        }
        
        public Key(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
            super(res, parent, x, y, parser);
        }
    }

    public Keyboard(Context context, int xmlLayoutResId) {
    	super(context, xmlLayoutResId);
    }
    
    public Keyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public Keyboard(Context context, int layoutTemplateResId, 
            CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
}
