package openrecordz.scripting;

import javax.script.Invocable;

//per moment.js
public class JSInvocable {
	private final Invocable invocable;
	private final Object object;

	private JSInvocable(Invocable invocable, Object object) {
		this.invocable = invocable;
		this.object = object;
	}

	public String invoke(String method, Object...args) {
		if (args == null) { args = new Object[0]; }

		try {
			return invocable.invokeMethod(object, method, args).toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}