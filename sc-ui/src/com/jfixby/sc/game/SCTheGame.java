
package com.jfixby.sc.game;

import com.jfixby.r3.api.GameStarter;
import com.jfixby.r3.uiact.UIEventsManager;
import com.jfixby.scarabei.api.assets.ID;
import com.jfixby.scarabei.api.assets.Names;

public class SCTheGame implements GameStarter {
	public static final ID game_ui_unit_id = Names.newID("com.jfixby.sc.ui.SCMapEditor");

	@Override
	public void onGameStart () {

		UIEventsManager.loadUnit(game_ui_unit_id);
		UIEventsManager.allowUserInput();
	}

}
