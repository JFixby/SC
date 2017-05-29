
package com.jfixby.sc.desktop.run;

import com.jfixby.r3.api.screen.Screen;
import com.jfixby.r3.fokker.api.FokkerEngineAssembler;

public class SCDesktopAssembler implements FokkerEngineAssembler {

	@Override
	public void assembleEngine () {
		Screen.setDebugScaleFactor(3);
	}

}
