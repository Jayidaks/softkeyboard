package com.menny.android.anysoftkeyboard.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import com.menny.android.anysoftkeyboard.AnyKeyboardContextProvider;
import com.menny.android.anysoftkeyboard.keyboards.ExternalAnyKeyboard;
import com.menny.android.anysoftkeyboard.keyboards.KeyboardBuildersFactory.KeyboardBuilderImpl;

public class GenericRowKeyboardTest extends InstrumentationTestCase {
	private static AnyKeyboardContextProvider contextProvider;
	private Keyboard qwertyWithGenericRowsKeyboard;
	private Keyboard qwertyWithoutGenericRowsKeyboard;

	public void setUp() throws Exception {
		super.setUp();

		contextProvider = new MockAnykeyboardContextProvider(
				getInstrumentation().getTargetContext());

		// Load english qwerty without the generic rows
		KeyboardBuilderImpl qwertyWithoutGenericRowsKeyboardBuilder = new KeyboardBuilderImpl(
				getInstrumentation().getContext(), "withoutGeneric", 0,
				R.xml.qwerty_without_generic_rows, -1, "none", 0, -1, null, "",
				1);

		// load english qwerty WITH the generic rows
		qwertyWithGenericRowsKeyboard = new ExternalAnyKeyboard(
				contextProvider, getInstrumentation().getContext(),
				R.xml.qwerty_with_generic_rows, -1, "withGeneric", 0, 0, -1,
				"none", null, false);

		qwertyWithoutGenericRowsKeyboard = qwertyWithoutGenericRowsKeyboardBuilder
				.createKeyboard(contextProvider);
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenericRowAddition() throws Throwable {

		// Test every attribute of keyboard (skipping drawables and such)
		Assert.assertNotNull(qwertyWithGenericRowsKeyboard);
		Assert.assertNotNull(qwertyWithoutGenericRowsKeyboard);

		Assert.assertEquals(qwertyWithGenericRowsKeyboard.getHeight(),
				qwertyWithoutGenericRowsKeyboard.getHeight());
		Assert.assertEquals(qwertyWithGenericRowsKeyboard.getMinWidth(),
				qwertyWithoutGenericRowsKeyboard.getMinWidth());
		Assert
				.assertTrue(keyEquals(qwertyWithGenericRowsKeyboard.getKeys()
						.get(qwertyWithGenericRowsKeyboard.getShiftKeyIndex()),
						qwertyWithoutGenericRowsKeyboard.getKeys().get(
								qwertyWithGenericRowsKeyboard
										.getShiftKeyIndex()), true));
		Assert.assertTrue(equalsKeyLists(qwertyWithGenericRowsKeyboard
				.getModifierKeys(), qwertyWithoutGenericRowsKeyboard
				.getModifierKeys()));

		int[] withNearestKeysIndices, withoutNearestKeysIndices;
		List<Key> withNearestKeys, withoutNearestKeys;
		withNearestKeysIndices = qwertyWithGenericRowsKeyboard.getNearestKeys(
				0, 0);
		withoutNearestKeysIndices = qwertyWithoutGenericRowsKeyboard
				.getNearestKeys(0, 0);
		withNearestKeys = subList(qwertyWithGenericRowsKeyboard.getKeys(),
				withNearestKeysIndices);
		withoutNearestKeys = subList(
				qwertyWithoutGenericRowsKeyboard.getKeys(),
				withoutNearestKeysIndices);

		Assert.assertTrue(equalsKeyLists(withNearestKeys, withoutNearestKeys));

		withNearestKeysIndices = qwertyWithGenericRowsKeyboard.getNearestKeys(
				5, 3);
		withoutNearestKeysIndices = qwertyWithoutGenericRowsKeyboard
				.getNearestKeys(5, 3);
		withNearestKeys = subList(qwertyWithGenericRowsKeyboard.getKeys(),
				withNearestKeysIndices);
		withoutNearestKeys = subList(
				qwertyWithoutGenericRowsKeyboard.getKeys(),
				withoutNearestKeysIndices);

		Assert.assertTrue(equalsKeyLists(withNearestKeys, withoutNearestKeys));

		Assert.assertTrue(equalsKeyLists(qwertyWithGenericRowsKeyboard
				.getKeys(), qwertyWithoutGenericRowsKeyboard.getKeys()));
	}

	private static boolean objectEquals(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null)
			return true;

		if (obj1 != null)
			return obj1.equals(obj2);
		else if (obj2 != null)
			return obj2.equals(obj1);

		// Should not get here.
		throw new IllegalStateException();
	}

	// Voluntary assertion for more logging
	private static boolean keyEquals(Key key1, Key key2, boolean doAssert) {
		if (doAssert) {
			Assert.assertTrue(Arrays.equals(key1.codes, key2.codes));

			Assert.assertEquals(key1.on, key2.on);
			Assert.assertEquals(key1.pressed, key2.pressed);
			Assert.assertEquals(key1.repeatable, key2.repeatable);
			Assert.assertEquals(key1.sticky, key2.sticky);
			Assert.assertEquals(key1.modifier, key2.modifier);
			Assert.assertEquals(key1.edgeFlags, key2.edgeFlags);
			Assert.assertEquals(key1.gap, key2.gap);
			Assert.assertEquals(key1.height, key2.height);
			Assert.assertTrue(objectEquals(key1.label, key2.label));

			Assert.assertTrue(objectEquals(key1.popupCharacters,
					key2.popupCharacters));
			Assert.assertTrue(objectEquals(key1.text, key2.text));
			Assert.assertEquals(key1.width, key2.width);
			Assert.assertEquals(key1.x, key2.x);
			Assert.assertEquals(key1.y, key2.y);
			return true;
		} else {
			return Arrays.equals(key1.codes, key2.codes)
					&& (key1.on == key2.on) && (key1.pressed == key2.pressed)
					&& (key1.repeatable == key2.repeatable)
					&& (key1.sticky == key2.sticky)
					&& (key1.modifier == key2.modifier)
					&& (key1.edgeFlags == key2.edgeFlags)
					&& (key1.gap == key2.gap) && (key1.height == key2.height)
					&& objectEquals(key1.label, key2.label)
					&& objectEquals(key1.popupCharacters, key2.popupCharacters)
					&& objectEquals(key1.text, key2.text)
					&& (key1.width == key2.width) && (key1.x == key2.x)
					&& (key1.y == key2.y);

		}

	}

	private static List<Key> subList(List<Key> list, int[] indices) {
		List<Key> keys = new ArrayList<Key>();
		for (int index : indices) {
			keys.add(list.get(index));
		}

		return keys;
	}

	// Very slow
	private static boolean equalsKeyLists(List<Key> list1, List<Key> list2) {
		if (list1.size() != list2.size())
			return false;

		Set<Integer> consumed = new HashSet<Integer>();
		for (Key key : list1) {
			int i = 0;
			boolean foundMatch = false;
			for (Key key2 : list2) {
				if (keyEquals(key, key2, false) && !consumed.contains(i)) {
					consumed.add(i);
					foundMatch = true;
					break;
				}
				i++;
			}
			if (!foundMatch)
				return false;
		}

		return true;

	}
}
