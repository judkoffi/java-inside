package fr.umlv.java.inside;

import java.util.Objects;

public class StringSwitchExample {

	public static int stringSwitch(String str) {
		Objects.requireNonNull(str);

		switch (str) {
		case "foo":
			return 0;
		case "bar":
			return 1;
		case "bazz":
			return 2;
		default:
			return -1;
		}
	}
	
	public static int stringSwitch2(String str) {
		Objects.requireNonNull(str);

		switch (str) {
		case "foo":
			return 0;
		case "bar":
			return 1;
		case "bazz":
			return 2;
		default:
			return -1;
		}
	}
}
