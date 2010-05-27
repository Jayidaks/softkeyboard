
/**
 * 
 * call wit each row as argument. codes/letters
 * <code>java TemplateGenerator "a b,c d e f" "333 334 667,44"
 * 
 * @author lado
 * 
 *TODO some rules to generate popupCharacters and keyLabel
 * 
 */
public class TemplateGenerator {

	public static final String HEADER_STRING = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ "\n"
			+ "<Keyboard xmlns:android=\"http://schemas.android.com/apk/res/android\"\n"
			+ "    android:keyWidth=\"10%p\"\n"
			+ "    android:horizontalGap=\"0px\"\n"
			+ "    android:verticalGap=\"0px\"\n"
			+ "    android:keyHeight=\"@dimen/key_height\">";

	public static final String FOOTER_STRING = "</Keyboard>";

	public static void main(String[] args) {

		StringBuilder sb = new StringBuilder();
		sb.append(HEADER_STRING).append("\n");

		for (String arg : args) {
			sb.append("<Row>").append("\n");
			boolean firstKey = true;
			boolean lastKey = false;
			String[] keys = arg.split(" ");
			for (int i = 0; i < keys.length; ++i) {
				if (i == keys.length - 1) {
					lastKey = true;
				}

				String codes = keys[i];
				sb
						.append("    <Key android:codes=\""
								+ codes
								+ "\" android:popupCharacters=\"\" android:keyLabel=\"\"");//

				if (firstKey) {
					sb.append(" android:keyEdgeFlags=\"left\" ");
					firstKey = false;
				} else if (lastKey) {
					sb.append(" android:keyEdgeFlags=\"right\" ");
					lastKey = false;
				}
				sb.append("/>").append("\n");

			}

			sb.append("</Row>").append("\n");
		}

		sb.append(FOOTER_STRING);
		System.out.println(sb.toString());

	}

}
